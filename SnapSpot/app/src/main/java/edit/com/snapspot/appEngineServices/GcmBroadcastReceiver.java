package edit.com.snapspot.appEngineServices;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by: Tim Kerschbaumer
 * Project: SnapSpot
 * Date: 15-05-12
 * Time: 13:37
 */

/** A wakeful broadcast receiver for getting messages from database
 *  what to do when a notification is received is handled in GcmIntentService
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		ComponentName componentName = new ComponentName(context.getPackageName(), GcmIntentService.class.getName());
		startWakefulService(context, (intent.setComponent(componentName)));
		setResultCode(Activity.RESULT_OK);
	}
}
