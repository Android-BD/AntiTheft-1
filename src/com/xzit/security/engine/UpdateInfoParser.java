package com.xzit.security.engine;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.xzit.security.domain.UpdateInfo;


/**
 * 根据更新的地址将新版本下载下来而后进行封装转换成UpdateInfo对象返回
 * @author lenovo
 *
 */

public class UpdateInfoParser {

	public static UpdateInfo getUpdateInfo(InputStream is) throws Exception {
		//新建UpdateInfo对象
		UpdateInfo info = new UpdateInfo();
		//取得XML文件转换的转换器
		XmlPullParser xmlPullParser = Xml.newPullParser();
		//设置以utf_8的编码读取输入流的数据
		xmlPullParser.setInput(is, "utf-8");
		//转换器的事件类型
		int type = xmlPullParser.getEventType();
		//只要未遇到文档的结尾，一直读取
		while (type != XmlPullParser.END_DOCUMENT) {
			//选择事件的类型
			switch (type) {
			//文档开始
			case XmlPullParser.START_TAG:
				//判断标签的名字
				if (xmlPullParser.getName().equals("version")) {
					info.setVersion(xmlPullParser.nextText());
				} else if (xmlPullParser.getName().equals("description")) {
					info.setDescription(xmlPullParser.nextText());
				} else if (xmlPullParser.getName().equals("apkurl")) {
					info.setUrl(xmlPullParser.nextText());
				}
				break;

			default:
				break;
			}
			//继续
			type = xmlPullParser.next();
		}
		return info;
	}

}
