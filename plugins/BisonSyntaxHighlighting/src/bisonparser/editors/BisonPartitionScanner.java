package bisonparser.editors;

import org.eclipse.jface.text.rules.*;

/**
 * A partion scanner for Bison
 * 
 * Has 4 types of partions
 *  -C_BLOCK for C code embeded in Bison
 *  -BISON_COMMENT for // and /* style comments
 *  -BISON_PROLOGUE for %{ type blocks
 *  -BISON_GRAMMAR_RULE for Bison code defines a grammar
 * 
 * @author honda
 */
public class BisonPartitionScanner extends RuleBasedPartitionScanner {
	public final static String C_BLOCK = "__c_block";
	public final static String BISON_COMMENT = "__bison_comment";
	public final static String BISON_PROLOGUE = "__bison_prologue";
	public final static String BISON_GRAMMAR_RULE = "__bison_grammar_rule";

	public BisonPartitionScanner() {

		IToken cBlock = new Token(C_BLOCK);
		IToken bisonComment = new Token(BISON_COMMENT);
		IToken bisonPrologue = new Token(BISON_PROLOGUE);
		IToken bisonGrammarRule = new Token(BISON_GRAMMAR_RULE);
		
		IPredicateRule[] rules = new IPredicateRule[4];

		rules[0] = new MultiLineRule("/*", "*/", bisonComment);
		rules[1] = new MultiLineRule("%{", "%}", bisonPrologue);
		rules[2] = new CBlockRule(cBlock);
		rules[3] = new GrammarRuleRule(bisonGrammarRule);
//		rules[3] = new MultiLineRule(":", ";", bisonGrammarRule);

		setPredicateRules(rules);
	}
}
