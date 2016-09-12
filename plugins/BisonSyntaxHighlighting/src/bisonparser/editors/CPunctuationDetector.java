package bisonparser.editors;

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * Defines punctuation as a word for C code.
 * 
 * Any combination of punctuation defines a punctuation word, for use in highlighting
 * 
 * @author honda
 */
class CPunctuationDetector implements IWordDetector
{
	public boolean isWordStart(char c)
	{
		if (c!='+' && c!='-' && c!='*' && c!='/' && c!='%' && c!='.' 
			&& c!='&' && c!='|' && c!='!' && c!='=' && c!='<' && c!='>'
			&& c!='{' && c!='}' && c!='(' && c!=')' && c!='[' && c!=']'
			&& c!=';' && c!=':' && c!='?' && c!='^')
		{
			return false;
		}
		return true;
	}
	
	public boolean isWordPart(char c)
	{
		if (this.isWordStart(c))
		{
			return true;
		}
		return false;
	}
}