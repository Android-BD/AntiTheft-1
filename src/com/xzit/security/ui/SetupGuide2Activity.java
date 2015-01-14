package com.xzit.security.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.xzit.security.R;

/**
 * 设置向导第二个界面
 * 
 * @author lenovo
 * 
 */
public class SetupGuide2Activity extends Activity implements OnClickListener {
	private Button bt_bind;
	private Button bt_next;
	private Button bt_pervious;
	private CheckBox cb_bind;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup_guide2);

		// 读取配置文件
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);

		// 实例化控件
		bt_bind = (Button) findViewById(R.id.bt_guide_bind);
		bt_next = (Button) findViewById(R.id.bt_guide_next);
		bt_pervious = (Button) findViewById(R.id.bt_guide_pervious);
		// 设置监听器
		bt_bind.setOnClickListener(this);
		bt_next.setOnClickListener(this);
		bt_pervious.setOnClickListener(this);

		cb_bind = (CheckBox) findViewById(R.id.cb_guide_check);
		// 初始化CheckBox状态
		String sim = sp.getString("simSerial", null);
		// 判断是否绑定了手机卡
		if (sim != null) {
			cb_bind.setText("已经绑定");
			cb_bind.setChecked(true);
		} else {
			cb_bind.setText("没有绑定");
			cb_bind.setChecked(false);
			resetSimInfo();
		}
		// 为绑定按钮设置监听器
		cb_bind.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					cb_bind.setText("已经绑定");
					setSimInfo();
				} else {
					cb_bind.setText("没有绑定");
					resetSimInfo();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_guide_bind:
			setSimInfo();
			cb_bind.setText("已经绑定");
			cb_bind.setChecked(true);
			break;

		case R.id.bt_guide_next:
			Intent intent = new Intent(this, SetupGuide3Activity.class);
			finish();
			startActivity(intent);
			// 这个是定义activity切换时的动画效果的
			overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
			break;
		case R.id.bt_guide_pervious:

			Intent i = new Intent(this, SetupGuide1Activity.class);
			finish();
			startActivity(i);
			// 这个是定义activity切换时的动画效果的
			overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
			break;

		default:
			break;
		}
	}

	//绑定手机卡信息
	private void setSimInfo() {
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		// 拿到sim卡的序列号，是唯一的
		String simSerial = telephonyManager.getSimSerialNumber();
		Editor editor = sp.edit();
		editor.putString("simSerial", simSerial);
		editor.commit();
	}

	// 解除绑定
	private void resetSimInfo() {
		Editor editor = sp.edit();
		editor.putString("simSerial", null);
		editor.commit();
	}

}
