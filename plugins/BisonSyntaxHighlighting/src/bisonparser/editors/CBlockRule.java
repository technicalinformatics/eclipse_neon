package bisonparser.editors;

import org.eclipse.jface.text.rules.*;

/**
 * Class for detecting a block of C code.
 * 
 * Works by detecting a { and finding the matching }. If it
 * finds more { it increments the depth, and if it finds a }
 * it decrements it until the entry depth is reached.
 * 
 * @author honda
 */
public class CBlockRule extends PatternRule {

	/**
	 * Keep track of the depth.
	 */
	private int depth;
	
	public CBlockRule(IToken token) {
		super("{", "}", token, (char)0, false);
	}
	
	/**
	 * Detect the ending sequence.
	 */
	protected boolean endSequenceDetected(ICharacterScanner scanner) {
		int c;
		int count = 0;
		depth=1;
		while ((c=scanner.read()) != ICharacterScanner.EOF) {
			count++;
			if(c=='{'){
				depth++;
			}
			else if(c=='}'){
				depth--;
			}
			
			if(depth<=0){
				break;
			}
		}
		if(depth<0){
			while(count>0){
				scanner.unread();
				count--;
			}
			return false;
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
