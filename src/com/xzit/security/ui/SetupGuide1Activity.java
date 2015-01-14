package com.xzit.security.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.xzit.security.R;

/**
 * 向导界面1
 * 
 * @author Lenovo
 * 
 */
public class SetupGuide1Activity extends Activity implements OnClickListener {
	//下一步按钮
	private Button next;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup_guide1);

		//实例化控件
		next = (Button) findViewById(R.id.bt_guide_next);
		//设置监听器
		next.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_guide_next:
			//跳转
			Intent intent = new Intent(this, SetupGuide2Activity.class);
			finish();
			startActivity(intent);
			// 这个是定义activity切换时的动画效果的
			overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
			break;

		default:
			break;
		}
	}

}
