package bisonparser.editors;

import org.eclipse.jface.text.rules.*;

/**
 * Provided by Eclipse XMLEditor example; deprecated
 * 
 * @deprecated
 * @author Eclipse
 */
public class TagRule extends MultiLineRule {

	public TagRule(IToken token) {
		super("<", ">", token);
	}
	protected boolean sequenceDetected(ICharacterScanner scanner,
										char[] sequence,
										boolean eofAllowed) {
		int c = scanner.read();
		if (sequence[0] == '<') {
			if (c == '?') {
				// processing instruction - abort
				scanner.unread();
				return false;
			}
			if (c == '!') {
				scanner.unread();
				// comment - abort
				return false;
			}
		} else if (sequence[0] == '>') {
			scanner.unread();
		}
		return super.sequenceDetected(scanner, sequence, eofAllowed);
	}
}
