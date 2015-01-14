package com.xzit.security.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xzit.security.R;


/**
 * UI界面表格显示数据适配器
 * 
 * @author lenovo
 * 
 */

public class MainUIAdapter extends BaseAdapter {

	// 显示的名称
	private static final String[] NAMES = new String[] { "手机防盗", "关于软件" };

	// 对应的名称的名字
	private static final int[] ICONS = new int[] { R.drawable.safer,
			R.drawable.about };

	// 图片显示
	private static ImageView imageView;
	// 对应图片的文本显示
	private static TextView textView;

	private Context context;
	// 布局扩充器
	private LayoutInflater inflater;
	// XML文件存储
	private SharedPreferences sp;

	public MainUIAdapter(Context context) {
		this.context = context;

		// 实例化布局扩充器
		inflater = LayoutInflater.from(this.context);
		// 实例化SharedPreferences对象
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
	}

	// 返回整个表格的个数
	@Override
	public int getCount() {
		return NAMES.length;
	}

	// 返回当前点击的位置
	@Override
	public Object getItem(int position) {
		return position;
	}

	// 返回当前的位置的项目的ID
	@Override
	public long getItemId(int position) {
		return position;
	}

	// 返回视图
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// 实例化控件
		View view = inflater.inflate(R.layout.main_item, null);

		imageView = (ImageView) view.findViewById(R.id.iv_main_icon);

		textView = (TextView) view.findViewById(R.id.tv_main_name);

		imageView.setImageResource(ICONS[position]);

		textView.setText(NAMES[position]);

		if (position == 0) {
			String name = sp.getString("lostName", "");
			// 判断是否有值
			if (!name.equals("")) {
				textView.setText(name);
			}
		}

		return view;
	}

}
