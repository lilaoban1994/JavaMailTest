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
			// �����ʼ�����
			Properties props = new Properties();
			props.setProperty("mail.transport.protocol", "smtp"); // ʹ�õ�Э�飨JavaMail�淶Ҫ��
			props.setProperty("mail.smtp.host", SMTPSERVER); // �����˵������
																// SMTP��������ַ
			props.setProperty("mail.smtp.port", SMTPPORT); // �˿�
			props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.setProperty("mail.smtp.auth", "true"); // ��Ҫ������֤
			props.setProperty("mail.smtp.ssl.enable", "true");// ����ssl
			// �����ʼ����ô����Ự��ע��session�𵼴��
			Session session = Session.getDefaultInstance(props);
			// ����debugģʽ�����Կ���������ϸ��������־
			session.setDebug(true);
			// ��ȡ����ͨ��
			Transport transport = session.getTransport();
			transport.connect(ACCOUT, PWD);
			// �����ʼ�
			MimeMessage message = createEmail(session, USERS, content); // ���û������ݴ��ݹ���
			// ���ӣ��������ʼ�
			transport.sendMessage(message, message.getAllRecipients());

			transport.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static MimeMessage createEmail(Session session, String users, String content) throws Exception {
		// ���ݻỰ�����ʼ�
		MimeMessage msg = new MimeMessage(session);
		// address�ʼ���ַ, personal�ʼ��ǳ�, charset���뷽ʽ
		InternetAddress fromAddress = new InternetAddress(ACCOUT, "��������");
		// ���÷����ʼ���
		msg.setFrom(fromAddress);
		// ��������ֱ����������
		// InternetAddress receiveAddress = new InternetAddress();
		// �����ʼ����շ�
		Address[] internetAddressTo = new InternetAddress().parse(users);
		// type:
		// Ҫ������Ϊ TO, CC ���� BCC������ CC �����͡�BCC �������ܳ��͡�������Message.RecipientType.TO

		msg.setRecipients(MimeMessage.RecipientType.TO, internetAddressTo);
		// �����ʼ�����
		msg.setSubject(SUBJECT);
		msg.setText(content);
		// ������ʾ�ķ���ʱ��
		msg.setSentDate(new Date());
		// ��������
		msg.saveChanges();
		return msg;
	}

	public static void main(String[] args) {
		JavaMailTest a = new JavaMailTest();
		a.sendEmail(TEXT);
	}
}