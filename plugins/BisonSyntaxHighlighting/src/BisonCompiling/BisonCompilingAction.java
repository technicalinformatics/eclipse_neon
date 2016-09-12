package BisonCompiling;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;


	


public class BisonCompilingAction implements IWorkbenchWindowActionDelegate {

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		MessageConsole console = new MessageConsole("System Output", null);
		ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] { console });
		ConsolePlugin.getDefault().getConsoleManager().showConsoleView(console);
		MessageConsoleStream stream = console.newMessageStream();

		System.setOut(new PrintStream(stream));
		System.setErr(new PrintStream(stream));
		
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		IWorkbenchPage page = win.getActivePage();
		IEditorPart editor = page.getActiveEditor();
		IPath fullIPath = ((IPathEditorInput)editor.getEditorInput()).getPath();
		File fullFilePath = fullIPath.toFile();
		String fullPath = fullFilePath.getPath();
		String fileName = fullFilePath.getName();
		if (fileName.substring(fileName.length() - 2).equals(".y") || fileName.substring(fileName.length() - 2).equals(".l")) {
			String name = fileName.substring(0, fileName.length() - 2);
			File path = new File(fullPath.substring(0, fullPath.length() - fileName.length()));
			System.out.println("Compiling " + name + " in directory " + path + "...\n");
			
			try {
				int i;
				boolean BisonError = false;
				boolean LexError = false;

				System.out.println("Compiling Bison...\n");
				Process p1 = Runtime.getRuntime().exec("bison -dg " + name + ".y", null, path);
				while ((i = p1.getErrorStream().read()) != -1) {
					System.out.print((char)i);
					BisonError = true;
				}
				if (!BisonError)
					System.out.println("Compiled Bison without error.\n");

				System.out.println("Compiling Lex...\n");
				Process p2 = Runtime.getRuntime().exec("lex " + name + ".l", null, path);
				while ((i = p2.getErrorStream().read()) != -1) {
					System.out.print((char)i);
					LexError = true;
				}
				if (!LexError)
					System.out.println("Compiled Lex without error.\n");
				
				if (BisonError || LexError) {
					System.out.println("Cannot compile because of error in compiling Bison or Lex code.");
				}
				else {
					boolean error = false;
					System.out.println("Compiling executable...\n");
					Process p3 = Runtime.getRuntime().exec("gcc -g lex.yy.c " + name + ".tab.c" + " -ll -o " + name, null, path);
					while ((i = p3.getErrorStream().read()) != -1) {
						System.out.print((char)i);
					}
					if (!error)
						System.out.println("Compiled executable without error.");
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else
			System.out.println("File is not a Bison or Lex File.");
		
	}

	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

}
