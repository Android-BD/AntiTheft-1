package com.xzit.security.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

/**
 * 开机自启动 当检测到开机的广播的时候做出的操作
 * 
 * @author Lenovo
 * 
 */
public class BootCompleteReceiver extends BroadcastReceiver {
	private SharedPreferences sp;

	@Override
	public void onReceive(Context context, Intent intent) {
		// 读取关机之前的配置文件
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		// 判断是否开启了保护程序
		boolean isProtected = sp.getBoolean("isProtected", false);
		// 假如开启了
		if (isProtected) {

			// 获得系统的电话管理器
			TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			// 获得当前的手机卡编号
			String currentSim = telephonyManager.getSimSerialNumber();
			// 获取关机之前保存的编号
			String protectedSim = sp.getString("simSerial", "");

			System.out.println("Sim卡已经变更了，手机可能被盗");

			// 假如两个编号不一样，则判断手机可能已经被盗，则向用户之前配置的安全号码手机发送手机
			if (!currentSim.equals(protectedSim)) {

				// 打开短信管理器
				SmsManager smsManager = SmsManager.getDefault();
				// 获得安全号码
				String number = sp.getString("number", "");
				// 发送短信
				smsManager.sendTextMessage(number, null, "Card Sim has been changed", null, null);

			}

		}
	}

}
