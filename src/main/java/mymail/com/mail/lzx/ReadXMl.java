package mymail.com.mail.lzx;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ReadXMl {

	public static List<ToObject>  getToObject(String filepath){
		SAXReader reader=new SAXReader();
		List<ToObject> list=new ArrayList<ToObject>();
		try {
			InputStream in=null;
			if((in=ReadXMl.class.getClassLoader().getResourceAsStream(filepath))==null) {
				String filePath = System.getProperty("user.dir") + File.separator+filepath; 
				in = new FileInputStream(filePath); 
			}
			Document document=reader.read(in);
			Element root=document.getRootElement();
			Iterator<Element> it=root.elementIterator("body");
			while(it.hasNext()){
				ToObject o=new ToObject();
				Element body=it.next();
				String from=body.element("from").getTextTrim();
				o.setFrom(from);
				String to=body.element("to").getTextTrim();
				o.setTo(to);
				String subject=body.element("subject").getTextTrim();
				o.setSubject(subject);
				String setContent=body.element("setContent").getTextTrim();
				o.setContext(setContent);
				Element files=body.element("files");
				Iterator<Element> it2=files.elementIterator("file");
				List<String> fs=new ArrayList<String>();
				while(it2.hasNext()) {
					String file=it2.next().getTextTrim();
					fs.add(file);
				}
				o.setFileName(fs);
				list.add(o);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			
		}
		return list;
	}
	
}
