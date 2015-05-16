package edit.com.snapspot.appEngineServices;

import android.content.Context;
import android.os.AsyncTask;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import edit.com.backend.registration.Registration;
import edit.com.backend.spot.model.CollectionResponseSpotRecord;
import edit.com.backend.spot.model.SpotRecord;
import edit.com.snapspot.models.Spot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by: Tim Kerschbaumer
 * Project: SnapSpot
 * Date: 15-05-12
 * Time: 13:38
 */

/** This class is used for registration of the device to GCM.
 */
public class DbOperations {

	// Set if the registration should be local or global
	private static final boolean LOCAL = false;
	private static final String PROJECT_ID = "461803206887";

	private static Registration regService;
	private static edit.com.backend.spot.Spot spotService;
	private static GoogleCloudMessaging gcm;

	/** Adds a spot to the backend
	 * @param spot the spot to be added
	 */
	public static void addSpot(final Spot spot) {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				initSpotService();
				try {
					spotService.addSpot(spot.getName(), spot.getDescription(), spot.getAddress(), spot.getTimestamp(), spot.getGeoPt()).execute();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
		}.execute();
	}

	/** Deletes a spot from the backed
	 * @param spot the spot to be deleted
	 */
	public static void deleteSpot(final Spot spot) {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				initSpotService();
				try {
					spotService.removeSpot(spot.getName()).execute();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
		}.execute();
	}

	public static void getSpots(final POICallback callback) {
		new AsyncTask<Void, Void, List<Spot>>() {
			@Override
			protected List<Spot> doInBackground(Void... params) {
				initSpotService();
				List<Spot> spotList = null;
				try {
					CollectionResponseSpotRecord record = spotService.getSpots().execute();
					List<SpotRecord> spots = record.getItems();
					spotList = new ArrayList<Spot>(spots.size());
					for(SpotRecord s : spots) {
						spotList.add(new Spot(s.getName(), s.getDescription(), s.getAdress(), s.getDate(), s.getGeoPt()));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				return spotList;
			}

			@Override
			protected void onPostExecute(List<Spot> spots) {
				// Callback
				callback.onPOIReady(spots);
			}

		}.execute();
	}

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

	// Initialize the spot service
	private static synchronized void initSpotService() {
		if(spotService == null) {
			edit.com.backend.spot.Spot.Builder builder;
			if(LOCAL) {
				// Code to run on LOCAL machine
				builder = new edit.com.backend.spot.Spot.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
						.setRootUrl("http://localhost:8080/_ah/api")
						.setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
							@Override
							public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
								abstractGoogleClientRequest.setDisableGZipContent(true);
							}
						});
			} else {
				builder = new edit.com.backend.spot.Spot.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
						.setRootUrl("https://southern-shard-93711.appspot.com/_ah/api/");
			}
			spotService = builder.build();
		}
	}

	// Initialize the registration service for gcm
	private static synchronized void initRegService() {
		if(regService == null) {
			Registration.Builder builder;
			if(LOCAL) {
				// Code to run on LOCAL machine
				builder = new Registration.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
						.setRootUrl("http://10.0.2.2:8080/_ah/api")
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
