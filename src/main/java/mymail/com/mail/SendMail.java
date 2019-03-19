package mymail.com.mail;

import java.io.ObjectInputStream.GetField;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import mymail.com.mail.lzx.FileUtils;
import mymail.com.mail.lzx.ReadProperties;
import mymail.com.mail.lzx.ReadXMl;
import mymail.com.mail.lzx.ToObject;

/**
 * Hello world!
 *
 */
public class SendMail {

	public static void sendMessage(ToObject to, Session session,Properties p) {
		try {
			// 创建默认的 MimeMessage 对象
			MimeMessage message = new MimeMessage(session);

			// Set From: 头部头字段
			message.setFrom(new InternetAddress(p.getProperty("username")));

			// Set To: 头部头字段
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to.getTo()));

			// Set Subject: 头部头字段
			message.setSubject(to.getSubject());

			// 设置消息体
			message.setText(to.getContext());
			// 发送消息
			Transport ts = session.getTransport();
			ts.connect(p.getProperty("host"), p.getProperty("username").trim(), p.getProperty("password").trim());
			ts.sendMessage(message,message.getAllRecipients());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void sendFileMessage(ToObject to, Session session,Properties p) {
		try {
			// 创建默认的 MimeMessage 对象
			MimeMessage message = new MimeMessage(session);

			// Set From: 头部头字段
			message.setFrom(new InternetAddress(p.getProperty("username")));

			// Set To: 头部头字段
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to.getTo()));

			// Set Subject: 头部头字段
			message.setSubject(to.getSubject());

			// 设置消息体
			// 创建消息部分
	         BodyPart messageBodyPart = new MimeBodyPart();
	 
	         // 消息
	         messageBodyPart.setText(to.getContext());
	        
	         // 创建多重消息
	         Multipart multipart = new MimeMultipart();
	 
	         // 设置文本消息部分
	         multipart.addBodyPart(messageBodyPart);
	 
	         // 附件部分
	         messageBodyPart = new MimeBodyPart();
	         List<String> files=to.getFileName();
	         for(String file:files) {
	        	 DataSource source = new FileDataSource(file);
	        	 messageBodyPart.setDataHandler(new DataHandler(source));
	        	 messageBodyPart.setFileName(MimeUtility.encodeWord(FileUtils.getFileName(file)));
	        	 multipart.addBodyPart(messageBodyPart);
	         }
	 
	         // 发送完整消息
	         message.setContent(multipart );

			// 发送消息
	         Transport ts = session.getTransport();
	         ts.connect(p.getProperty("host"), p.getProperty("username").trim(), p.getProperty("password").trim());
	         ts.sendMessage(message, message.getAllRecipients());
		}catch (MessagingException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args) {
		String ppath="mail.properties";
		if(args!=null && args.length==2) {
			ppath=args[0];
		}
		final Properties p=ReadProperties.getProperty(ppath);
		String tpath="to.xml";
		if(args!=null ) {
			if(args.length==2) {
				tpath=args[1];
			}else {
				tpath=args[0];
			}
		}
		List<ToObject> tos=ReadXMl.getToObject(tpath);
		if(tos!=null && tos.size()>0) {
			Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());  
	        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";  
			Properties properties=new Properties();
			properties.put("mail.smtp.ssl.enable", "true");
			properties.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
			 properties.setProperty("mail.smtp.socketFactory.fallback", "false");
			 properties.setProperty("mail.smtp.port", "465");
			 properties.setProperty("mail.smtp.socketFactory.port", "465");
			 properties.setProperty("mail.smtp.host", p.getProperty("host"));
			 properties.put("mail.smtp.auth", "true");
			 properties.setProperty("mail.user", p.getProperty("username"));
			 properties.setProperty("mail.password",p.getProperty("password"));
			 properties.setProperty("mail.transport.protocol", "smtp");
			 
			Session session = Session.getDefaultInstance(properties);
			session.setDebug(true);
			for(ToObject to:tos) {
				SendMail.sendFileMessage(to, session,p);
			}
		}
		
	}
}
