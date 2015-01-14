package com.xzit.security.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.xzit.security.R;
import com.xzit.security.adapter.MainUIAdapter;

/**
 * 主功能界面，用以显示功能界面
 * 
 * @author lenovo
 * 
 */
public class MainActivity extends Activity implements OnItemClickListener {
	// 网格
	private GridView gridView;
	// 网格的适配器
	private MainUIAdapter adapter;
	// SharedPreferences用于存储配置文件
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// 初始化控件
		sp = this.getSharedPreferences("config", Context.MODE_PRIVATE);
		gridView = (GridView) findViewById(R.id.gv_main);
		// 新建适配器
		adapter = new MainUIAdapter(this);
		// 设置适配器
		gridView.setAdapter(adapter);

		// 设置监听器
		gridView.setOnItemClickListener(this);

		// 设置网格的长按监听器
		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
				if (position == 0) {
					// 新建AlertDialog的builder
					AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
					// 设置标题
					builder.setTitle("设置");
					// 设置对话框的内容
					builder.setMessage("请输入要理性的名称");
					// 新建编辑文本
					final EditText et = new EditText(MainActivity.this);
					// 设置文本的高亮显示的信息
					et.setHint("新名称");
					// 将编辑文本框添加到对话框当中
					builder.setView(et);

					// 设置确认按钮和取消按钮
					builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

							String name = et.getText().toString();
							if (name.equals("")) {
								Toast.makeText(MainActivity.this, "输入内容不能为空", Toast.LENGTH_SHORT).show();
							} else {
								// 打开sharedprsfense的编辑器
								Editor editor = sp.edit();
								// 输入信息
								editor.putString("lostName", name);
								// 保存
								editor.commit();

								TextView tv = (TextView) view.findViewById(R.id.tv_main_name);
								// 设置textview的内容
								tv.setText(name);
								// 通知刷新信息
								adapter.notifyDataSetChanged();
							}
						}
					});
					builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					});
					builder.create().show();
				}
				return false;
			}
		});
	}

	/**
	 * 表格点击响应事件
	 */

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (position) {
		case 0: // 手机防盗
			// 跳转
			Intent intent = new Intent(MainActivity.this, LostProtectedActivity.class);
			startActivity(intent);
			break;

		case 1: // 关于

			// 跳转
			Intent intent2 = new Intent(MainActivity.this, AboutActivity.class);
			startActivity(intent2);

			break;

		default:
			break;
		}
	}

}
