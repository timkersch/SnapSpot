package edit.com.snapspot.appEngineServices;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import edit.com.snapspot.R;
import edit.com.snapspot.ui.MainActivity;

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
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void updateDevice(final String message) {
		if(message.equals("SPOT")) {
			NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
					.setContentTitle("SnapSpot")
					.setSmallIcon(R.drawable.ic_launcher2)
					.setStyle(new NotificationCompat.BigTextStyle().bigText("Someone just posted a new spot!"))
					.setContentText("Someone just posted a new spot!");

			mBuilder.setContentIntent(contentIntent);
			mNotificationManager.notify(1, mBuilder.build());
		}
	}

}