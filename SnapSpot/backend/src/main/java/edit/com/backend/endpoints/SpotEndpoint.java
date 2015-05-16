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

import static edit.com.backend.utils.OfyService.ofy;

/**
 * Created by: Tim Kerschbaumer
 * Project: SnapSpot
 * Date: 15-05-15
 * Time: 14:19
 */
@Api(name = "spot", version = "v1", namespace = @ApiNamespace(ownerDomain = "backend.com.edit", ownerName = "backend.com.edit", packagePath = ""))
public class SpotEndpoint {

	@ApiMethod(name = "addSpot")
	public void addSpot(@Named("name") String name, @Named("description") String description,
	                   @Named("latitude") float latitude, @Named("longitude") float longitude, @Named("date")Date date, @Named("adress") String adress) {
		SpotRecord record = new SpotRecord();
		record.setGeoPt(new GeoPt(latitude, longitude));
		record.setDescription(description);
		record.setName(name);
		record.setDate(date);
		record.setAdress(adress);
		ofy().save().entity(record).now();
	}

	@ApiMethod(name = "getSpots")
	public CollectionResponse<SpotRecord> getSpots() {
		List<SpotRecord> spotRecords = ofy().load().type(SpotRecord.class).list();
		return CollectionResponse.<SpotRecord>builder().setItems(spotRecords).build();
	}

	@ApiMethod(name = "findSpot")
	public SpotRecord findSpot(@Named("name") String name) {
		return ofy().load().type(SpotRecord.class).filter("name", name).first().now();
	}

	@ApiMethod(name = "removeSpot")
	public void removeSpot(@Named("name") String name) {
		// TODO remove this spot
		//ofy().load().type(SpotRecord.class).filter("name", name).first().now();
	}

	private void sendMessage(String message) {
		try {
			new MessagingEndpoint().sendMessage(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
