package com.zp.test20181105;

public class getRecipients {

	public static String[] getRecipients1(String users) {
		try {
			StringBuilder builder = new StringBuilder();
			// 读入联系人
			builder.append(users);
			builder.append(",");
			// 将联系人分割为数组返回
			return builder.toString().split(",");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * public static void main(String[] args) { getRecipients testa = new
	 * getRecipients(); String[] reci = testa.getRecipients1(); for (String a :
	 * reci) { System.out.println(a); } }
	 */
}