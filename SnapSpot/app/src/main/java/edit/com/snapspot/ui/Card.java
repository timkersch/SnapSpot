package edit.com.snapspot.ui;

import edit.com.snapspot.models.Spot;

/**
 * Created by Joakim on 2015-05-16.
 */
public class Card {
    private Spot spot;

    public Card(Spot spot){
        this.spot = spot;
    }

    public Spot getSpot(){
        return spot;
    }
}
