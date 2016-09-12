package bisonparser.editors;

import org.eclipse.jface.text.*;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.jface.text.contentassist.ContextInformationValidator;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.graphics.Point;
import java.util.List;
import java.util.ArrayList;
import org.eclipse.jface.text.contentassist.CompletionProposal;

public class CContentAssistProcessor implements IContentAssistProcessor {

	/**
	 * list of known C keywords
	 */
	private final static String[] CKEYWORDS = new String[] { "auto","double","int","struct","break","else","long",
															"switch","case","enum","register","typedef","char",
															"extern","return","union","const","float","short",
															"unsigned","continue","for","signed","void","default",
															"goto","sizeof","volatile","do","if","static","while"};
	
	/**
	 * List of C preprocessor directive
	 */
	private final static String[] CDIRECTIVES
					= new String[] {"#if","#elif","#else","#endif","#ifndef",
									"#ifdef","#define","#undef","#include"};
	
	/**
	 * Name for Style Proposals
	 * 
	 * number of COPTIONDISPLAY entries must match up with
	 * number of COPTIONSTART entries and
	 * number of COPTIONFINISH entries and
	 * number of COPTIONOFFSET entries
	 */
	private final static String[] COPTIONDISPLAY = new String[] { "Block Comment","ifdef","ifndef" };

	/**
	 * Prefix additon for Style Proposals
	 * 
	 * number of COPTIONDISPLAY entries must match up with
	 * number of COPTIONSTART entries and
	 * number of COPTIONFINISH entries and
	 * number of COPTIONOFFSET entries
	 */
	private final static String[] COPTIONSTART = new String[] { "/*","#ifdef \n","#ifndef \n"};
	
	/**
	 * Postfix addition for Style Proposals
	 * 
	 * number of COPTIONDISPLAY entries must match up with
	 * number of COPTIONSTART entries and
	 * number of COPTIONFINISH entries and
	 * number of COPTIONOFFSET entries
	 */
	private final static String[] COPTIONFINISH = new String[] { "*/","\n#endif","\n#endif"};
	
	/**
	 * Offset for Style Proposals
	 * COPTIONOFFSET if >0 offset from front of string forward if <0 offset from
	 * back of last character + 1 (so -1 is after all inserted characters)
	 * 
	 * number of COPTIONDISPLAY entries must match up with
	 * number of COPTIONSTART entries and
	 * number of COPTIONFINISH entries and
	 * number of COPTIONOFFSET entries
	 */ 
	private final static int[] COPTIONOFFSET = new int[] {-1,7,8};

	/**
	 * @author Realsolve-PhilZoio
	 */
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,
			int documentOffset) {
		// Retrieve current document
		IDocument doc = viewer.getDocument();
		// Retrieve current selection range
		Point selectedRange = viewer.getSelectedRange();
		List propList = new ArrayList();
		if (selectedRange.y > 0) {
			try {
				// Retrieve selected text
				String text = doc.get(selectedRange.x, selectedRange.y);
				// Compute completion proposals
				computeStyleProposals(text, selectedRange, propList);
			} catch (BadLocationException e) {
			}
		} else {
			// Retrieve qualifier
			String qualifier = getQualifier(doc, documentOffset);
			// Compute completion proposals
			computeStructureProposals(qualifier, documentOffset, propList);
		}
		// Create completion proposal array
		ICompletionProposal[] proposals = new ICompletionProposal[propList
				.size()];
		// and fill with list elements
		propList.toArray(proposals);
		// Return the proposals
		return proposals;
	}
	
	public IContextInformation[] computeContextInformation(ITextViewer viewer,
			int offset) {
		return new ContextInformation[0];
	}
	
	public char[] getCompletionProposalAutoActivationCharacters() {
		return new char[] { 'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','#' };
	}

	public char[] getContextInformationAutoActivationCharacters() {
		return getCompletionProposalAutoActivationCharacters();
	}
	
	public IContextInformationValidator getContextInformationValidator() {
		return new ContextInformationValidator(this);
	}

	/**
	 * Don't know where this is called or what a descriptive
	 * error message for it should be. This is just meant to
	 * hold the place of an actualy error message can be det
	 * ermined.
	 */
	public String getErrorMessage() {
		return "ERROR #1492: New World could not be located. Please Contact your Systems Administrator.";
	}

	/**
	 * 
	 * @param doc IDocument
	 * @param documentOffset int
	 * @return String that is the word up to the cursor position
	 */
	private String getQualifier(IDocument doc, int documentOffset) {
		// Use string buffer to collect characters
		StringBuffer buf = new StringBuffer();
		while (true) {
			try {
				
				// Read character backwards
				char c = doc.getChar(--documentOffset);
				
				// This was not the start of a tag
				if (!Character.isWhitespace(c)){
					buf.append(c);
				}
				
				// Start of tag. Return qualifier
				// TODO add support of punctuation in addition to whitespace
				if (Character.isWhitespace(c))
					return buf.reverse().toString();
			} catch (BadLocationException e) {
			
			// Document start reached, no tag found
			return "";
			}
		}
	}
	
	/**
	 * Create an alphabetic list based off CKEYWORDS for proposals
	 * 
	 * @param qualifier contents to be replaced
	 * @param documentOffset offset into document
	 * @param propList List of proposals to propagate
	 */
	private void computeStructureProposals(String qualifier, int documentOffset, List propList) { 
		int qlen = qualifier.length();
		
		// Loop through all keywords
		// alphabetically
		String[] sorted = CKEYWORDS;
		java.util.Arrays.sort(sorted, String.CASE_INSENSITIVE_ORDER);
		
		for (int i = 0; i < sorted.length; i++) {
			String keyword = sorted[i];
			
			// Check if proposal matches qualifier
			if (keyword.startsWith(qualifier)) {
				CompletionProposal proposal = new CompletionProposal(keyword, documentOffset - qlen, qlen, keyword.length());
				propList.add(proposal);
			}
		}
		// Loop through all directives
		// alphabetically
		sorted = CDIRECTIVES;
		java.util.Arrays.sort(sorted, String.CASE_INSENSITIVE_ORDER);
		for (int i = 0; i < sorted.length; i++) {
			String keyword = sorted[i];
			
			// Check if proposal matches qualifier
			if (keyword.startsWith(qualifier)) {
				CompletionProposal proposal = new CompletionProposal(keyword, documentOffset - qlen, qlen, keyword.length());
				propList.add(proposal);
			}
		}
	}

	/**
	 * Create a list based off COPTIONDISPLAY, COPTIONSTART, and COPTIONFINISH
	 * for proposals. Line comments are hardcoded here.
	 * 
	 * @param selectedText text to be replaced
	 * @param selectedRange 
	 * @param propList List of proposals to propagate
	 */
	private void computeStyleProposals(String selectedText, Point selectedRange, List propList) {
		String replacement;
		int cursor;
		// Loop through all styles
		for (int i = 0; i < COPTIONDISPLAY.length; i++) {
			// Compute replacement text
			replacement = COPTIONSTART[i] + selectedText + COPTIONFINISH[i];
			// Derive cursor position (<0 offset from back+1, >=0 offset from front
			if(COPTIONOFFSET[i]<0)
				cursor = replacement.length()+COPTIONOFFSET[i]+1;
			else
				cursor = COPTIONOFFSET[i];
			// Compute a suitable context information
			IContextInformation contextInfo = new ContextInformation("Signing of the Declaration of Independence", "1776");
			// Construct proposal
			CompletionProposal proposal = new CompletionProposal(replacement,selectedRange.x, selectedRange.y, cursor, null, COPTIONDISPLAY[i],contextInfo, replacement);
			// and add to result list
			propList.add(proposal);
		}
		// Hard code comment single line ability
		replacement = "//";
		for(int i=0;i<selectedText.length();i++){
			replacement+=selectedText.charAt(i);
			if(selectedText.charAt(i)=='\n'){
				replacement+="//";
			}
		}
		cursor = replacement.length();
		IContextInformation contextInfo = new ContextInformation("French Revolution", "1789");
		CompletionProposal proposal = new CompletionProposal(replacement,selectedRange.x, selectedRange.y, cursor, null, "Line Comment",contextInfo, replacement);
		propList.add(proposal);
	}
		
}