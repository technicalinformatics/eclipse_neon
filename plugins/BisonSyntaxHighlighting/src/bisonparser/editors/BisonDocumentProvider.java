package bisonparser.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

/**
 * Simple subclass for FileDocumentProvider
 * 
 * @author honda
 */
public class BisonDocumentProvider extends FileDocumentProvider {

	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		if (document != null) {
			IDocumentPartitioner partitioner =
				new FastPartitioner(
					new BisonPartitionScanner(),
					new String[] {
						BisonPartitionScanner.BISON_COMMENT,
						BisonPartitionScanner.BISON_PROLOGUE,
						BisonPartitionScanner.C_BLOCK,
						BisonPartitionScanner.BISON_GRAMMAR_RULE});
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return document;
	}
}