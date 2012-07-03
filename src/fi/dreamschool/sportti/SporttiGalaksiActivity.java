package fi.dreamschool.sportti;

import android.content.Intent;
import android.os.Bundle;
import com.phonegap.DroidGap;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.view.WindowManager;

public class SporttiGalaksiActivity extends DroidGap {
	
	private WifiManager.WifiLock wifiLock;
	private PowerManager.WakeLock wakeLock;
    
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = new Intent(".SporttiGalaksiWifiService");
		this.startService(intent);

	 	super.loadUrl("file:///android_asset/www/index.html");

		// http://developer.android.com/reference/android/net/wifi/WifiManager.WifiLock.html
        // http://developer.android.com/reference/android/net/wifi/WifiManager.html
        // http://www.framentos.com/en/android-tutorial/2012/02/27/how-to-prevent-wi-fi-sleep-on-android/
        WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiLock = wm.createWifiLock(WifiManager.WIFI_MODE_FULL, "My Tag"); // WIFI_MODE_FULL_HIGH_PERF
        wifiLock.acquire();

        //http://developer.android.com/reference/android/os/PowerManager.html
        //http://goltermann.cc/2011/11/android-accessing-wifi-even-in-standby-using-wakelock-wifilock-alarmmanager-and-services/
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag"); // PARTIAL_WAKE_LOCK, SCREEN_DIM_WAKE_LOCK
        wakeLock.acquire();

        if(wifiLock.isHeld()) {
        	System.out.println("WIFILOCK IS HELD, IN MODE: " + wm.getWifiState());
        } else {
        	System.out.println("WIFILOCK IS NOT HELD");
        }

        if(wakeLock.isHeld()) {
        	System.out.println("WAKELOCK IS HELD");
        } else {
        	System.out.println("WAKELOCK IS NOT HELD");
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

	}

	@Override
    public void onDestroy() {
        super.onDestroy();
 
        wifiLock.release();
        wakeLock.release();
    }

}
