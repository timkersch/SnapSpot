package edit.com.snapspot.appEngineServices;

import java.util.List;

import edit.com.snapspot.models.Spot;

/**
 * Created by Joakim on 2015-05-15.
 */
public interface POICallback {

    void onPOIready(List<Spot> spots);
}
