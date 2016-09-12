package bisonparser.editors;

import org.eclipse.jface.text.rules.*;

/**
 * Defines the Bison grammar definition partition 
 * 
 * @author honda
 */
public class GrammarRuleRule extends PatternRule {

	private int depth;
	
	/**
	 * define the grammar definition region between ':' and ';'
	 * 
	 * @param token
	 */
	public GrammarRuleRule(IToken token) {
		super(":", ";", token, (char)0, false);
	}
	
	/**
	 * make sure to not count ending marks inside curly braces '{'
	 * 
	 * @see CBlockRule
	 */
	protected boolean endSequenceDetected(ICharacterScanner scanner) {
		int c;
		int count = 0;
		depth=0;
		while ((c=scanner.read()) != ICharacterScanner.EOF) {
			count++;
			if(c=='{'){
				depth++;
			}
			else if(c=='}'){
				depth--;
			}
			
			if(depth<=0 && c==fEndSequence[0]){
				break;
			}
		}
		
		if(depth==0){
			return true;
		}
		if(c==ICharacterScanner.EOF){
			return true;
		}
		scanner.unread();
		
		return true;
	}
	
}