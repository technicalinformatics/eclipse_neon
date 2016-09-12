package bisonparser.editors;

import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.*;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.contentassist.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Extends SourceViewerConfiguration
 * @author honda
 */
public class BisonConfiguration extends SourceViewerConfiguration {
	private XMLDoubleClickStrategy doubleClickStrategy;
	private BisonColorTable colorTable;
	private RuleBasedScanner cblockscanner;
	private RuleBasedScanner bisonscanner;
	private RuleBasedScanner grammarrulescanner;

	public BisonConfiguration(BisonColorTable colortable) {
		this.colorTable = colortable;
	}
	
	/**
	 * @return String of available partion types
	 */
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {
			IDocument.DEFAULT_CONTENT_TYPE,
			BisonPartitionScanner.BISON_COMMENT,
			BisonPartitionScanner.BISON_PROLOGUE,
			BisonPartitionScanner.C_BLOCK };
	}
	
	/**
	 * Provide an instance of ITextDoubleClickStrategy
	 * This is code from example. Should be changed to fit Bison
	 * @return XMLDoubleClickStrategy
	 */
	public ITextDoubleClickStrategy getDoubleClickStrategy(
		ISourceViewer sourceViewer,
		String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new XMLDoubleClickStrategy();
		return doubleClickStrategy;
	}
	
	/**
	 * Provide an instance of BisonScanner
	 * @return BisonScanner
	 */
	protected RuleBasedScanner getBisonScanner() {
		if (bisonscanner == null) {
			bisonscanner = new BisonScanner(colorTable);
			bisonscanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						(Color)colorTable.get(colorTable.B_DEFAULT))));
		}
		return bisonscanner;
	}
	
	/**
	 * Provide an instance of GrammarRuleScanner
	 * Implemented same as CBlockScanner
	 * @return CBlockScanner
	 */
	protected RuleBasedScanner getGrammarRuleScanner() {
		if (grammarrulescanner == null) {
			grammarrulescanner = new CBlockScanner(colorTable);
			grammarrulescanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						(Color)colorTable.get(colorTable.B_GRAMMAR_RULE))));
		}
		return grammarrulescanner;
	}
	
	/**
	 * Provide an instance of CBlockScanner
	 * @return CBlockScanner
	 */
	protected RuleBasedScanner getCBlockScanner() {
		if (cblockscanner == null) {
			cblockscanner = new CBlockScanner(colorTable);
			cblockscanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						(Color)colorTable.get(colorTable.C_DEFAULT))));
		}
		return cblockscanner;
	}
	
	/**
	 * Set up content assistant
	 */
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer)
	{

	    ContentAssistant assistant = new ContentAssistant();

	    IContentAssistProcessor CProcessor = new CContentAssistProcessor();
	    IContentAssistProcessor BisonProcessor = new BisonContentAssistProcessor();
	    assistant.setContentAssistProcessor(CProcessor, BisonPartitionScanner.C_BLOCK);
	    assistant.setContentAssistProcessor(CProcessor, BisonPartitionScanner.BISON_PROLOGUE);
	    assistant.setContentAssistProcessor(BisonProcessor, IDocument.DEFAULT_CONTENT_TYPE);
	    assistant.enableAutoActivation(true);
	    assistant.setAutoActivationDelay(500);
	    assistant.setProposalPopupOrientation(IContentAssistant.CONTEXT_INFO_BELOW);
	    assistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_BELOW);
	    return assistant;

	}

/**
 * Set partions scanners
 */	
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr;
		
		dr = new DefaultDamagerRepairer(getBisonScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
		
		dr = new DefaultDamagerRepairer(getCBlockScanner());
		reconciler.setDamager(dr, BisonPartitionScanner.BISON_PROLOGUE);
		reconciler.setRepairer(dr, BisonPartitionScanner.BISON_PROLOGUE);
		
		dr = new DefaultDamagerRepairer(getGrammarRuleScanner());
		reconciler.setDamager(dr, BisonPartitionScanner.BISON_GRAMMAR_RULE);
		reconciler.setRepairer(dr, BisonPartitionScanner.BISON_GRAMMAR_RULE);
		
		dr = new DefaultDamagerRepairer(getCBlockScanner());
		reconciler.setDamager(dr, BisonPartitionScanner.C_BLOCK);
		reconciler.setRepairer(dr, BisonPartitionScanner.C_BLOCK);

		NonRuleBasedDamagerRepairer ndr;

		ndr = new NonRuleBasedDamagerRepairer(
				new TextAttribute(
					(Color)colorTable.get(colorTable.B_COMMENT)));
		reconciler.setDamager(ndr, BisonPartitionScanner.BISON_COMMENT);
		reconciler.setRepairer(ndr, BisonPartitionScanner.BISON_COMMENT);
		
		return reconciler;
	}

	public IInformationControlCreator getInformationControlCreator(ISourceViewer sourceViewer) {
		return new IInformationControlCreator() {
			public IInformationControl createInformationControl(Shell parent) {
				return new DefaultInformationControl(parent,presenter);
			}
		};
	}

	private static final DefaultInformationControl.IInformationPresenter presenter = new DefaultInformationControl.IInformationPresenter() {
		public String updatePresentation(Display display, String infoText,
		TextPresentation presentation, int maxWidth, int maxHeight) {
			int start = -1;
			// Loop over all characters of information text
			for (int i = 0; i < infoText.length(); i++) {
				switch (infoText.charAt(i)) {
					case '<' :
					// Remember start of tag
					start = i;
					break;
					case '>' :
					if (start >= 0) {
						// We have found a tag and create a new style range
						StyleRange range = new StyleRange(start, i - start + 1, null, null, SWT.BOLD);
						// Add this style range to the presentation
						presentation.addStyleRange(range);
						// Reset tag start indicator
						start = -1;
					}
					break;
				}
			}
		// Return the information text
		return infoText;
		}
	};
	
}