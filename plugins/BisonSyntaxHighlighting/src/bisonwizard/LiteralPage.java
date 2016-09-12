package bisonwizard;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (mpe).
 */

public class LiteralPage extends WizardPage {
	private Text containerText;
    private Table table;

	private Text fileText, lexFileText, singleLineCommentText, multiLineCommentBeginText, multiLineCommentEndText, whitespaceText;

	private ISelection selection;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public LiteralPage(ISelection selection) {
		super("wizardPage");
		setTitle("Multi-page Editor File");
		setDescription("This wizard creates a new file with *.mpe extension that can be opened by a multi-page editor.");
		this.selection = selection;
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		table = new Table(container, SWT.BORDER | SWT.MULTI);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;
		
//		Label instructions = new Label(container, SWT.NULL);
//		instructions.setText("Place the name of the literal in the left hand column. Place a regular expression for that literal in the right hand column.");


	    table.setLinesVisible(true);
	    for (int i = 0; i < 2; i++) {
	      TableColumn column = new TableColumn(table, SWT.NONE);
	      column.setWidth(100);
	    }
	    for (int i = 0; i < 7; i++) {
	      TableItem item = new TableItem(table, SWT.NONE);
	      item.setText(new String[] { "", ""});
	    }
	    final TableEditor editor = new TableEditor(table);
	    editor.horizontalAlignment = SWT.LEFT;
	    editor.grabHorizontal = true;
	    table.addListener(SWT.MouseDown, new Listener() {
	      public void handleEvent(Event event) {
	        Rectangle clientArea = table.getClientArea();
	        Point pt = new Point(event.x, event.y);
	        int index = table.getTopIndex();
	        while (index < table.getItemCount()) {
	          boolean visible = false;
	          final TableItem item = table.getItem(index);
	          for (int i = 0; i < table.getColumnCount(); i++) {
	            Rectangle rect = item.getBounds(i);
	            if (rect.contains(pt)) {
	              final int column = i;
	              final Text text = new Text(table, SWT.NONE);
	              Listener textListener = new Listener() {
	                public void handleEvent(final Event e) {
	                  switch (e.type) {
	                  case SWT.FocusOut:
	                    item.setText(column, text.getText());
	                    text.dispose();
	                    break;
	                  case SWT.Traverse:
	                    switch (e.detail) {
	                    case SWT.TRAVERSE_RETURN:
	                      item
	                          .setText(column, text
	                              .getText());
	                    // FALL THROUGH
	                    case SWT.TRAVERSE_ESCAPE:
	                      text.dispose();
	                      e.doit = false;
	                    }
	                    break;
	                  }
	                }
	              };
	              text.addListener(SWT.FocusOut, textListener);
	              text.addListener(SWT.Traverse, textListener);
	              editor.setEditor(text, item, i);
	              text.setText(item.getText(i));
	              text.selectAll();
	              text.setFocus();
	              return;
	            }
	            if (!visible && rect.intersects(clientArea)) {
	              visible = true;
	            }
	          }
	          if (!visible)
	            return;
	          index++;
	        }
	      }
	    });

		Button addButton = new Button(container, SWT.PUSH + SWT.RIGHT);
		addButton.setText("Add Token");
		addButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

			      TableItem item = new TableItem(table, SWT.NONE);
			      item.setText("");
			}
		});
		Label whiteSpace1 = new Label(container, SWT.NULL);
		whiteSpace1.setText("");

		Label singleLineCommentLabel = new Label(container, SWT.NULL);
		singleLineCommentLabel.setText("&Single Line Comment:");
		
		singleLineCommentText = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		singleLineCommentText.setLayoutData(gd);
		singleLineCommentText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		Label whiteSpace2 = new Label(container, SWT.NULL);
		whiteSpace2.setText("");
		
		Label multiLineCommentLabel = new Label(container, SWT.NULL);
		multiLineCommentLabel.setText("&Multi Line Comment Begin:");
		
		multiLineCommentBeginText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		multiLineCommentBeginText.setLayoutData(gd);
		multiLineCommentBeginText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		Label whiteSpace3 = new Label(container, SWT.NULL);
		whiteSpace3.setText("");
		
		Label multiLineCommentEndLabel = new Label(container, SWT.NULL);
		multiLineCommentEndLabel.setText("&Multi Line Comment End:");
		
		multiLineCommentEndText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		multiLineCommentEndText.setLayoutData(gd);
		multiLineCommentEndText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		Label whiteSpace4 = new Label(container, SWT.NULL);
		whiteSpace4.setText("");
		
		Label whitespaceLabel = new Label(container, SWT.NULL);
		whitespaceLabel.setText("&White space:");
		
		whitespaceText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		whitespaceText.setLayoutData(gd);
		whitespaceText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		Label whiteSpace5 = new Label(container, SWT.NULL);
		whiteSpace5.setText("");
		
		Label label = new Label(container, SWT.NULL);
		label.setText("&Container:");
		
		containerText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		containerText.setLayoutData(gd);
		containerText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		Button button = new Button(container, SWT.PUSH);
		button.setText("Browse...");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});
		label = new Label(container, SWT.NULL);
		label.setText("&Bison file name:");

		fileText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		fileText.setLayoutData(gd);
		fileText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		Label whiteSpace6 = new Label(container, SWT.NULL);
		whiteSpace6.setText("");

		Label label2 = new Label(container, SWT.NULL);
		label2.setText("&Lex file name:");

		lexFileText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		lexFileText.setLayoutData(gd);
		lexFileText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		initialize();
		dialogChanged();
		setControl(container);
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
		if (selection != null && selection.isEmpty() == false
				&& selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.size() > 1)
				return;
			Object obj = ssel.getFirstElement();
			if (obj instanceof IResource) {
				IContainer container;
				if (obj instanceof IContainer)
					container = (IContainer) obj;
				else
					container = ((IResource) obj).getParent();
				containerText.setText(container.getFullPath().toString());
			}
		}
		fileText.setText("new_file.y");
		lexFileText.setText("new_file.l");
	}

	/**
	 * Uses the standard container selection dialog to choose the new value for
	 * the container field.
	 */

	private void handleBrowse() {
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(
				getShell(), ResourcesPlugin.getWorkspace().getRoot(), false,
				"Select new file container");
		if (dialog.open() == ContainerSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
				containerText.setText(((Path) result[0]).toString());
			}
		}
	}

	/**
	 * Ensures that both text fields are set.
	 */

	private void dialogChanged() {
		IResource container = ResourcesPlugin.getWorkspace().getRoot()
				.findMember(new Path(getContainerName()));
		String fileName = getBisonFileName();

		if (getContainerName().length() == 0) {
			updateStatus("File container must be specified");
			return;
		}
		if (container == null
				|| (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
			updateStatus("File container must exist");
			return;
		}
		if (!container.isAccessible()) {
			updateStatus("Project must be writable");
			return;
		}
		if (fileName.length() == 0) {
			updateStatus("File name must be specified");
			return;
		}
		if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
			updateStatus("File name must be valid");
			return;
		}
		int dotLoc = fileName.lastIndexOf('.');
		if (dotLoc != -1) {
			String ext = fileName.substring(dotLoc + 1);
			if (ext.equalsIgnoreCase("y") == false) {
				updateStatus("File extension must be \"y\"");
				return;
			}
		}
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getContainerName() {
		return containerText.getText();
	}

	public String getBisonFileName() {
		return fileText.getText();
	}
	
	public String getLexFileName() {
		return lexFileText.getText();
	}
	
	public String getSingleLineComment() {
		return singleLineCommentText.getText();
	}
	
	public String getMultiLineCommentBegin() {
		return multiLineCommentBeginText.getText();
	}
	
	public String getMultiLineCommentEnd() {
		return multiLineCommentEndText.getText();
	}
	
	public String getWhitespace() {
		return whitespaceText.getText();
	}
	
	public String getBisonContents() {
		String yfile = "%{\n#include <stdio.h>\n#include <string.h>\nvoid yyerror(const char *str)\n{\n\tfprintf(stderr,\"error: %s\",str);\n}\n\nint yywrap()\n{\n\treturn 1;\n} \n  \nmain()\n{\n\tyyparse();\n} \n%}\n\n";
		
		int index = table.getTopIndex();
		System.out.println(index);
		
		while(index < table.getItemCount() && table.getItem(index).getText(0) != "") {
			yfile += "%token " + table.getItem(index).getText(0) + "\n";
			index += 1;
		}
		
		yfile += "\n%%\n\n/* TODO: Autogenerated file. Bison rules go here */\n\n%%\n\n/* TODO: Autogenerated file. Additional C code goes here */";		
		
		
		System.out.println("done");
		
		return yfile;
	}
	
	public String getLexContents() {
		String lexFile = "%{\n\n#include <stdio.h>\n#include \"" + getBisonFileName().replaceAll("\\.y$", "") + ".tab.h\"\n%}\n\n%%\n\n";
		
		int index = table.getTopIndex();
		System.out.println(index);
		
		while(index < table.getItemCount() && table.getItem(index).getText(1) != "") {
			if(table.getItem(index).getText(0) != "") {
					lexFile += table.getItem(index).getText(1) + "\treturn " + table.getItem(index).getText(0) + ";\n";
			}
			else {
				lexFile += table.getItem(index).getText(1) + "\t/* ignore */;\n";
			}
			index += 1;
		}
		
		if(getSingleLineComment() != "")  {
			lexFile += escapeSpecialChars(getSingleLineComment()) + ".*?$\t\t/* ignore single line comment */;\n";
		}
		
		if(getWhitespace() != "")  {
			lexFile += "[" + getWhitespace() + "]\t\t/* ignore whitespace */;\n";
		}
		
		if(getMultiLineCommentBegin() != "" && getMultiLineCommentEnd() != "")  {
			lexFile += escapeSpecialChars(getMultiLineCommentBegin()) + ".*?" + escapeSpecialChars(getMultiLineCommentEnd()) + "\t\t/* ignore multiline comment */;\n";
		}
		
		lexFile += "\n\n%%";
		
		return lexFile;
	}
	
	String escapeSpecialChars(String s) {
		String escapedStr = "";
		for(int i = 0; i < s.length(); i++) {
			if(s.charAt(i) == '[' || s.charAt(i) == '\\' || s.charAt(i) == '$' || s.charAt(i) == '.' || 
				s.charAt(i) == '|' || s.charAt(i) == '?' || s.charAt(i) == '*' || s.charAt(i) == '+' ||
				s.charAt(i) == '(' || s.charAt(i) == ')' || s.charAt(i) == '/') {
				escapedStr += '\\';
			}
			escapedStr += s.charAt(i);
		}
		return escapedStr;
	}
}