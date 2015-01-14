package com.xzit.security.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.xzit.security.domain.ContactInfo;

/**
 * 获取联系方式
 * 
 * @author Lenovo
 * 
 */
public class ContactInfoService {
	// 上下文
	private Context context;

	// 构造方法
	public ContactInfoService(Context context) {
		this.context = context;
	}

	// 以列表的形式获取所有的联系人
	public List<ContactInfo> getContactInfos() {

		List<ContactInfo> infos = new ArrayList<ContactInfo>();

		ContactInfo info;

		ContentResolver contentResolver = context.getContentResolver();

		// 系统的联系人的地址
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		// 数据的地址
		Uri dataUri = Uri.parse("content://com.android.contacts/data");
		// 查询
		Cursor cursor = contentResolver.query(uri, null, null, null, null);
		// 迭代查询出需要的信息
		while (cursor.moveToNext()) {
			info = new ContactInfo();
			// 获取ID
			String id = cursor.getString(cursor.getColumnIndex("_id"));
			// 获取名字
			String name = cursor.getString(cursor
					.getColumnIndex("display_name"));
			// 将姓名赋值
			info.setName(name);

			Cursor dataCursor = contentResolver.query(dataUri, null,
					"raw_contact_id = ? ", new String[] { id }, null);
			// 查询出电话号码
			while (dataCursor.moveToNext()) {
				// 获取数据的mimetype
				String type = dataCursor.getString(dataCursor
						.getColumnIndex("mimetype"));
				// 判断是否
				if (type.equals("vnd.android.cursor.item/phone_v2")) {
					String number = dataCursor.getString(dataCursor
							.getColumnIndex("data1"));
					info.setPhone(number);
				}
			}
			dataCursor.close();
			infos.add(info);
			info = null;
		}
		cursor.close();
		return infos;
	}

}
