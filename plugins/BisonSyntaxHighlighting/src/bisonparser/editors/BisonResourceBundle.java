package bisonparser.editors;

/*import java.util.ListResourceBundle;

class BisonResourceBundle extends ListResourceBundle {
	public Object[][] getContents() {
		return contents;
	}
	static final Object[][] contents = { { "CorrectionAssist", "CorrectionAssist" }, { "ContentAssistProposal", "ContentAssistProposal" }, { "TemplateProposals", "TemplateProposals" }, };
}*/

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class BisonResourceBundle {

	private static final String RESOURCE_BUNDLE= "bisonparser.editors.BisonResourceBundle";//$NON-NLS-1$

	private static ResourceBundle fgResourceBundle= ResourceBundle.getBundle(RESOURCE_BUNDLE);

	private BisonResourceBundle() {
	}

	public static String getString(String key) {
		try {
			return fgResourceBundle.getString(key);
		} catch (MissingResourceException e) {
			return "!" + key + "!";//$NON-NLS-2$ //$NON-NLS-1$
		}
	}
	
	public static ResourceBundle getResourceBundle() {
		return fgResourceBundle;
	}
}