package com.xzit.security.engine;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * 这个类，我们要做成了单例模式，因为手机里面只有一个gps所以免得每次都新开一个对象
 * 
 * 负责获取位置
 * 
 * @author Lenovo
 * 
 */
public class GPSInfoProvider {
	private static GPSInfoProvider gpsInfoProvider;
	private static Context context;
	private static MyLocationListener listener;
	private LocationManager locationManager;

	private GPSInfoProvider() {

	}

	/**
	 * 为了让这个方法一定执行完，所以我们加入了synchronized来修饰
	 * 
	 * 
	 * @return  返回单例
	 */
	public static synchronized GPSInfoProvider getInstance(Context context) {

		System.out.println("GPSInfoProvider effecitive");

		if (gpsInfoProvider == null) {
			gpsInfoProvider = new GPSInfoProvider();
			GPSInfoProvider.context = context;
		}
		return gpsInfoProvider;
	}

	// 获得当前的经纬度
	public String getLocation() {

		System.out.println("getLocation");

		//获得系统的位置管理器
		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		String provider = getBestProvider();

		// 这个方法是位置更新的的操作，有四个参数
		// 第一个参数就是使用的定位设备啊，如gps，基站定位啊
		// 第二个参数就是多长时间更新一次定位信息，太频繁了会很耗电，根据自己程序的实际需要来进行确定
		// 第三个参数就是用户位移了多少米之后，就重新获取一次定位信息，太频繁了会很耗电，根据自己程序的实际需要来进行确定
		// 最后一个参数就是在位置发生变化的回调方法

		locationManager.requestLocationUpdates(provider, 60000, 50,getListener());
		//它会把所以支持的定位方式都打列出来，这样就可以知道手机所支持的定位啦
		// locationManager.getAllProviders();

		SharedPreferences sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);

		//再读出文件中存储的位置信息
		String location = sp.getString("lastLocation", "");

		System.out.println("getLocation的地址" + location);

		return location;
	}

	// 停止gps
	public void stopGPSListener() {
		if (locationManager != null) {
			locationManager.removeUpdates(getListener());
		}
	}

	// 配置定位器
	private String getBestProvider() {

		System.out.println("getBestProvider");

		Criteria criteria = new Criteria();
		// 这个是定义它的定位精度的
		// Criteria.ACCURACY_COARSE 这个是一般的定位
		// Criteria.ACCURACY_FINE 这个是精准定位
		criteria.setAccuracy(Criteria.ACCURACY_FINE);

		// 设置是不是对海拔敏感的
		criteria.setAltitudeRequired(false);

		// 设置对手机的耗电量，定位要求越高，越耗电
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);

		// 设置对速度变化是不是敏感
		criteria.setSpeedRequired(true);

		// 设置在定位时，是不是允许与运营商交换数据的开销
		criteria.setCostAllowed(true);

		// 这个方法是用来得到最好的定位方式的，它有两个参数，一个是Criteria(类似于Map集合)，就是一些条件，比如说对加速度敏感啊，什么海拔敏感这些的
		// 第二个参数就是，如果我们置为false，那么我们得到的也有可能是已经关掉了的设备，如果是true那么，就只会得到已经打开了的设备
		return locationManager.getBestProvider(criteria, true);
	}

	// 做成单例模式，同时为了一定执行完，也加上synchronized修饰
	private synchronized MyLocationListener getListener() {
		if (listener == null) {
			listener = new MyLocationListener();
		}
		return listener;
	}

	// =========================================================================监听器，负责监听位置的变化的

	private class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {

			System.out.println("ssssssssss");

			// 手机位置发生改变时调用的方法
			String latitude = "Latitude" + location.getLatitude();// 纬度
			String longitude = "Longitude" + location.getLongitude();// 经度

			System.out.println("latitude" + latitude);
			System.out.println("longitude" + longitude);

			//打开配置文件，在进行更新存储
			SharedPreferences sp = context.getSharedPreferences("config",
					Context.MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString("lastLocation", latitude + " - " + longitude);
			editor.commit();
			/**
			 * 已经成功存入文件中
			 */

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// 定位设备的状态发生改变的时候调用的方法，比如说用户把设备打开，或关闭，第二个参数就是设备的状态啦
		}

		@Override
		public void onProviderEnabled(String provider) {
			// 设备打开的时候调用的方法
		}

		@Override
		public void onProviderDisabled(String provider) {
			// 设备关闭的时候调用的方法
		}

	}

}
