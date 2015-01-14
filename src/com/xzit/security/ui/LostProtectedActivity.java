package com.xzit.security.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.xzit.security.R;
import com.xzit.security.utils.MD5Encoder;

/**
 * 安全功能界面
 * 
 * 本窗体没有布局文件，显示的时候是通过代码动态的生成一个对话框
 * 对话框的布局在layout之中
 * @author Lenovo
 * 
 */

public class LostProtectedActivity extends Activity implements OnClickListener {

	//SharedPreferences对象
	private SharedPreferences sp;
	//对话框
	private Dialog dialog;
	//文本输入框，分别输入密码和确认密码
	private EditText password;
	private EditText confirmPassword;
	//安全号码
	private TextView tv_protectedNumber;
	//重新进入设置向导
	private TextView tv_protectedGuide;
	//是否开启保护功能选择
	private CheckBox cb_isProtected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//读取软件的设置信息
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);

		//判断是否有密码
		if (isSetPassword()) {
			//显示登录对话框
			showLoginDialog();
		} else {
			//直接显示第一次需要配置的对话框
			showFirstDialog();
		}
	}

	//登录对话框函数
	private void showLoginDialog() {
		
		
		//新建对话框，设置为自定义的风格
		dialog = new Dialog(this, R.style.MyDialog);
		//用已有的布局新建一个新的view
		View view = View.inflate(this, R.layout.login_dialog, null);
		//初始化控件
		password = (EditText) view.findViewById(R.id.et_protected_password);
		Button yes = (Button) view.findViewById(R.id.bt_protected_login_yes);
		Button cancel = (Button) view.findViewById(R.id.bt_protected_login_no);
		
		
		//设置监听器
		yes.setOnClickListener(this);
		cancel.setOnClickListener(this);
		//为对话框设置布局
		dialog.setContentView(view);
		//设置对话框不能取消
		dialog.setCancelable(false);
		//显示对话框
		dialog.show();
		

		
	}

	//第一次进入软件时需要设置密码
	private void showFirstDialog() {
		
		//实例化对话框，并设置显示的布局
		dialog = new Dialog(this, R.style.MyDialog);
		
		// dialog.setContentView(R.layout.first_dialog);
		
		// 这种填充布局的方式比较方便，只需要拿到一个LayoutInflate对象，方式更加灵活
		View view = View.inflate(this, R.layout.first_dialog, null);
		
		//实例化控件
		password = (EditText) view.findViewById(R.id.et_protected_first_password);
		confirmPassword = (EditText) view.findViewById(R.id.et_protected_confirm_password);
		Button yes = (Button) view.findViewById(R.id.bt_protected_first_yes);
		Button cancel = (Button) view.findViewById(R.id.bt_protected_first_no);
		//为按钮设置监听器
		yes.setOnClickListener(this);
		cancel.setOnClickListener(this);
		dialog.setContentView(view);
		dialog.setCancelable(false);
		dialog.show();
	}

	//判断是否已经设置了密码函数
	private boolean isSetPassword() {
		String pwd = sp.getString("password", "");
		if (pwd.equals("") || pwd == null) {
			return false;
		}
		return true;
	}

	//判断是否开始设置导向
	private boolean isSetupGuide() {
		return sp.getBoolean("setupGuide", false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//判断点击的按钮
		case R.id.bt_protected_first_yes:
			//获得输入的两次密码
			String fp = password.getText().toString().trim();
			String cp = confirmPassword.getText().toString().trim();
			//如若为为空
			if (fp.equals("") || cp.equals("")) {
				Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
				return;
			} else {
				//如若两次输入的一致
				if (fp.equals(cp)) {
					//打开文件的输入编辑器
					Editor editor = sp.edit();
					// 取出的密码经过MD5加密
					editor.putString("password", MD5Encoder.encode(fp));
					//提交内容保存
					editor.commit();
					//对话框消失
					dialog.dismiss();

					//如若为进行安全导向
					if (!isSetupGuide()) {
						finish();
						//结束当前的开始进行安全导向界面
						Intent intent = new Intent(this,
								SetupGuide1Activity.class);
						startActivity(intent);
					}
				} else {
					Toast.makeText(this, "两次密码不相同", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			dialog.dismiss();
			break;

			//假如不进行安全密码设置
		case R.id.bt_protected_first_no:
			dialog.dismiss();
			finish();
			break;

			//点击的是登录按钮
		case R.id.bt_protected_login_yes:
			//获得输入的密码
			String pwd = password.getText().toString().toString();
			if (pwd.equals("")) {
				Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
			} else {
				//获得保存的密码
				String str = sp.getString("password", "");
				//判断密码是否相同，反解码进过加密的密码
				if (MD5Encoder.encode(pwd).equals(str)) {
					//判断是否在进行安全向导
					if (isSetupGuide()) {
						setContentView(R.layout.lost_protected);
						//实例化控件
						tv_protectedNumber = (TextView) findViewById(R.id.tv_lost_protected_number);
						tv_protectedGuide = (TextView) findViewById(R.id.tv_lost_protected_guide);
						cb_isProtected = (CheckBox) findViewById(R.id.cb_lost_protected_isProtected);

						tv_protectedNumber.setText("手机安全号码为："+ sp.getString("number", ""));
						tv_protectedGuide.setOnClickListener(this);

						//获取是否打开了安全保护
						boolean isProtecting = sp.getBoolean("isProtected",false);
						if (isProtecting) {
							cb_isProtected.setText("已经开启保护");
							cb_isProtected.setChecked(true);
						}

						//为开始安全保护的选择框设置监听器
						cb_isProtected.setOnCheckedChangeListener(new OnCheckedChangeListener() {
									@Override
									public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
										//根据选择框的状态更新文本提示框
										if (isChecked) {
											cb_isProtected.setText("已经开启保护");
											Editor editor = sp.edit();
											editor.putBoolean("isProtected",
													true);
											editor.commit();
										} else {
											cb_isProtected.setText("没有开启保护");
											Editor editor = sp.edit();
											editor.putBoolean("isProtected",
													false);
											editor.commit();
										}
									}
								});
					}
					dialog.dismiss();
				} else {
					Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
				}
			}
			break;

		case R.id.bt_protected_login_no:
			dialog.dismiss();
			finish();
			break;

		case R.id.tv_lost_protected_guide: // 重新进入设置向导
			finish();
			Intent setupGuideIntent = new Intent(this,
					SetupGuide1Activity.class);
			startActivity(setupGuideIntent);
			break;

		default:
			break;
		}
	}

}
