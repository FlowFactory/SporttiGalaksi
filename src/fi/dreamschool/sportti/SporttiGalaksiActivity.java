package fi.dreamschool.sportti;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import org.apache.cordova.*;
import com.strumsoft.*;

public class SporttiGalaksiActivity extends DroidGap {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.loadUrl("file:///android_asset/www/index.html");

		// lock wifi on
		Intent serviceIntent = new Intent();
		serviceIntent.setAction("fi.dreamschool.sportti.SporttiGalaksiWifi");
		this.startService(serviceIntent);

		// attach websocket factory
		// appView.addJavascriptInterface(new WebSocketFactory(appView), "WebSocketFactory");
	}
}