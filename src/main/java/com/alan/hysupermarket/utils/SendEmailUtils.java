package com.alan.hysupermarket.utils;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmailUtils {

	public static final String HOST = "smtp.163.com";
	public static final String PROTOCOL = "smtp";
	public static final int PORT = 25;
	public static final String FROM = "hy_supermarket@163.com";// 发件人的email
	public static final String PWD = "hy123456789";// 发件人密码 这里是发件人设置的smtp 服务的授权码
													// 也就是授权密码 并不是邮箱的登录密码！

	/**
	 * 获取session
	 * 
	 * @return
	 */
	private static Session getSession() {

		Properties props = new Properties();
		props.put("mail.smtp.host", HOST);// 设置服务器地址
		props.put("mail.store.protocol", PROTOCOL);// 设置协议
		props.put("mail.smtp.port", PORT);// 设置端口
		props.put("mail.smtp.auth", true);

		Authenticator authenticator = new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(FROM, PWD);
			}

		};
		Session session = Session.getDefaultInstance(props, authenticator);
		return session;
	}

	/**
	 * 发送邮件
	 * 
	 * @param toEmail
	 * @param content
	 */
	public static void send(String toEmail, String content) {
		Session session = getSession();
		try {
			// 添加信息
			Message msg = new MimeMessage(session);
			// 设置信息
			msg.setFrom(new InternetAddress(FROM));
			InternetAddress[] address = { new InternetAddress(toEmail) };
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject("鸿雁账号找回密码邮件");
			msg.setSentDate(new Date());
			msg.setContent(content, "text/html;charset=utf-8");
			// 发送信息
			Transport.send(msg);
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}

}
