package bisonparser.editors;

import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.TextEditorAction;
import org.eclipse.ui.texteditor.TextOperationAction;
import org.eclipse.jface.action.IAction;
import java.util.ResourceBundle;
import java.util.MissingResourceException;

/**
 * Implementation of TextEditor
 * @author honda
 */
public class BisonEditor extends TextEditor {
	
	private ResourceBundle resourceBundle;
	private BisonColorTable bcolortable;

	/**
	 * Creates BisonColorTable
	 * Creates and attaches BisonConfiguration as SourceViewerConfiguration
	 * Creates and sets BisonDocumentProvider as DocumentProvider
	 */
	public BisonEditor() {
		super();
		bcolortable = new BisonColorTable();
		setSourceViewerConfiguration(new BisonConfiguration(bcolortable));
		setDocumentProvider(new BisonDocumentProvider());
		try
		{
			resourceBundle=ResourceBundle.getBundle("bisonparser.editors.BisonResourceBundle");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}
	
	public void dispose() {
		// TODO create dispose method for bison color table
		super.dispose();
	}

	/**
	 * Deals with setup for ContentAssist
	 */
	protected void createActions() {
		super.createActions();
		
		IAction a= new TextOperationAction(resourceBundle, "ContentAssistProposal.", this, ISourceViewer.CONTENTASSIST_PROPOSALS); //$NON-NLS-1$
		a.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
		setAction("ContentAssistProposal", a); //$NON-NLS-1$
		
		a= new TextOperationAction(resourceBundle, "ContentAssistTip.", this, ISourceViewer.CONTENTASSIST_CONTEXT_INFORMATION);  //$NON-NLS-1$
		a.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_CONTEXT_INFORMATION);
		setAction("ContentAssistTip", a); //$NON-NLS-1$
		
		a= new DefineFoldingRegionAction(resourceBundle, "DefineFoldingRegion.", this); //$NON-NLS-1$
		setAction("DefineFoldingRegion", a); //$NON-NLS-1$
	}
	
	/**
	 * This may need to be removed due to license issues:
	 * Copyright (c) 2000, 2005 IBM Corporation and others.
	 * All rights reserved. This program and the accompanying materials
	 * are made available under the terms of the Eclipse Public License v1.0
	 * which accompanies this distribution, and is available at
	 * http://www.eclipse.org/legal/epl-v10.html
	 * @author org.eclipse.ui.examples.javaeditor
	 */
	private class DefineFoldingRegionAction extends TextEditorAction {
		public DefineFoldingRegionAction(ResourceBundle bundle, String prefix, ITextEditor editor) {
			super(bundle, prefix, editor);
		}
		
		private IAnnotationModel getAnnotationModel(ITextEditor editor) {
			return (IAnnotationModel) editor.getAdapter(ProjectionAnnotationModel.class);
		}
		
		/*
		 * @see org.eclipse.jface.action.Action#run()
		 */
		public void run() {
			ITextEditor editor= getTextEditor();
			ISelection selection= editor.getSelectionProvider().getSelection();
			if (selection instanceof ITextSelection) {
				ITextSelection textSelection= (ITextSelection) selection;
				if (!textSelection.isEmpty()) {
					IAnnotationModel model= getAnnotationModel(editor);
					if (model != null) {
						
						int start= textSelection.getStartLine();
						int end= textSelection.getEndLine();
						
						try {
							IDocument document= editor.getDocumentProvider().getDocument(editor.getEditorInput());
							int offset= document.getLineOffset(start);
							int endOffset= document.getLineOffset(end + 1);
							Position position= new Position(offset, endOffset - offset);
							model.addAnnotation(new ProjectionAnnotation(), position);
						} catch (BadLocationException x) {
							// ignore
						}
					}
				}
			}
		}
	}
}
