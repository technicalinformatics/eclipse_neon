package com.bisonide.vsd.utils;
import org.eclipse.swt.SWT;

public class VSDEdge {
	String label;
	String source;
	String target;
	int color;
	int textColor;
	
	public VSDEdge() {
		label = "";
		source = "0";
		target = "0";
		color = SWT.COLOR_BLACK;
		textColor = SWT.COLOR_BLACK;
	}
	
	public VSDEdge(VSDEdge e) {
		label = e.label;
		source = e.source;
		target = e.target;
		color = e.color;
		textColor = e.textColor;
	}
	
	public VSDEdge(String newLabel, String newSource, String newTarget, int newColor, int newTextColor) {
		label = newLabel;
		source = newSource;
		target = newTarget;
		color = newColor;
		textColor = newTextColor;
	}
	
	public String getLabel() {
		return label;
	}
	
	public String getSource() {
		return source;
	}
	
	public String getTarget() {
		return target;
	}
	
	public int getColor() {
		return color;
	}
	
	public int getTextColor() {
		return textColor;
	}
}
