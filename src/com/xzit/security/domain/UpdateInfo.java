package com.xzit.security.domain;

/**
 * UpdateInfo数据更新的格式
 * 
 * 
 * 在此处暂时用不到
 * @author lenovo
 *
 */


public class UpdateInfo {
	//版本信息
	private String version;
	//版本描述信息
	private String description;
	//服务器下载更新地址
	private String url;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
