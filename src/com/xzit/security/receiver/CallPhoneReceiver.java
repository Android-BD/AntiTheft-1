package com.xzit.security.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xzit.security.ui.LostProtectedActivity;


/**
 * 设置快捷的进入方式
 * 当输入1314拨号之后，程序会自动的进入
 * @author lenovo
 *
 */
public class CallPhoneReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		//获得系统的拨号盘上的号码
		String outPhoneNumber = this.getResultData();
		//假如获得是1314，则自动的跳转到程序
		if (outPhoneNumber.equals("1314")) {
			//跳转
			Intent i = new Intent(context, LostProtectedActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
			//停止拨号
			setResultData(null);
		}
	}

}
