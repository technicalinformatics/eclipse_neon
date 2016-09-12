package bisonparser;

import java.util.StringTokenizer;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "BisonParser";

	// The shared instance
	private static Activator plugin;
	
	// The look key for bison options
	public static final String BISON_OPTIONS_PREFERENCE = "bison_options";
	// The look key for highlighting options
	public static final String HIGHLIGHT_PREFERENCE = "highlight";
	// The defaults for options
	public static final String DEFAULT_BISON_OPTIONS = "bug;bogus;hack;";
	// The default for highlighting
	public static final int DEFAULT_HIGHLIGHT = SWT.COLOR_BLUE;

	//The entry delimiter
	private static String PREFERENCE_DELIMITER = ";";
	private static String VALUE_IDENTIFIER = "=";
	private static String ENTRY_DELIMITER = ",";
	private static String KEY_DELIMITER = ";";

	
	/**
	 * The constructor
	 */
	public Activator() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	/** 
	 * Initializes a preference store with default preference values 
	 * for this plug-in.
	 * @param store the preference store to fill
	 */
	protected void initializeDefaultPreferences(IPreferenceStore store) {
		store.setDefault(BISON_OPTIONS_PREFERENCE, DEFAULT_BISON_OPTIONS);
		Color color = Display.getDefault().getSystemColor(DEFAULT_HIGHLIGHT);
		PreferenceConverter.setDefault(store,HIGHLIGHT_PREFERENCE,color.getRGB());
	}

	/**
	 * Return the bad words preference default
	 * as an array of Strings.
	 * @return String[]
	 */
	public String[] getDefaultBadWordsPreference() {
		return convert(
			getPreferenceStore().getDefaultString(BISON_OPTIONS_PREFERENCE));
	}

	/**
	 * Return the bad words preference as an array of
	 * Strings.
	 * @return String[]
	 */
	public String[] getBadWordsPreference() {
		return convert(getPreferenceStore().getString(BISON_OPTIONS_PREFERENCE));
	}

	/**
	 * Convert the supplied PREFERENCE_DELIMITER delimited
	 * String to a String array.
	 * @return String[]
	 */
	private String[] convert(String preferenceValue) {
		StringTokenizer tokenizer =
			new StringTokenizer(preferenceValue, PREFERENCE_DELIMITER);
		int tokenCount = tokenizer.countTokens();
		String[] elements = new String[tokenCount];

		for (int i = 0; i < tokenCount; i++) {
			elements[i] = tokenizer.nextToken();
		}

		return elements;
	}

	/**
	 * Set the bad words preference
	 * @param String [] elements - the Strings to be 
	 * 	converted to the preference value
	 */
	public void setBadWordsPreference(String[] elements) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < elements.length; i++) {
			buffer.append(elements[i]);
			buffer.append(PREFERENCE_DELIMITER);
		}
		getPreferenceStore().setValue(BISON_OPTIONS_PREFERENCE, buffer.toString());
	}
}
