package edit.com.snapspot.appEngineServices;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by: Tim Kerschbaumer
 * Project: SnapSpot
 * Date: 15-05-12
 * Time: 13:36
 */

/** This class handles messages from database.
 */
public class GcmIntentService extends IntentService {

	public GcmIntentService() {
		super("southern-shard-93711");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);

		if(extras != null && !extras.isEmpty()) {
			if(GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				updateDevice(extras.getString("message"));
			}
		}
	}

	private void updateDevice(String message) {
		// TODO here goes what to do when device gets a message
	}
}
