package edit.com.backend.endpoints;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.GeoPt;
import edit.com.backend.records.SpotRecord;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static edit.com.backend.utils.OfyService.ofy;

/**
 * Created by: Tim Kerschbaumer
 * Project: SnapSpot
 * Date: 15-05-15
 * Time: 14:19
 */
@Api(name = "spot", version = "v1", namespace = @ApiNamespace(ownerDomain = "backend.com.edit", ownerName = "backend.com.edit", packagePath = ""))
public class SpotEndpoint {

	private static final Logger log = Logger.getLogger(RegistrationEndpoint.class.getName());

	@ApiMethod(name = "addSpot")
	public void addSpot(@Named("name") String name,
	                    @Named("description") String description,
	                    @Named("adress") String adress,
	                    @Named("date") Date date,
	                    GeoPt geoPt) {
		SpotRecord record = new SpotRecord();
		record.setName(name);
		record.setDescription(description);
		record.setAdress(adress);
		record.setGeoPt(geoPt);
		record.setDate(date);
		ofy().save().entity(record).now();
		sendMessage("SPOT");
	}

	@ApiMethod(name = "getSpots")
	public CollectionResponse<SpotRecord> getSpots() {
		List<SpotRecord> spotRecords = ofy().load().type(SpotRecord.class).list();
		return CollectionResponse.<SpotRecord>builder().setItems(spotRecords).build();
	}

	@ApiMethod(name = "getSpot")
	public SpotRecord getSpot(@Named("name") String name) {
		return ofy().load().type(SpotRecord.class).filter("name", name).first().now();
	}

	@ApiMethod(name = "removeSpot")
	public void removeSpot(@Named("name") String name) {
		ofy().delete().entity(name);
	}

	private void sendMessage(String message) {
		try {
			new MessagingEndpoint().sendMessage(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
