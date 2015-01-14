package com.xzit.security.ui;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.xzit.security.R;
import com.xzit.security.domain.UpdateInfo;
import com.xzit.security.engine.DownloadTask;
import com.xzit.security.engine.UpdateInfoService;



/**
 * 程序启动画面，欢迎界面和检查更新界面
 * @author lenovo
 *
 */
public class SplashActivity extends Activity {
	//版本信息显示文本
	private TextView tv_version;
	//线性布局器
	private LinearLayout ll;
	//进度条对话框
	private ProgressDialog progressDialog;
	//版本更新信息对象
	private UpdateInfo info;
	//版本信息字符串
	private String version;

	private static final String TAG = "Security";

	// 新建一个handler处理类，用以处理
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (isNeedUpdate(version)) {
				
				System.out.println(version);
				
				
				showUpdateDialog();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 全屏显示
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置布局
		setContentView(R.layout.splash);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// 实例化控件
		tv_version = (TextView) findViewById(R.id.tv_splash_version);
		//获取当前的版本号
		version = getVersion();

		tv_version.setText("版本号  " + version);

		//获取整个页面的布局
		ll = (LinearLayout) findViewById(R.id.ll_splash_main);
		//新建动画效果
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
		//设置动画的时间
		alphaAnimation.setDuration(2000);
		//启动动画
		ll.startAnimation(alphaAnimation);

		//新建进度条对话框，默认不显示
		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setMessage("正在下载...");


		//线程休息三秒钟发送handler消息，启动handler处理类
		new Thread() {
			public void run() {
				try {
					sleep(3000);
					handler.sendEmptyMessage(0);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();

	}

	//当需要进行更新的时候，调用该方法显示对话框
	private void showUpdateDialog() {
		//新建警告对话框
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle("升级提醒");
		builder.setMessage(info.getDescription());
		builder.setCancelable(false);

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				//根据内存卡的情况，存储安装包
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					File dir = new File(Environment.getExternalStorageDirectory(), "/security/update");
					if (!dir.exists()) {
						dir.mkdirs();
					}
					//存储的路径
					String apkPath = Environment.getExternalStorageDirectory()+ "/security/update/new.apk";
					UpdateTask task = new UpdateTask(info.getUrl(), apkPath);
					progressDialog.show();
					new Thread(task).start();
				} else {
					//内存卡不可用
					Toast.makeText(SplashActivity.this, "SD卡不可用，请插入SD卡",
							Toast.LENGTH_SHORT).show();
					loadMainUI();
				}
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				loadMainUI();
			}

		});
		builder.create().show();
	}

	/**
	 * 根据当前的版本号判断当前是否需要更新
	 * @param version 获得的当前的版本号
	 * @return
	 */
	private boolean isNeedUpdate(String version) {
		//新建一个版本更新工具对象
		UpdateInfoService updateInfoService = new UpdateInfoService(this);
		try {
			//从服务器获得新版本的信息
			info = updateInfoService.getUpdateInfo(R.string.serverUrl);
			//获得新版本的版本号
			String v = info.getVersion();
			//当前的版本号和新的版本号进行比对
			if (v.equals(version)) {
				Log.i(TAG, "当前版本：" + version);
				Log.i(TAG, "最新版本：" + v);
				//不需要更新，直接跳转到主界面
				loadMainUI();
				return false;
			} else {
				Log.i(TAG, "需要更新");
				return true;
			}
		} catch (Exception e) {
			//未联网或者服务器断开的时候进行异常曝出
			e.printStackTrace();
			Toast.makeText(this, "获取更新信息异常，请稍后再试", Toast.LENGTH_SHORT).show();
			//继续跳转到主界面
			loadMainUI();
		}
		return false;
	}

	/**
	 * 获取版本号码
	 * 
	 * @return 版本号
	 */
	private String getVersion() {
		try {
			//获取包的管理器
			PackageManager packageManager = getPackageManager();
			//获取包的信息
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			//获取版本信息
			return packageInfo.versionName;
			
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "版本号未知";
		}
	}

	// 加载完UI界面之后转向设置向导
	private void loadMainUI() {
		//跳转
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		//结束当前的界面
		finish();
	}

	//安装程序
	private void install(File file) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
		finish();
		startActivity(intent);
	}

	// ===========================================================================================

	//更新的线程
	class UpdateTask implements Runnable {
		private String path;
		private String filePath;

		/**
		 * 
		 * @param path 更新的网络地址
		 * @param filePath	存储的地址
		 */
		public UpdateTask(String path, String filePath) {
			this.path = path;
			this.filePath = filePath;
		}

		@Override
		public void run() {
			try {
				//启动下载
				File file = DownloadTask.getFile(path, filePath, progressDialog);
				progressDialog.dismiss();
				//安装下载好的文件
				install(file);
			} catch (Exception e) {
				e.printStackTrace();
				progressDialog.dismiss();
				Toast.makeText(SplashActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
				//跳转到加载UI界面的函数
				loadMainUI();
			}
		}

	}

}
