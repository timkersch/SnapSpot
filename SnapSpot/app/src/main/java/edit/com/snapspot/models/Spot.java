package edit.com.snapspot.models;

/**
 * Created by: Tim Kerschbaumer
 * Project: SnapSpot
 * Date: 15-05-15
 * Time: 14:19
 */

import com.google.api.client.util.DateTime;
import edit.com.backend.spot.model.GeoPt;

/** This class represents a spot.
 */
public class Spot {
    private GeoPt geoPt;
    private String name, description, address;
    private DateTime timestamp;

    public Spot(String name, String description, String address, long timeInMilliseconds, float latitude, float longitude){
		this.geoPt = new GeoPt();
		geoPt.setLatitude(latitude);
		geoPt.setLongitude(longitude);
		this.name = name;
		this.description = description;
		this.address = address;
		this.timestamp = new DateTime(timeInMilliseconds);
	}

	public Spot(String name, String description, String address, DateTime dateTime, GeoPt geoPt){
		this.geoPt = geoPt;
		this.name = name;
		this.description = description;
		this.address = address;
		this.timestamp = dateTime;
	}

    public float getLatitude() {
        return geoPt.getLatitude();
    }

    public float getLongitude() {
        return geoPt.getLongitude();
    }

    public String getName() {
        return name;
    }

	public GeoPt getGeoPt() {
		return geoPt;
	}

    public String getDescription() {
        return description;
    }

    public String getAddress(){
        return address;
    }

    public DateTime getTimestamp(){
        return timestamp;
    }

}
