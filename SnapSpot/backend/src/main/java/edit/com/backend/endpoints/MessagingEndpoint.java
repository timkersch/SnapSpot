package edit.com.backend.endpoints;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiNamespace;
import edit.com.backend.records.RegistrationRecord;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import javax.inject.Named;

import static edit.com.backend.utils.OfyService.ofy;

@Api(name = "messaging", version = "v1", namespace = @ApiNamespace(ownerDomain = "backend.com.edit", ownerName = "backend.com.edit", packagePath = ""))
public class MessagingEndpoint {
	private static final Logger log = Logger.getLogger(MessagingEndpoint.class.getName());

	private static final String API_KEY = System.getProperty("gcm.api.key");

	public void sendMessage(@Named("message") String message) throws IOException {
		if (message == null || message.trim().length() == 0) {
			log.warning("Not sending message because it is empty");
			return;
		}
		// Crop longer messages
		if (message.length() > 1000) {
			message = message.substring(0, 1000) + "[...]";
		}
		Sender sender = new Sender(API_KEY);
		Message msg = new Message.Builder().addData("message", message).build();
		List<RegistrationRecord> records = ofy().load().type(RegistrationRecord.class).list();
		for (RegistrationRecord record : records) {
			Result result = sender.send(msg, record.getRegId(), 5);
			if (result.getMessageId() != null) {
				log.info("Message sent to " + record.getRegId());
				String canonicalRegId = result.getCanonicalRegistrationId();
				if (canonicalRegId != null) {
					// If the regId changed, update the datastore
					log.info("Registration Id changed for " + record.getRegId() + " updating to " + canonicalRegId);
					record.setRegId(canonicalRegId);
					ofy().save().entity(record).now();
				}
			} else {
				String error = result.getErrorCodeName();
				if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
					log.warning("Registration Id " + record.getRegId() + " no longer registered with GCM, removing from datastore");
					// if the device is no longer registered with Gcm, remove it from the datastore
					ofy().delete().entity(record).now();
				} else {
					log.warning("Error when sending message : " + error);
				}
			}
		}
	}
}
