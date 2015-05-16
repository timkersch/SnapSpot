package edit.com.snapspot.models;

/**
 * Created by: Tim Kerschbaumer
 * Project: SnapSpot
 * Date: 15-05-15
 * Time: 14:19
 */

/** This class represents a spot.
 */
public class Spot {
    private float latitude, longitude;
    private String name, description;

    public Spot(float latitude, float longitude, String name, String description){
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.description = description;
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

}
