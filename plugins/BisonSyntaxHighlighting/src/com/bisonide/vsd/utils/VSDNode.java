package com.bisonide.vsd.utils;

public class VSDNode {
	private String title;
	private String label;
	private VSDEdge[] edges;
	private boolean isAccept;
	
	public VSDNode() {
		title = "";
		label = "";
		edges = new VSDEdge[0];
		isAccept = false;
	}
	
	public VSDNode(String t, String l) {
		title = t;
		label = l;
		edges = new VSDEdge[0];
		isAccept = false;
	}
	
	public VSDNode(String t, String l, boolean accept) {
		title = t;
		label = l;
		edges = new VSDEdge[0];
		isAccept = accept;
	}
	
	public VSDNode(String t, String l, VSDEdge[] e) {
		title = t;
		label = l;
		edges = e;
		isAccept = false;
	}
	
	public VSDNode(String t, String l, VSDEdge[] e, boolean accept) {
		title = t;
		label = l;
		edges = e;
		isAccept = accept;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getLabel() {
		return label;
	}
	
	public VSDEdge[] getEdges() {
		return edges;
	}
	
	public boolean isAccept() {
		return isAccept;
	}
	
	public void addEdge(VSDEdge e) {
		int oldlen = edges.length;
		edges = (VSDEdge[])Utils.resizeArray(edges, oldlen+1);
		edges[oldlen] = e;
	}
	
	public void addEdge(String l, String s, String t, int c, int tc) {
		VSDEdge newEdge = new VSDEdge(l, s, t, c, tc);
		int oldlen = edges.length;
		edges = (VSDEdge[])Utils.resizeArray(edges, oldlen+1);
		edges[oldlen] = newEdge;
	}
	
	public String getRule() {
		String temp = "";
		int n = label.indexOf("\\n");
		if(n < label.length() && n >= 0)
			temp =  label.substring(n+3);
		while(temp.startsWith(" ")) {
			temp = temp.substring(1);
		}
		return temp;
	}
}
