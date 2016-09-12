package com.bisonide.vsd.utils;

public class InternalVSD {
	private VSDNode[] nodes;
	
	public InternalVSD() {
		nodes = new VSDNode[0];
	}
	
	public VSDNode[] getNodes() {
		return nodes;
	}
	
	public VSDNode getNode(String title) {
		for(int x = 0; x < nodes.length; x++) {
			if(nodes[x].getTitle().equals(title))
				return nodes[x];
		}
		return null;
	}
	
	public void addNode(VSDNode n) {
		int oldlen = nodes.length;
		nodes = (VSDNode[])Utils.resizeArray(nodes, oldlen+1);
		nodes[oldlen] = n;
	}
	
	public void addNode(String t, String l) {
		VSDNode newNode = new VSDNode(t, l);
		int oldlen = nodes.length;
		nodes = (VSDNode[])Utils.resizeArray(nodes, oldlen+1);
		nodes[oldlen] = newNode;
	}
	
	public void addNode(String t, String l, boolean accept) {
		VSDNode newNode = new VSDNode(t, l, accept);
		int oldlen = nodes.length;
		nodes = (VSDNode[])Utils.resizeArray(nodes, oldlen+1);
		nodes[oldlen] = newNode;
	}
	
	public void addEdge(String nodeTitle, String l, String s, String t, int c, int tc) {
		for(int x = 0; x < nodes.length; x++) {
			if(nodes[x].getTitle().equals(nodeTitle))
				nodes[x].addEdge(l, s, t, c, tc);
		}
	}
	
	public void addEdge(String nodeTitle, VSDEdge e) {
		for(int x = 0; x < nodes.length; x++) {
			if(nodes[x].getTitle().equals(nodeTitle))
				nodes[x].addEdge(e);
		}
	}
}
