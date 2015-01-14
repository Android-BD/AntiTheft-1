package com.xzit.security.engine;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;

import com.xzit.security.domain.UpdateInfo;

/**
 * 工具类，负责获取更新的信息
 * 
 * @author lenovo
 * 
 */
public class UpdateInfoService {

	private Context context;

	public UpdateInfoService(Context context) {
		this.context = context;
	}

	/**
	 * 获得更新所需要的信息，在程序启动检查更新的时候需要使用 根据获得信息和已有的版本的信息比对，得出是否需要更新或者更新的一些信息
	 * 
	 * @param urlId
	 *            路径所对应的标签
	 * @return UpdateInfo对象
	 * @throws Exception
	 */
	public UpdateInfo getUpdateInfo(int urlId) throws Exception {
		String path = context.getResources().getString(urlId);
		// URL路径对象
		URL url = new URL(path);
		// 打开链接
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		// 设置链接属性
		httpURLConnection.setConnectTimeout(5000);
		httpURLConnection.setRequestMethod("GET");
		// 打开下载输入流
		InputStream is = httpURLConnection.getInputStream();
		// 根据输入流转换成UpdateInfo对象
		return UpdateInfoParser.getUpdateInfo(is);
	}

}
