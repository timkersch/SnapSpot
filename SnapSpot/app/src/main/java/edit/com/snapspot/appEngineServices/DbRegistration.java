package edit.com.snapspot.appEngineServices;

import android.content.Context;
import android.os.AsyncTask;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import edit.com.backend.registration.Registration;

import java.io.IOException;

/**
 * Created by: Tim Kerschbaumer
 * Project: SnapSpot
 * Date: 15-05-12
 * Time: 13:38
 */

/** This class is used for registration of the device to GCM.
 */
public class DbRegistration {

	// Set if the registration should be LOCAL or global
	private static final boolean LOCAL = false;
	private static final String PROJECT_ID = "461803206887";

	private static Registration regService;
	private static GoogleCloudMessaging gcm;

	/** Register this device for GCM
	 * @param context
	 */
	public static void registerGcm(final Context context) {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				initRegService();
				String regId = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regId = gcm.register(PROJECT_ID);
					regService.register(regId).execute();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return regId;
			}

			@Override
			protected void onPostExecute(String regId) {
				// Store the registration ID
			}
		}.execute();
	}

	// Initialize the registration service for gcm
	private static synchronized void initRegService() {
		if(regService == null) {
			Registration.Builder builder;
			if(LOCAL) {
				// Code to run on LOCAL machine
				builder = new Registration.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
						.setRootUrl("http://localhost:8080/_ah/api")
						.setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
							@Override
							public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
								abstractGoogleClientRequest.setDisableGZipContent(true);
							}
						});
			} else {
				builder = new Registration.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
						.setRootUrl("https://southern-shard-93711.appspot.com/_ah/api/");
			}
			regService = builder.build();
		}
	}
}
