package com.xzit.security.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;

/**
 * 异步的下载任务
 * 
 * @author lenovo
 * 
 */

public class DownloadTask {

	/**
	 * 静态方法，下载文件
	 * 
	 * @param path
	 *            下载地址
	 * @param filePath
	 *            存储地址
	 * @param progressDialog
	 *            进度条
	 * @return 下载完成的文件
	 * @throws Exception
	 */
	public static File getFile(String path, String filePath,
			ProgressDialog progressDialog) throws Exception {
		// 新建URL地址
		URL url = new URL(path);
		// 打开URL链接
		HttpURLConnection httpURLConnection = (HttpURLConnection) url
				.openConnection();
		httpURLConnection.setConnectTimeout(2000);
		// 设置访问方式
		httpURLConnection.setRequestMethod("GET");
		// 判断返回码
		if (httpURLConnection.getResponseCode() == 200) {
			// 获取内容的大小
			int total = httpURLConnection.getContentLength();
			// 设置进度条的最大值
			progressDialog.setMax(total);

			// 新建输入流
			InputStream is = httpURLConnection.getInputStream();
			// 根据存储地址新建文件
			File file = new File(filePath);
			// 打开输出流
			FileOutputStream fos = new FileOutputStream(file);

			byte[] buffer = new byte[1024];
			int len;
			int process = 0;
			while ((len = is.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
				process += len;
				progressDialog.setProgress(process);
			}
			fos.flush();
			fos.close();
			is.close();
			return file;
		}
		return null;
	}

}
