package bisonparser.editors;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import bisonparser.Activator;

/**
 * Deals with Preferece API (not currently used)
 * The BadWordsColorPreferencePage is a preference page that
 * handles setting the colors used by the editors.
 * @deprecated
 */

public class BadWordsColorPreferencePage
	extends PreferencePage
	implements IWorkbenchPreferencePage {

	private ColorFieldEditor colorEditor;

	/*
	 * @see PreferencePage#createContents(Composite)
	 */
	protected Control createContents(Composite parent) {

		Composite entryTable = new Composite(parent, SWT.NULL);

		//Create a data that takes up the extra space in the dialog .
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.grabExcessHorizontalSpace = true;
		entryTable.setLayoutData(data);

		GridLayout layout = new GridLayout();
		entryTable.setLayout(layout);
		
		Composite colorComposite = new Composite(entryTable,SWT.NONE);

		colorComposite.setLayout(new GridLayout());
		
		//Create a data that takes up the extra space in the dialog.
		colorComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		colorEditor =
			new ColorFieldEditor(
//				Activator.HIGHLIGHT_PREFERENCE,
				Activator.BISON_OPTIONS_PREFERENCE,
				"Highlight Color",
				colorComposite);				
			
		
		//Set the editor up to use this page
		//colorEditor.setPreferencePage(this);
		colorEditor.setPage(this);
		colorEditor.setPreferenceStore(getPreferenceStore());
		colorEditor.load();

		return entryTable;
	}

	/*
	 * @see IWorkbenchPreferencePage#init(IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		//Initialize the preference store we wish to use
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	/**
	 * Performs special processing when this page's Restore Defaults button has 
	 * been pressed.
	 * Sets the contents of the color field to the default value in the preference
	 * store.
	 */
	protected void performDefaults() {
		colorEditor.loadDefault();
	}
	/** 
	 * Method declared on IPreferencePage. Save the
	 * color preference to the preference store.
	 */
	public boolean performOk() {
		colorEditor.store();
		return super.performOk();
	}


}