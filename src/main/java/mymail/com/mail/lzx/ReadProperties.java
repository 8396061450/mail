package mymail.com.mail.lzx;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadProperties {
	
	public static Properties getProperty(String name) {
		Properties p=new Properties();
		InputStream in=ReadProperties.class.getClassLoader().getResourceAsStream(name);
		try {
			if(in==null) {
				String filePath = System.getProperty("user.dir") + File.separator+name; 
				in = new FileInputStream(name); 
			}
			p.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p;
	}

}
