package bisonparser.editors;

import org.eclipse.jface.text.*;
import org.eclipse.jface.text.rules.*;
import org.eclipse.swt.graphics.Color;

/**
 * Highlights Keywords in Bison code section
 * 
 * Uses B_GRAMMAR_RULE for default
 * Uses B_KEYWORD for keywords
 * 
 * @author honda
 */
public class BisonScanner extends RuleBasedScanner {
	private static String[] bKeyWords = { "%%", "%require", "%token", "%left", "%right", "%noassoc", "%union", "%type", "%initial-action", "%parse-param", "%destructor", "%expect", "%expect-rr", "%start", "%pure-parser", "%debug", "%defines", "%file-prefix", "%debug", "%defines", "%file-prefix", "%locations", "%name-prefix", "%no-parser", "%no-lines", "%output", "%pure-parser", "%require", "%token-table", "%verbose", "%yacc" };
	
	public BisonScanner(BisonColorTable bct) {		
		IToken string;
		IRule[] rules = new IRule[2];

		// Add rule for Grammar Rule Declarations
		string = new Token(new TextAttribute((Color)bct.get(bct.B_GRAMMAR_RULE)));
			
		WordRule wordRule = new WordRule(new BisonWordDetector());
		string = new Token(new TextAttribute((Color)bct.get(bct.B_KEYWORD)));
		for(int i=0; i< bKeyWords.length; i++){
			wordRule.addWord(bKeyWords[i], string);
		}
		rules[0] = wordRule;
		
		// Add generic whitespace rule.
		rules[1] = new WhitespaceRule(new DefaultWhitespaceDetector());

		setRules(rules);
	}
}