package fi.dreamschool.sportti;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.IBinder;

public class SporttiGalaksiWifi extends Service {

	//
	private WifiLock wifiLock;

	/**
	 * 
	 */
	public IBinder onBind(Intent arg0) {
		return null;
	}

	/**
	 * 
	 */
	public void onCreate() {
		super.onCreate();

		// WifiManager
		WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		wifiLock = wm.createWifiLock(WifiManager.WIFI_MODE_FULL, "MyWifiTag");

		wifiLock.acquire();

		if (wifiLock.isHeld()) {
			System.out.println("WifiLock : isHeld");
		} else {
			System.out.println("WifiLock : notHeld");
		}
	}

	/**
	 * 
	 */
	public void onDestroy() {
		super.onDestroy();

		wifiLock.release();

		System.out.println("WifiLock onDestroy : release");

	}

}
