package bisonparser.editors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.eclipse.swt.graphics.RGB;

import bisonparser.Activator;

/**
 * Partially implemented class that has been replaced
 * by BisonColorTable
 * 
 * @deprecated
 * @author honda
 */
public class BisonOptions {
	public String XML_COMMENT = "__XML_COMMENT";
	public String PROC_INSTR = "__PROC_INSTR";
	public String STRING = "__STRING";
	public String DEFAULT = "__DEFAULT";
	public String TAG = "__TAG";
	public String C_CODE = "__C_CODE";
	public String C_DEFAULT = "__C_DEFAULT";
	public String C_COMMENT = "__C_COMMENT";
	public String C_DIRECTIVE = "__C_DIRECTIVE";
	public String C_STRING = "__C_STRING";
	public String C_KEYWORD = "__C_KEYWORD";
	public String C_TYPE = "__C_TYPE";
	public String C_CONSTANT = "__C_CONSTANT";
	public String B_KEYWORD = "__B_KEYWORD";
	public String B_GRAMMAR_RULE = "__B_GRAMMAR_RULE";
	public String B_DEFAULT = "__B_DEFAULT";
	public String B_COMMENT = "__B_COMMENT";
	
	public HashMap	colors;
	public File		optionsfile;
	
	public BisonOptions() {
		colors = new HashMap();
		optionsfile = new File(Activator.getDefault().getStateLocation().toOSString(),"BisonOptions.options");
		System.out.println(Activator.getDefault().getStateLocation().toOSString());
		
		colors.put(XML_COMMENT, new RGB(255, 0, 153));
		colors.put(PROC_INSTR, new RGB(153, 0, 153));
		colors.put(STRING, new RGB(102, 0, 0));
		colors.put(DEFAULT, new RGB(0, 0, 0));
		colors.put(TAG, new RGB(0, 0, 255));
		colors.put(C_CODE, new RGB(178,64,64));
		colors.put(C_DEFAULT, new RGB(128,128,128));
		colors.put(C_COMMENT, new RGB(255,0,153));
		colors.put(C_DIRECTIVE, new RGB(0,153,0));
		colors.put(C_STRING, new RGB(102,0,0));
		colors.put(C_KEYWORD, new RGB(0,153,153));
		colors.put(C_TYPE, new RGB(0,153,0));
		colors.put(C_CONSTANT, new RGB(153,153,0));
		colors.put(B_KEYWORD, new RGB(153,0,153));
		colors.put(B_GRAMMAR_RULE, new RGB(255,153,0));
		colors.put(B_DEFAULT, new RGB(0, 0, 0));
		colors.put(B_COMMENT, new RGB(255, 0, 153));
		
		if(!optionsfile.exists())
		{
			try {
				BufferedReader reader = new BufferedReader(new FileReader(optionsfile));
				while(true){
					try{
						String line = reader.readLine();
						String key = new String("");
						int i=0;
						while(i<line.length() && line.charAt(i)!=':'){
							key.concat(""+line.charAt(i));
							i++;
						}
						while(i<line.length() && (Character.isWhitespace(line.charAt(i)) || 
								line.charAt(i)==':' || line.charAt(i)=='#')){
							i++;
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
		else{
			try {
				optionsfile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
