package fi.dreamschool.sportti;

import java.net.URI;
import android.webkit.WebView;

public class WebSocket extends com.strumsoft.websocket.phonegap.WebSocket {
	
	public WebSocket(WebView appView, URI uri, Draft draft, String id) {
		super(appView, uri, draft, id);
	}

	public void onOpen(){
		super.onOpen();
	}
	
	public void onClose() {
		super.onClose();
	}
	
	public void onMessage(String msg) {
		super.onMessage(msg);
	}

	public void onReconnect() {
		
	}

}
