package com.doug.component.support.mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.doug.component.cache.CacheBean;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class Mail {
	
	public boolean sendTextMail(final MailSenderInfo mailInfo) 
    {
        // 判断是否需要身份认证    
//		PopAuthenticator authenticator = null;    
        Properties pro = mailInfo.getProperties();   
        if (mailInfo.isValidate()) 
        {    
            // 如果需要身份认证，则创建一个密码验证器    
//            authenticator = new PopAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());    
        }   
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session    
        
        Session sendMailSession = Session.getDefaultInstance(pro,new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailInfo.getUserName(), mailInfo.getPassword());
            }
        });    
        try 
        {    
            // 根据session创建一个邮件消息    
            Message mailMessage = new MimeMessage(sendMailSession);    
            // 创建邮件发送者地址    
            Address from = new InternetAddress(mailInfo.getFromAddress());    
            // 设置邮件消息的发送者    
            mailMessage.setFrom(from);    
            // 创建邮件的接收者地址，并设置到邮件消息中    
            Address to = new InternetAddress(mailInfo.getToAddress());    
            mailMessage.setRecipient(Message.RecipientType.TO,to);    
            // 设置邮件消息的主题    
            mailMessage.setSubject(mailInfo.getSubject());    
            // 设置邮件消息发送的时间    
            mailMessage.setSentDate(new Date());    
            // 设置邮件消息的主要内容    
            String mailContent = mailInfo.getContent();    
            mailMessage.setText(mailContent);    
            // 发送邮件    
            Transport.send(mailMessage);  
            System.out.println("success");
            return true;    
        } 
        catch (MessagingException ex) 
        {    
            ex.printStackTrace();    
        }    
        return false;    
    }
	
	public static void sendMsg(String toAddress, String subject, String content,Context context) {
		  MailSenderInfo mailInfo = new MailSenderInfo();    
//	      mailInfo.setMailServerHost("smtp.163.com");    
//	      mailInfo.setMailServerPort("25");  
	      mailInfo.setMailServerHost("smtp.exmail.qq.com");    
	      mailInfo.setMailServerPort("465");
	      mailInfo.setValidate(true);    
	      mailInfo.setUserName("developer@xiaomanjf.com");  
	      mailInfo.setPassword("Developer123");//您的邮箱密码    安全码
	      mailInfo.setFromAddress("developer@xiaomanjf.com");  
	      mailInfo.setToAddress(toAddress);
//	      mailInfo.setFromAddress("wzc2542736@163.com");
	      mailInfo.setSubject(subject);
	      
	      ApplicationInfo appInfo = null;
		try {
			appInfo = context.getPackageManager()
						.getApplicationInfo(context.getPackageName(),
								PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			String channel = appInfo.metaData.getString("UMENG_CHANNEL", "")
					.toUpperCase();
	      mailInfo.setContent(CacheBean.getInstance().getApkInfo().toString() + "\nchannel：" + channel + "\n" +content);    
	         //这个类主要来发送邮件   
	      Mail sms = new Mail();   
	          sms.sendTextMail(mailInfo);//发送文体格式    
	}
}
