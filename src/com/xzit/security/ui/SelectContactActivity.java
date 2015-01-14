package com.xzit.security.ui;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xzit.security.R;
import com.xzit.security.domain.ContactInfo;
import com.xzit.security.engine.ContactInfoService;

/**
 * 选择联系人界面
 * 
 * @author lenovo
 * 
 */

public class SelectContactActivity extends Activity {
	//列表对象
	private ListView lv;
	//联系人数据
	private List<ContactInfo> infos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置布局
		setContentView(R.layout.select_contact);

		//获得联系人的数据
		infos = new ContactInfoService(this).getContactInfos();

		//实例化列表
		lv = (ListView) findViewById(R.id.lv_select_contact);
		//设置适配器
		lv.setAdapter(new SelectContactAdapter());
		//设置监听器
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//根据选中的编号，获得手机号
				String number = infos.get(position).getPhone();
				Intent intent = new Intent();
				intent.putExtra("number", number);
				setResult(1, intent);
				finish();
			}
		});
	}

	// =================================================================================

	private class SelectContactAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return infos.size();
		}

		@Override
		public Object getItem(int position) {
			return infos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ContactInfo info = infos.get(position);
			View view;
			ContactViews views;
			if (convertView == null) {
				//新建联系人view
				views = new ContactViews();
				//扩展布局文件
				view = View.inflate(SelectContactActivity.this,
						R.layout.contact_item, null);
				//实例化控件
				views.tv_name = (TextView) view
						.findViewById(R.id.tv_contact_name);
				views.tv_number = (TextView) view
						.findViewById(R.id.tv_contact_number);
				views.tv_name.setText("联系人：" + info.getName());
				views.tv_number.setText("联系电话：" + info.getPhone());

				view.setTag(views);
			} else {
				view = convertView;
			}
			return view;
		}

	}

	private class ContactViews {
		TextView tv_name;
		TextView tv_number;
	}

}
