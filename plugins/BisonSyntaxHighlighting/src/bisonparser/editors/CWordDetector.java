package bisonparser.editors;

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * Defines words for C code.
 * 
 * Can start with letter or underscore
 * Can continue with letter number or underscore
 * 
 * @author honda
 */
class CWordDetector implements IWordDetector
{
	public boolean isWordStart(char c)
	{
		if (!Character.isLetter(c) && c != '_')
		{
			return false;
		}
		return true;
	}
	
	public boolean isWordPart(char c)
	{
		if (Character.isLetter(c) || Character.isDigit(c) || c == '_' )
		{
			return true;
		}
		return false;
	}
}