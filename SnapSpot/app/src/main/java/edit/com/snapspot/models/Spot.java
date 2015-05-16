package edit.com.snapspot.models;

/**
 * Created by: Tim Kerschbaumer
 * Project: SnapSpot
 * Date: 15-05-15
 * Time: 14:19
 */

import com.google.api.client.util.DateTime;

/** This class represents a spot.
 */
public class Spot {
    private float latitude, longitude;
    private String name, description, address;
    private DateTime timestamp;

    public Spot(float latitude, float longitude, String name, String description, String address, DateTime timestamp){
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.description = description;
        this.address = address;
        this.timestamp = timestamp;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
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
