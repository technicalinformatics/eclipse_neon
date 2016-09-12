package bisonparser.editors;

import org.eclipse.jface.text.*;
import org.eclipse.jface.text.rules.*;
import org.eclipse.swt.graphics.Color;

/**
 * Scans a partion in C style.
 * 
 * Checks for C style keywords, types, constants, punctuation,
 * comments, quotes and directives
 * 
 * @author honda
 */
public class CBlockScanner extends RuleBasedScanner {
	private static String[] cKeywords = {"auto", "break", "case", "const", "continue", "default", "do", "else", "enum", "extern", "for", "goto", "if", "register", "return", "sizeof", "static", "struct", "switch", "this", "typedef", "union", "volatile", "while"};
	private static String[] cTypes = {"void", "bool", "char", "byte", "short", "int", "long", "float", "double", "signed", "unsigned"};
	private static String[] cConstants = {"false", "NULL", "true", "0"};
	private static String[] cPunctuation = {"{", "}", "(", ")", "[", "]", "->", ".", "!", "~", "+", "-", "++", "--", "&", "*", "/", "%", "<<", ">>", "<", ">", "<=", "=>", "==", "!=", "^", "|", "&&", "||", "?", "=", "*=", "/=", "%=", "+=", "-=", "&=", "^=", "|=", "<<=", ">>="};
	
	public CBlockScanner(BisonColorTable bct){//, IToken default_color) {
		IToken string;
		IToken default_color = new Token(new TextAttribute((Color)bct.get(bct.C_DEFAULT)));
		IRule[] rules = new IRule[8];

		// Add rule for double quotes
		string = new Token(new TextAttribute((Color)bct.get(bct.C_STRING)));
		rules[0] = new MultiLineRule("\"", "\"", string, '\\');
	
		// Add a rule for single quotes
		string = new Token(new TextAttribute((Color)bct.get(bct.C_STRING)));
		rules[1] = new MultiLineRule("'", "'", string, '\\');
		
		// Add a rule for C comments
		string = new Token(new TextAttribute((Color)bct.get(bct.C_COMMENT)));
		rules[2] = new EndOfLineRule("//", string);
		
		// Add a rule for C++ comments
		string = new Token(new TextAttribute((Color)bct.get(bct.C_COMMENT)));
		rules[3] = new MultiLineRule("/*", "*/", string);
		
		// Add a rule for Directives
		string = new Token(new TextAttribute((Color)bct.get(bct.C_DIRECTIVE)));
		rules[4] = new EndOfLineRule("#", string);
		
		// Add word rule for Keywords, Types, and Constants
		WordRule wordRule;
		// Add a rule for Keywords
		wordRule = new WordRule(new CWordDetector(), default_color);
		string = new Token(new TextAttribute((Color)bct.get(bct.C_KEYWORD)));
		for (int i= 0; i < cKeywords.length; i++)
			wordRule.addWord(cKeywords[i], string);		
		// Add a rule for Types
		string = new Token(new TextAttribute((Color)bct.get(bct.C_TYPE)));
		for (int i= 0; i < cTypes.length; i++)
			wordRule.addWord(cTypes[i], string);
		// Add a rule for Constants
		string = new Token(new TextAttribute((Color)bct.get(bct.C_CONSTANT)));
		for (int i= 0; i < cConstants.length; i++)
			wordRule.addWord(cConstants[i], string);
		rules[5]=wordRule;
		
		// Add word rule for Punctuation
		wordRule = new WordRule(new CPunctuationDetector(), default_color);
		for (int i= 0; i < cPunctuation.length; i++)
			wordRule.addWord(cPunctuation[i], default_color);
		rules[6]=wordRule;
		
		// Add generic whitespace rule.
		rules[7] = new WhitespaceRule(new DefaultWhitespaceDetector());

		setRules(rules);
	}
}