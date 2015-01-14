package com.xzit.security.domain;


/**
 * 实体类ContactInfo
 * 联系人信息
 * 这里需要的信息只有姓名和号码
 * 所有这里只需要两个属性name和phone
 * @author lenovo
 *
 */
public class ContactInfo {
	private String name;
	private String phone;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
