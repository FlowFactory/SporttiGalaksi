package fi.dreamschool.sportti;

import android.content.Intent;
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

    }
}