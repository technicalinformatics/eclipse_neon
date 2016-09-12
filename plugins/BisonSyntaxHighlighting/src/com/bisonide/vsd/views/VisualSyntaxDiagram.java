package com.bisonide.vsd.views;


import java.io.File;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.graphics.*;
import com.bisonide.vsd.utils.*;
import org.eclipse.swt.graphics.Font;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class VisualSyntaxDiagram extends ViewPart {
	private TableViewer viewer;
	private Image vsdImage;
	private InternalVSD currVSD;
	private Action action1;
	private Action action2;
	private Action doubleClickAction;
	private String file; 

	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 * (like Task List, for example).
	 */
	 
	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			return new String[] {""};
		}
	}
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return "";
		}
		public Image getColumnImage(Object obj, int index) {
			if (index == 0)
				return getImage(obj);
			return null;
		}
		public Image getImage(Object obj) {
			drawVSD(currVSD);
			return vsdImage;
		}
	}
	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public VisualSyntaxDiagram() {
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
			file = path.getPath() + "/" + name + ".vcg";
		}
		currVSD = new InternalVSD();
		currVSD = VCGParser.parse(file);
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		vsdImage = new Image(viewer.getControl().getDisplay(), 1500,3000);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				VisualSyntaxDiagram.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		manager.add(action2);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
	}

	private void makeActions() {
		action1 = new Action() {
			public void run() {
				currVSD = VCGParser.parse(file);
				drawVSD(currVSD);
				viewer.getControl().redraw();
				showMessage("Visual Syntax Diagram Updated");
			}
		};
		action1.setText("Update");
		action1.setToolTipText("Update Visual Syntax Diagram");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_TOOL_REDO));
		
		action2 = new Action() {
			public void run() {
				showMessage("Key:\n Regular Nodes are Cyan\n Final Nodes are Magenta");
			}
		};
		action2.setText("Key");
		action2.setToolTipText("Display Key");
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				showMessage("Double-click detected on "+obj.toString());
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}
	private void showMessage(String message) {
		MessageDialog.openInformation(
			viewer.getControl().getShell(),
			"BisonIDE VSD View",
			message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	private void drawVSD(InternalVSD vsd) {
		//currVSD = VCGParser.parse(file);
		
		Control vctrl = viewer.getControl();
		
	    GC gc = new GC(vsdImage);
		gc.setBackground(vctrl.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		gc.setForeground(vctrl.getDisplay().getSystemColor(SWT.COLOR_BLACK)); 
	    
	    Font f = new Font(vctrl.getDisplay(), "Courier", Utils.FONTSIZE, SWT.NORMAL);
	    gc.setFont(f);
	    int[] xy = new int[] {Utils.XSTART, Utils.YSTART};
	    VSDNode[] nodes = vsd.getNodes();
	    
	    if (nodes.length > 0) {
	    	drawRightArrow(gc, vctrl, "", Utils.ARROWLEN, xy[0], xy[1] + Utils.BOXHEIGHT/2);
	    	gc.setBackground(vctrl.getDisplay().getSystemColor(SWT.COLOR_WHITE));
	    	VSDNode currNode = nodes[0];
	    	xy[0] += Utils.ARROWLEN;
	    	drawVSDHelper(gc, vctrl, vsd, currNode, xy);
	    } else {
	    	gc.setBackground(vctrl.getDisplay().getSystemColor(SWT.COLOR_WHITE));
	    	gc.drawText("No nodes to display", xy[0], xy[1]);
	    }
	    
	    gc.dispose();
	    
	    //currVSD = new InternalVSD();
	}
	
	private int[] drawVSDHelper(GC gc, Control vctrl, InternalVSD vsd, VSDNode currNode, int[] startxy) {
    	int[] xy = new int[2];
    	xy[0] = startxy[0];
    	xy[1] = startxy[1];
    	
    	String name = currNode.getTitle();
    	int boxlen = name.length()*Utils.FONTWIDTH + Utils.BOXBUFFER;
    	if (currNode.isAccept())
    		gc.setBackground(vctrl.getDisplay().getSystemColor(SWT.COLOR_MAGENTA));
    	else
    		gc.setBackground(vctrl.getDisplay().getSystemColor(SWT.COLOR_CYAN));
    	drawNode(gc, name, boxlen, xy);
    	gc.setBackground(vctrl.getDisplay().getSystemColor(SWT.COLOR_WHITE));
    	
    	VSDEdge[] edges = currNode.getEdges();
    	if (edges.length > 0) {
    		int xlen = 0;
    		for (int x = 0; x < edges.length; x++) {
    			if (xlen < edges[x].getLabel().length()) {
    				xlen = edges[x].getLabel().length();
    			}
    		}
    		
    		for (int x = 0; x < edges.length; x++) {
    			if (x == 0) {
    				gc.drawLine(xy[0]+boxlen, xy[1]+Utils.BOXHEIGHT/2, xy[0]+boxlen+Utils.ARROWLEN, xy[1]+Utils.BOXHEIGHT/2);
    				drawRightArrow(gc, vctrl, edges[x].getLabel(), xlen*Utils.FONTWIDTH+Utils.ARROWLEN, xy[0]+boxlen+Utils.ARROWLEN, xy[1]+Utils.BOXHEIGHT/2);
    			}else
    				drawRightArrow(gc, vctrl, edges[x].getLabel(), xlen*Utils.FONTWIDTH+Utils.ARROWLEN, xy[0]+boxlen+Utils.ARROWLEN, xy[1]+Utils.BOXHEIGHT/2);
    			VSDNode nextNode = vsd.getNode(edges[x].getTarget());
    			gc.drawLine(xy[0]+boxlen+Utils.ARROWLEN, startxy[1]+Utils.BOXHEIGHT/2, xy[0]+boxlen+Utils.ARROWLEN, xy[1]+Utils.BOXHEIGHT/2);
    			xy[0] += (xlen*Utils.FONTWIDTH+Utils.ARROWLEN*2 + boxlen);
    			xy = drawVSDHelper(gc, vctrl, vsd, nextNode, xy);
    			xy[0] = startxy[0];
    			xy[1] += (Utils.YBUFFER+Utils.BOXHEIGHT);
    		}
    	    xy[1] -= (Utils.YBUFFER + Utils.BOXHEIGHT);
    		
    	} else {
    		drawRightArrow(gc, vctrl, "", Utils.ARROWLEN, xy[0] + boxlen, xy[1] + Utils.BOXHEIGHT/2);
    		gc.drawText("Rule: " + currNode.getRule(), xy[0]+boxlen+Utils.ARROWLEN, xy[1] + Utils.BOXHEIGHT/2 - Utils.FONTSIZE/2);
    	} 
    	
    	xy[0] = startxy[0];
    	
    	return xy;
	}
	
	private void drawNode(GC gc, String name, int length, int[] xy) {
    	gc.drawRectangle(xy[0], xy[1], length, Utils.BOXHEIGHT);
    	gc.fillRectangle(xy[0], xy[1], length, Utils.BOXHEIGHT);
    	gc.drawText(name, xy[0]+Utils.BOXBUFFER/2, xy[1]+Utils.BOXBUFFER);
	}
	
	private void drawRightArrow(GC gc, Control vctrl, String name, int length, int x, int y) {
		gc.setBackground(vctrl.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		gc.drawLine(x, y, x+length, y);
		gc.fillPolygon(new int[] {x+length, y, x+length-5, y-3, x+length-5, y+3});
		gc.setBackground(vctrl.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		gc.drawText(name, x+Utils.ARROWLEN/2, y-Utils.FONTSIZE-5);
	}
}