package bisonparser.editors;

import org.eclipse.jface.text.*;
import org.eclipse.jface.text.ITextViewer;
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

public class BisonContentAssistProcessor implements IContentAssistProcessor {

	/**
	 * list of known bison keywords
	 */
	private final static String[] BISONKEYWORDS
					= new String[] { "error","YYABORT","YYACCEPT","YYBACKUP","YYERROR",
									"YYERROR_VERBOSE","YYINITDEPTH","YYLEX_PARAM","YYLTYPE",
									"yyltype","YYMAXDEPTH","YYPARSE_PARAM","YYRECOVERING",
									"YYSTYPE","yychar","yyclearin","yydebug","yyerrok",
									"yyerror","yylex","yylval","yylloc","yynerrs","yyparse",
									"%left","%no_lines","%nonassoc","%prec","%pure_parser",
									"%raw","%right","%start","%token","%token_table","%type",
									"%union","%%","%{","%}"};
	
	/**
	 * Name for Style Proposals
	 * 
	 * number of BISONOPTIONDISPLAY entries must match up with
	 * number of BISONOPTIONSTART entries and
	 * number of BISONOPTIONFINISH entries
	 */
	private final static String[] BISONOPTIONDISPLAY = new String[] { "Block Comment","Prologue" };
	
	/**
	 * Prefix addition for Style Proposals
	 * 
	 * number of BISONOPTIONDISPLAY entries must match up with
	 * number of BISONOPTIONSTART entries and
	 * number of BISONOPTIONFINISH entries
	 */
	private final static String[] BISONOPTIONSTART = new String[] { "/*","%{\n"};
	
	/**
	 * Postfix addition for Style Proposals
	 * 
	 * number of BISONOPTIONDISPLAY entries must match up with
	 * number of BISONOPTIONSTART entries and
	 * number of BISONOPTIONFINISH entries
	 */
	private final static String[] BISONOPTIONFINISH = new String[] { "*/","\n%}"};

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
		return new char[] { 'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','%' };
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
		return "ERROR #1492: New World could not be located. Please report how you got to this message.";
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
	 * Create an alphabetic list based off BISONKEYWORDS for proposals
	 * 
	 * @param qualifier contents to be replaced
	 * @param documentOffset offset into document
	 * @param propList List of proposals to propagate
	 */
	private void computeStructureProposals(String qualifier, int documentOffset, List propList) { 
		// sort alphabetically
		String[] sorted = BISONKEYWORDS;
		java.util.Arrays.sort(sorted, String.CASE_INSENSITIVE_ORDER);
		int qlen = qualifier.length();
		// Loop through all proposals
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
	 * Create a list based off BISONOPTIONDISPLAY, BISONOPTIONSTART,
	 * and BISONOPTIONFINISH for proposals. Line comments are hard-
	 * coded here.
	 * 
	 * @param selectedText text to be replaced
	 * @param selectedRange 
	 * @param propList List of proposals to propagate
	 */
	private void computeStyleProposals(String selectedText, Point selectedRange, List propList) {
		String replacement;
		int cursor;
		// Loop through all styles
		for (int i = 0; i < BISONOPTIONDISPLAY.length; i++) {
			// Compute replacement text
			replacement = BISONOPTIONSTART[i] + selectedText + BISONOPTIONFINISH[i];
			// Derive cursor position
			cursor = replacement.length();
			// Compute a suitable context information
			IContextInformation contextInfo = new ContextInformation("Norman Conquest", "1066");
			// Construct proposal
			CompletionProposal proposal = new CompletionProposal(replacement,selectedRange.x, selectedRange.y, cursor, null, BISONOPTIONDISPLAY[i],contextInfo, replacement);
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
		IContextInformation contextInfo = new ContextInformation("Sacking of Rome", "1527");
		CompletionProposal proposal = new CompletionProposal(replacement,selectedRange.x, selectedRange.y, cursor, null, "Line Comment",contextInfo, replacement);
		propList.add(proposal);
	}
		
}