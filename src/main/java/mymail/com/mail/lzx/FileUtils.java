package mymail.com.mail.lzx;

import java.io.File;

public class FileUtils {

	public static String getFileName(String name) {
		if(name!=null && name.length()>0 ) {
			int start=0;
			if((start=name.lastIndexOf(File.separator))!=-1) {
				return name.substring(start+1, name.length());
			}
		}
		return "null";
	}
	
}
