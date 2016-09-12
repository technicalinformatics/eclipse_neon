package com.bisonide.vsd.utils;

public class Utils {
	public static final int BOXHEIGHT = 30;
	public static final int ARROWLEN = 20;
	public static final int FONTSIZE = 10;
	public static final int BOXBUFFER = 10;
	public static final int FONTWIDTH = 8;
	public static final int YBUFFER = 10;
	public static final int XBUFFER = ARROWLEN;
	public static final int YSTART = 10;
	public static final int XSTART = 10;
	
	public static Object resizeArray (Object oldArray, int newSize) {
		   int oldSize = java.lang.reflect.Array.getLength(oldArray);
		   Class elementType = oldArray.getClass().getComponentType();
		   Object newArray = java.lang.reflect.Array.newInstance(
		         elementType,newSize);
		   int preserveLength = Math.min(oldSize,newSize);
		   if (preserveLength > 0)
		      System.arraycopy (oldArray,0,newArray,0,preserveLength);
		   return newArray; 
	}
}
