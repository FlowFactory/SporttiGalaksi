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
	private WifiManager wm;

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

		System.out.println("WifiLock onCreate");
		
		// WifiManager 
		wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		wifiLock = wm.createWifiLock(WifiManager.WIFI_MODE_FULL, "MyWifiTag");
		System.out.println(wm.getWifiState());
		wifiLock.acquire();
		
		System.out.println("WifiLock onCreate : acquired");
		
		  
        System.out.println(wm.getWifiState());
        
        if(wifiLock.isHeld()) {
        	System.out.println("isHeld");
        } else {
        	System.out.println("notHeld");
        }
	}

	/**
	 * 
	 */
	public void onDestroy() {
		
		super.onDestroy();
		
		System.out.println(wm.getWifiState());
		
		System.out.println("WifiLock onDestroy");
		
		wifiLock.release();
		
		System.out.println("WifiLock onDestroy : release");
  
		System.out.println(wm.getWifiState());
		
        //System.out.println(wm.EXTRA_WIFI_STATE);
	}
	
	/*public void onReceive() {
		super.onReceive();
		
	}*/

}

// static WifiManager.WifiLock wifilock;
// private static final String WIFILOCK_TAG = "WFWIFILOCK";

/*
public SporttiGalaksiWifi(final Context context) {
	WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

	wifilock = wm.createWifiLock(WifiManager.WIFI_MODE_FULL, WIFILOCK_TAG);
}
*/

/*
public void lock(final boolean state) {
	if(state && !wifilock.isHeld()) {
		wifilock.acquire();
		onAcquire();
	} else if(wifilock.isHeld()) {
		wifilock.release();
		onRelease();
	}
}
public void onRelease(){	
}
public void onAcquire() {
}
*/