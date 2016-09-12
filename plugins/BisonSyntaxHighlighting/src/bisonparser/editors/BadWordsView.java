package bisonparser.editors;

import java.io.IOException;
import java.io.InputStream;

//import org.eclipse.core.resources.IFile;
//import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import bisonparser.Activator;

/**
 * Deals with Preferece API (not currently used)
 * The BadWordsView is a simple view that creates a StyledText'
 * that highlights the bad words.
 * @deprecated
 */
public class BadWordsView extends ViewPart {

	private Color foreground;
	private StyledText text;

	ISelectionListener selectionListener = new ISelectionListener() {

		/**
		 * @see org.eclipse.ui.ISelectionListener#selectionChanged(IWorkbenchPart, ISelection)
		 */
		public void selectionChanged(
			IWorkbenchPart part,
			ISelection selection) {
			setText(selection);
		}

	};

	IPropertyChangeListener preferenceListener =
		new IPropertyChangeListener() {
		/*
		 * @see IPropertyChangeListener.propertyChange()
		 */
		public void propertyChange(PropertyChangeEvent event) {

			if (event
				.getProperty()
				.equals(Activator.HIGHLIGHT_PREFERENCE)) {
				//Update the colors by clearing the current color,
				//updating the view and then disposing the old color.
				Color oldForeground = foreground;
				foreground = null;
				setBadWordHighlights(text.getText());
				oldForeground.dispose();
			}
			if (event
				.getProperty()
				.equals(Activator.BISON_OPTIONS_PREFERENCE))
				//Only update the text if only the words have changed
				setBadWordHighlights(text.getText());
		}
	};

	/**
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(Composite)
	 */
	public void createPartControl(Composite parent) {
		text = new StyledText(parent, SWT.NULL);
		GridData data = new GridData(GridData.FILL_BOTH);
		text.setLayoutData(data);
		setText(
			getSite()
				.getWorkbenchWindow()
				.getSelectionService()
				.getSelection());
	}

	/**
	 * Set the text value in the widget passed on the supplied
	 * selection
	 */

	private void setText(ISelection selection) {

		if (selection instanceof IStructuredSelection) {
//			Object firstObject =
//				((IStructuredSelection) selection).getFirstElement();
//			if (firstObject != null && firstObject instanceof IAdaptable) {
//				Object resource =
//					((IAdaptable) firstObject).getAdapter(IResource.class);
//				if (resource != null && resource instanceof IFile) {
//					try {
//						InputStream contents = ((IFile) resource).getContents();
//						byte[] data = new byte[contents.available()];
//						contents.read(data);
//						setBadWordHighlights(new String(data));
//					} catch (CoreException exception) {
//						//Do nothing
//					} catch (IOException exception) {
//						//Do nothing
//					}
//				}
//			}
		}

	}

	/**
	 * Set the highlights for the bad words using the passed in
	 * text
	 */

	private void setBadWordHighlights(String fileContents) {

		Color foregroundColor = getForegroundColor();
		text.setText(fileContents);
		String[] badStrings =
			Activator.getDefault().getBadWordsPreference();
		Color background = text.getBackground();

		for (int i = 0; i < badStrings.length; i++) {
			String badWord = badStrings[i];
			int length = badWord.length();
			int nextIndex = 0;
			while (nextIndex > -1) {
				nextIndex = fileContents.indexOf(badWord, nextIndex);
				if (nextIndex > -1) {
					StyleRange range =
						new StyleRange(
							nextIndex,
							length,
							foregroundColor,
							background);
					text.setStyleRange(range);
					nextIndex += length;
				}

			}

		}
	}
	/**
	 * Get the current value of the foreground color.
	 * Create it if it does not exist yet.
	 */

	private Color getForegroundColor() {
		if (foreground == null)
			foreground =
				new Color(
					text.getDisplay(),
					PreferenceConverter.getColor(
						Activator.getDefault().getPreferenceStore(),
						Activator.HIGHLIGHT_PREFERENCE));
		return foreground;
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	public void setFocus() {
	}

	/* (non-Javadoc)
	* Method declared on IViewPart.
	*/
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		site.getPage().addSelectionListener(selectionListener);
		Activator
			.getDefault()
			.getPreferenceStore()
			.addPropertyChangeListener(preferenceListener);
	}

	/* (non-Javadoc)
	* Method declared on IWorkbenchPart.
	*/
	public void dispose() {
		// remove ourselves as a selection listener
		getSite().getPage().removeSelectionListener(selectionListener);
		//and no longer listen to the preference store
		Activator
			.getDefault()
			.getPreferenceStore()
			.removePropertyChangeListener(preferenceListener);
		if (foreground != null)
			foreground.dispose();
		// run super.
		super.dispose();
	}

}
