package bisonparser.editors;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

/**
 * Defines whitespace.
 * 
 * Can be space, tab, newline, or cariagereturn
 * 
 * @author honda
 */
public class DefaultWhitespaceDetector implements IWhitespaceDetector {

	public boolean isWhitespace(char c) {
		return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
	}
}