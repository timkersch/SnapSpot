package edit.com.backend.endpoints;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import edit.com.backend.records.RegistrationRecord;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import javax.inject.Named;

import static edit.com.backend.utils.OfyService.ofy;

@Api(name = "registration", version = "v1", namespace = @ApiNamespace(ownerDomain = "backend.com.edit", ownerName = "backend.com.edit", packagePath = ""))
public class RegistrationEndpoint {

	private static final Logger log = Logger.getLogger(RegistrationEndpoint.class.getName());

	/**
	 * Register a device to the backend
	 *
	 * @param regId The Google Cloud Messaging registration Id to add
	 */
	@ApiMethod(name = "register")
	public void registerDevice(@Named("regId") String regId) {
		log.info("Register device");
		if (findRecord(regId) != null) {
			log.info("Device " + regId + " already registered, skipping register");
			return;
		}
		RegistrationRecord record = new RegistrationRecord();
		record.setRegId(regId);
		ofy().save().entity(record).now();
	}

	/**
	 * Unregister a device from the backend
	 *
	 * @param regId The Google Cloud Messaging registration Id to remove
	 */
	@ApiMethod(name = "unregister")
	public void unregisterDevice(@Named("regId") String regId) {
		RegistrationRecord record = findRecord(regId);
		if (record == null) {
			log.info("Device " + regId + " not registered, skipping unregister");
			return;
		}
		ofy().delete().entity(record).now();
	}

	/**
	 * Return a collection of registered devices
	 *
	 * @param count The number of devices to list
	 * @return a list of Google Cloud Messaging registration Ids
	 */
	@ApiMethod(name = "listDevices")
	public CollectionResponse<RegistrationRecord> listDevices(@Named("count") int count) {
		List<RegistrationRecord> records = ofy().load().type(RegistrationRecord.class).limit(count).list();
		return CollectionResponse.<RegistrationRecord>builder().setItems(records).build();
	}

	private RegistrationRecord findRecord(String regId) {
		return ofy().load().type(RegistrationRecord.class).filter("regId", regId).first().now();
	}

}
