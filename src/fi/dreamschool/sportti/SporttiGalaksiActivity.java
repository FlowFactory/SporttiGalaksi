package fi.dreamschool.sportti;

import android.content.Intent;
import android.content.IntentFilter;
import android.location.GpsStatus.Listener;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import com.phonegap.*;

public class SporttiGalaksiActivity extends DroidGap {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.loadUrl("file:///android_asset/www/index.html");
        
        Intent serviceIntent = new Intent();
        serviceIntent.setAction("fi.dreamschool.sportti.SporttiGalaksiWifi");
        this.startService(serviceIntent);
        
        
      //  IntentFilter intfil = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        
        
        
        
        /*
        try {
        	
        	WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        	
			while(true) {
				System.out.println( wm.getWifiState() );
				Thread.sleep(5 * 1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        */
        
        //System.out.println(WifiManager.EXTRA_NEW_STATE);
    }
}