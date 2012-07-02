package fi.dreamschool.sportti;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
//import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
//import android.util.Log;

public class SporttiGalaksiWifiService extends Service {

	private WifiManager.WifiLock wifiLock;
    private PowerManager.WakeLock wakeLock;
    
	public SporttiGalaksiWifiService() {
		// http://developer.android.com/reference/android/net/wifi/WifiManager.WifiLock.html
	    // http://developer.android.com/reference/android/net/wifi/WifiManager.html
	    // http://www.framentos.com/en/android-tutorial/2012/02/27/how-to-prevent-wi-fi-sleep-on-android/
	    WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
	    wifiLock = wm.createWifiLock(WifiManager.WIFI_MODE_FULL, "My Tag"); // WIFI_MODE_FULL_HIGH_PERF
	    wifiLock.acquire();

	    // http://goltermann.cc/2011/11/android-accessing-wifi-even-in-standby-using-wakelock-wifilock-alarmmanager-and-services/
	    PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
	    wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag");
	    wakeLock.acquire();

	    if(wakeLock.isHeld()) {
	    	System.out.println("WAKELOCK IS HELD");
	    } else {
	    	System.out.println("WAKELOCK IS NOT HELD");
	    }
	    
	    System.out.println("SporttiService INIT");
	}
	
	@Override
    public void onDestroy() {
        super.onDestroy();
 
        wifiLock.release();
        wakeLock.release();
    }

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	/*
	@Override
    public void onCreate() {
		
		// http://developer.android.com/reference/android/net/wifi/WifiManager.WifiLock.html
	    // http://developer.android.com/reference/android/net/wifi/WifiManager.html
	    // http://www.framentos.com/en/android-tutorial/2012/02/27/how-to-prevent-wi-fi-sleep-on-android/
	    WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
	    wifiLock = wm.createWifiLock(WifiManager.WIFI_MODE_FULL, "My Tag"); // WIFI_MODE_FULL_HIGH_PERF
	    wifiLock.acquire();

	    // http://goltermann.cc/2011/11/android-accessing-wifi-even-in-standby-using-wakelock-wifilock-alarmmanager-and-services/
	    PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
	    wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag");
	    wakeLock.acquire();

	    if(wakeLock.isHeld()) {
	    	System.out.println("WAKELOCK IS HELD");
	    } else {
	    	System.out.println("WAKELOCK IS NOT HELD");
	    }
	    
	    System.out.println("SporttiService INIT");
    }
	*/

	/*
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       Log.i("LocalService", "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }
	*/

	/*
	@Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
	*/
	/**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
	/* 
    public class LocalBinder extends Binder {
    	SporttiGalaksiWifiService getService() {
            return SporttiGalaksiWifiService.this;
        }
    }
	*/
    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    //private final IBinder mBinder = new LocalBinder();

}
