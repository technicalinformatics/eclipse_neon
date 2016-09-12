package bisonparser.editors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import bisonparser.Activator;

/**
 * Provide colors for syntax highlighting.
 * Displays debugging messages to console. May need clean up.
 * @author honda
 */
public class BisonColorTable {
	
	public String XML_COMMENT = "XML_COMMENT";
	public String PROC_INSTR = "PROC_INSTR";
	public String STRING = "STRING";
	public String DEFAULT = "DEFAULT";
	public String TAG = "TAG";
	public String C_CODE = "C_CODE";
	public String C_DEFAULT = "C_DEFAULT";
	public String C_COMMENT = "C_COMMENT";
	public String C_DIRECTIVE = "C_DIRECTIVE";
	public String C_STRING = "C_STRING";
	public String C_KEYWORD = "C_KEYWORD";
	public String C_TYPE = "C_TYPE";
	public String C_CONSTANT = "C_CONSTANT";
	public String B_KEYWORD = "B_KEYWORD";
	public String B_GRAMMAR_RULE = "B_GRAMMAR_RULE";
	public String B_DEFAULT = "B_DEFAULT";
	public String B_COMMENT = "B_COMMENT";
	
	private HashMap	colors;
	public File		optionsfile;
	
	public BisonColorTable() {
		colors = new HashMap();
		optionsfile = new File(Activator.getDefault().getStateLocation().toOSString(),"BisonOptions.options");
		System.out.println(Activator.getDefault().getStateLocation().toOSString());
		
		colors.put(XML_COMMENT, new Color(Display.getCurrent(), new RGB(255, 0, 153)));
		colors.put(PROC_INSTR, new Color(Display.getCurrent(), new RGB(153, 0, 153)));
		colors.put(STRING, new Color(Display.getCurrent(), new RGB(102, 0, 0)));
		colors.put(DEFAULT, new Color(Display.getCurrent(), new RGB(0, 0, 0)));
		colors.put(TAG, new Color(Display.getCurrent(), new RGB(0, 0, 255)));
		colors.put(C_CODE, new Color(Display.getCurrent(), new RGB(178,64,64)));
		colors.put(C_DEFAULT, new Color(Display.getCurrent(), new RGB(128,128,128)));
		colors.put(C_COMMENT, new Color(Display.getCurrent(), new RGB(255,0,153)));
		colors.put(C_DIRECTIVE, new Color(Display.getCurrent(), new RGB(0,153,0)));
		colors.put(C_STRING, new Color(Display.getCurrent(), new RGB(102,0,0)));
		colors.put(C_KEYWORD, new Color(Display.getCurrent(), new RGB(0,153,153)));
		colors.put(C_TYPE, new Color(Display.getCurrent(), new RGB(0,153,0)));
		colors.put(C_CONSTANT, new Color(Display.getCurrent(), new RGB(153,153,0)));
		colors.put(B_KEYWORD, new Color(Display.getCurrent(), new RGB(153,0,153)));
		colors.put(B_GRAMMAR_RULE, new Color(Display.getCurrent(), new RGB(255,153,0)));
		colors.put(B_DEFAULT, new Color(Display.getCurrent(), new RGB(0, 0, 0)));
		colors.put(B_COMMENT, new Color(Display.getCurrent(), new RGB(255, 0, 153)));

		/* if file does not exist attempt to create it */
		if(!optionsfile.exists()){
			System.out.println("Options file does not exist. Attempting to create file.");
			try {
				optionsfile.createNewFile();
			}
			catch (IOException e) {
				System.out.println("Options file could not be created.");
				e.printStackTrace();
			}
		}
		else{
			System.out.println("Options file exist.");
		}
		
		/* fill map with info from file */
		System.out.println("Attempting to read from options file.");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(optionsfile));
			while(true){
				try{
					String line = reader.readLine();
					String key = new String("");
					if(line != null){
						//System.out.println("\t" + line);
						int i=0;
						/* key is everything that comes before ':' */
						while(i<line.length() && line.charAt(i)!=':'){
							key += line.charAt(i);
							i++;
						}
						/* region of whitespace : and # is skipped */
						while(i<line.length() && (Character.isWhitespace(line.charAt(i)) || 
								line.charAt(i)==':' || line.charAt(i)=='#')){
							i++;
						}
						/* red field is first set of all digits */
						String rstr =  new String("0");
						while(i<line.length() && (Character.isDigit(line.charAt(i)))){
							rstr += line.charAt(i);
							i++;
						}
						/* skip until number */
						while(i<line.length() && (!Character.isDigit(line.charAt(i)))){
							i++;
						}
						/* green field is second set of all digits */
						String gstr =  new String("0");
						while(i<line.length() && (Character.isDigit(line.charAt(i)))){
							gstr +=line.charAt(i);
							i++;
						}
						/* skip until number */
						while(i<line.length() && (!Character.isDigit(line.charAt(i)))){
							i++;
						}
						/* blue field is last set of all digits */
						String bstr =  new String("0");
						while(i<line.length() && (Character.isDigit(line.charAt(i)))){
							bstr += line.charAt(i);
							i++;
						}
						/* convert all values from string to int */
						int rval = Integer.parseInt(rstr, 10);
						int gval = Integer.parseInt(gstr, 10);
						int bval = Integer.parseInt(bstr, 10);
						/* ensure that all values are valid (0-255) */
						if(rval > 255)
							rval = 255;
						if(gval > 255)
							gval = 255;
						if(bval > 255)
							bval = 255;
						
						/* add entry to map */
						System.out.println("\t+" +
								key + " " +
								Integer.toString(rval) + "," +
								Integer.toString(gval) + "," +
								Integer.toString(bval));
						colors.put(key,
							new Color(Display.getCurrent(), new RGB(rval, gval, bval)));
					}
					else{
						break;
					}
				}
				catch (FileNotFoundException e) {
					break;
				}
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Color get(String key) {
		Object retval;
		retval = colors.get(key);
				// default return black;
		if(retval==null){
			return new Color(Display.getCurrent(), new RGB(0,0,0));
		}
		System.out.println(retval.getClass().getName());
		return (Color)retval;
	}
	
}
