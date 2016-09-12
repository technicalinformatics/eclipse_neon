package com.bisonide.vsd.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class VCGParser {

private static VSDNode[] nodes = new VSDNode[0];
private static VSDEdge[] edges = new VSDEdge[0];

	public static boolean isNode(String s)
	{
		int i=0;
		while ((s!=null)&&(i < s.length()-4))
		{
			if (s.substring(i, i+5).equals("node:"))
			{
				return true;
			}
			else
				i++;
		}
		return false;
	}
	
	public static boolean isEdge(String s)
	{
		int i=0;
		while ((s!=null)&&(i < (s.length()-5)))
		{
			if (s.substring(i, i+5).equals("edge:"))
				return true;
			else
				i++;
		}
		return false;
	}
	
	public static boolean isAccept(String s)
	{
		int i=0;
		while (i < s.length()-6)
		{
			if (s.substring(i, i+7).equals("$accept"))
			{
				if (s.charAt(s.length()-1) == '.')
					return true;
				else return false;
			}
			else i++;
		}
		return false;
	}
	
	public static VSDEdge processEdge(BufferedReader reader)
	{
		String s = "";
		//get source name
		try
		{
			s = reader.readLine();
		}
		catch (IOException e)
		{
			System.out.println("IOexception = " + e);
		}
		int i=0, j=0;
		String source = "";
		while ((s.charAt(i) != '"' )&&(i<s.length()))
			i++;
		j = i+1;
		while (s.charAt(j) != '"')
			j++;
		source = s.substring(i+1,j);
		//get target name
		try
		{
			s = reader.readLine();
		}
		catch (IOException e)
		{
			System.out.println("IOexception = " + e);
		}
		i=0; 
		j=0;
		String target = "";
		while ((s.charAt(i) != '"' )&&(i<s.length()))
			i++;
		j = i+1;
		while (s.charAt(j) != '"')
			j++;
		target = s.substring(i+1,j);
		//get label name
		try
		{
			s = reader.readLine();
		}
		catch (IOException e2)
		{
			System.out.println("IOexception = " + e2);
		}
		i=0;
		j=0;
		String label = "";
		while ((s.charAt(i) != '"' )&&(i<s.length()))
			i++;
		j = i+1;
		while (s.charAt(j) != '"')
			j++;
		label = s.substring(i+1,j);
		try
		{
			reader.readLine();
			reader.readLine();
			s = reader.readLine();
		}
		catch (IOException e)
		{
			System.out.println("IOexception = " + e);
		}
		VSDEdge edge = new VSDEdge(label, source, target, 0, 0);
		return edge;
	}
	
	public static VSDNode processNode(BufferedReader reader)
	{
		String s = "";
		boolean accept = false;
		try
		{
			s = reader.readLine();
		}
		catch (IOException e)
		{
			System.out.println("IOexception = " + e);
		}
		String title = "", label = "";
		int i=0, j=0;
		while ((s!=null)&&(i < s.length()-5))
		{
			if (s.substring(i, i+6).equals("title:"))
			{
				i=i+7;
				while (s.charAt(i) != '"' )
					i++;
				j = i+1;
				while (s.charAt(j) != '"')
					j++;
				title = s.substring(i+1,j);
			}
			else
				i++;
		}
		try
		{
			s = reader.readLine();
		}
		catch (IOException e)
		{
			System.out.println("IOexception = " + e);
		}
		i=0;
		j=0;
		while ((s!=null)&&(i < s.length()-5))
		{
			if (s.substring(i, i+6).equals("label:"))
			{
				i=i+7;
				while (s.charAt(i) != '"' )
					i++;
				j = i+1;
				while (s.charAt(j) != '"')
					j++;
				label = s.substring(i+1,j);
			}
			else
				i++;
		}
		if (isAccept(label))
			accept = true;
		VSDNode node = new VSDNode(title, label, accept);
		try
		{
			reader.readLine();
		}
		catch (IOException e)
		{
			System.out.println("IOexception = " + e);
		}
		return node;
	}
	
	public static InternalVSD parse(String file)
	{
		InternalVSD vsd = new InternalVSD();
		try
		{
			String temp;
			int oldlen=0;
			FileReader file_reader = new FileReader (file);
			BufferedReader reader = new BufferedReader (file_reader);
			for (int i=0; i<6; i++)
				reader.readLine();
			while ((temp = reader.readLine()) != null)
			{
				while (isNode(temp))
				{
					oldlen = nodes.length;
					nodes = (VSDNode[])Utils.resizeArray(nodes, oldlen+1);
					nodes[oldlen] = new VSDNode();
					nodes[oldlen] = processNode(reader);
					temp="";
				}
			}
			FileReader file_reader2 = new FileReader (file);
			BufferedReader reader2 = new BufferedReader (file_reader2);
			while ((temp = reader2.readLine()) != null)
			{
				while (isEdge(temp))
				{
					oldlen = edges.length;
					edges = (VSDEdge[])Utils.resizeArray(edges, oldlen+1);
					edges[oldlen] = new VSDEdge();
					edges[oldlen] = processEdge(reader2);
					temp="";
				}
			}
			for (int i=0; i<nodes.length;i++)
			{
				vsd.addNode(nodes[i]);
			}
			for (int i=0; i<edges.length;i++)
			{
				vsd.addEdge(edges[i].getSource(), edges[i]);
			}
		}
		catch(IOException e)
		{
			System.out.println("IOexception = " + e);
		}
		return vsd;
	}
}
