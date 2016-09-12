package bisonparser.editors;

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * Detects Bison Keywords
 * 
 * A Bison keyword may start with any letter _ or %
 * And may be otherwise comprised of any alphanumeric character or _ or %
 * 
 * @author honda
 */
class BisonWordDetector implements IWordDetector
{
	public boolean isWordStart(char c)
	{
		if (!Character.isLetter(c) && c != '_' && c!='%')
		{
			return false;
		}
		return true;
	}
	
	public boolean isWordPart(char c)
	{
		if (Character.isLetter(c) || Character.isDigit(c) || c == '_' || c=='%')
		{
			return true;
		}
		return false;
	}
}