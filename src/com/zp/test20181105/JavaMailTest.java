package com.zp.test20181105;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMailTest {

	public static String SMTPSERVER;
	public static String SMTPPORT;
	public static String ACCOUT;
	public static String PWD;
	public static String USERS;
	public static String SUBJECT;
	public static String TEXT;

	private static Properties properties = new Properties();
	static {
		try {
			InputStream in = new BufferedInputStream(new FileInputStream("resources\\Recipients.properties"));
			InputStreamReader reader = new InputStreamReader(in, "utf-8");
			properties.load(reader);
			Iterator<String> it = properties.stringPropertyNames().iterator();
			while (it.hasNext()) {
				String key = it.next();
				switch (key) {
				case "smtp.server":
					SMTPSERVER = properties.getProperty(key);
					break;
				case "smtp.port":
					SMTPPORT = properties.getProperty(key);
					break;
				case "smtp.account":
					ACCOUT = properties.getProperty(key);
					break;
				case "smtp.pwd":
					PWD = properties.getProperty(key);
					break;
				case "email.users":
					USERS = properties.getProperty(key);
					break;
				case "email.subject":
					SUBJECT = properties.getProperty(key);
					break;
				case "email.text":
					TEXT = properties.getProperty(key);
					break;
				default:
					break;
				}
			}
			in.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static void sendEmail(String content) {
		try {
			// 创建邮件配置
			Properties props = new Properties();
			props.setProperty("mail.transport.protocol", "smtp"); // 使用的协议（JavaMail规范要求）
			props.setProperty("mail.smtp.host", SMTPSERVER); // 发件人的邮箱的SMTP服务器地址
			props.setProperty("mail.smtp.port", SMTPPORT); // 端口
			props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.setProperty("mail.smtp.auth", "true"); // 需要请求认证
			props.setProperty("mail.smtp.ssl.enable", "true");// 开启ssl
			// 根据邮件配置创建会话，注意session别导错包
			Session session = Session.getDefaultInstance(props);
			// 开启debug模式，可以看到更多详细的输入日志
			session.setDebug(true);
			// 获取传输通道
			Transport transport = session.getTransport();
			transport.connect(ACCOUT, PWD);
			// 创建邮件
			MimeMessage message = createEmail(session, USERS, content); // 将用户和内容传递过来
			// 连接，并发送邮件
			transport.sendMessage(message, message.getAllRecipients());

			transport.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static MimeMessage createEmail(Session session, String users, String content) throws Exception {
		// 根据会话创建邮件
		MimeMessage msg = new MimeMessage(session);
		// address邮件地址, personal邮件昵称, charset编码方式
		InternetAddress fromAddress = new InternetAddress(ACCOUT, "测试推送");
		// 设置发送邮件方
		msg.setFrom(fromAddress);
		// 单个可以直接这样创建
		// InternetAddress receiveAddress = new InternetAddress();
		// 设置邮件接收方
		Address[] internetAddressTo = new InternetAddress().parse(users);
		// type:
		// 要被设置为 TO, CC 或者 BCC，这里 CC 代表抄送、BCC 代表秘密抄送。举例：Message.RecipientType.TO

		msg.setRecipients(MimeMessage.RecipientType.TO, internetAddressTo);
		// 设置邮件标题
		msg.setSubject(SUBJECT);
		msg.setText(content);
		// 设置显示的发件时间
		msg.setSentDate(new Date());
		// 保存设置
		msg.saveChanges();
		return msg;
	}

	public static void main(String[] args) {
		JavaMailTest a = new JavaMailTest();
		a.sendEmail(TEXT);
	}
}
