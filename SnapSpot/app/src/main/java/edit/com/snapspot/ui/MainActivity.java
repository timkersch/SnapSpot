package edit.com.snapspot.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.location.Location;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import edit.com.snapspot.R;
import edit.com.snapspot.appEngineServices.DbOperations;
import edit.com.snapspot.appEngineServices.POICallback;

import edit.com.snapspot.models.Spot;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        FeedFragment.OnFragmentInteractionListener, CreateFragment.OnFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    private SupportMapFragment mapFragment;
    private CreateFragment createFragment;
    private GoogleMap map;
    private FeedFragment feedFragment;
    private List<Marker> markers;
    private GoogleApiClient mGoogleApiClient;
    private final String TAG = "MainActivity";
    private boolean createOpen = false;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DbOperations.registerGcm(this);

        // Set up the action bar.
        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        markers = new ArrayList<>();

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        //DbOperations.addSpot(new Spot("Ottomania", "This is a description", "Street 1, 123 00, Gothenburg", System.currentTimeMillis(), 57.69632f, 11.97077f));
	    //DbOperations.addSpot(new Spot("Ölstugan Tullen", "This is a description", "Street 2, 321 00, Gothenburg", System.currentTimeMillis(), 57.69930f, 11.94940f));
	    //DbOperations.addSpot(new Spot("BrewDog Bar", "This is a description", "Street 3, 132 00, Gothenburg", System.currentTimeMillis(), 57.70326f, 11.95895f));
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public void getCurrentPlace(ResultCallback<PlaceLikelihoodBuffer> callback){
        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(mGoogleApiClient, null);
        result.setResultCallback(callback);
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void showAllMarkers(){
        if(markers.isEmpty()){
            Location location = map.getMyLocation();
            LatLng myLocation = new LatLng(0, 0);
            if (location != null) {
                myLocation = new LatLng(location.getLatitude(),
                        location.getLongitude());
            }
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
                    6));
        }
    }

    public void getPOIs(){
        // Todo: Use callback to get POIs
        //DbOperations.
        DbOperations.getSpots(new POICallback() {
            @Override
            public void onPOIReady(List<Spot> spots) {
                // Clear all markers
                map.clear();
                // Insert new markers
                for (Spot s : spots) {
                    Marker m = map.addMarker(new MarkerOptions()
                            .position(new LatLng(s.getLatitude(), s.getLongitude()))
                            .title(s.getName())
                            .snippet(s.getDescription()));
                    markers.add(m);
                }
                // Show markers
                showAllMarkers();
                // Update feed
                feedFragment.updateFeed(spots);
            }
        });

    }

    public void addPOI(Spot spot){
        DbOperations.addSpot(spot);
    }

    public void removePOI(Spot spot){
        //DbOperations.deleteSpot(spot);
    }

    @Override
    public void onFragmentInteraction(String id) {
        // Insert id (name) and delete
        //removePOI(new Spot(0, 0, id, "", "", null));
    }

    @Override
    public void onCreateNew() {
        //TODO open simons view
        if(createFragment == null){
            createFragment = CreateFragment.newInstance();
        }
        android.app.FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.replace(R.id.create_new, createFragment);
        trans.addToBackStack("createNew");
        trans.commit();
        createOpen = true;
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        Log.d(TAG, "onCreateNew");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onSend(Spot spot) {
        addPOI(spot);
    }

    @Override
    public void onBackPressed() {
        if (createOpen) {
            backFromCreateFragment();
            return;
        }
        super.onBackPressed();
    }

    private void backFromCreateFragment() {
        createOpen = false;
        getFragmentManager().popBackStack();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch(position){
                case 1:
                    if(mapFragment == null){
                        mapFragment = SupportMapFragment.newInstance();
                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                map = googleMap;
                                map.setMyLocationEnabled(true);
                                showAllMarkers();
                            }
                        });
                    }
                    return mapFragment;
                case 0:
                    if(feedFragment == null){
                        feedFragment = FeedFragment.newInstance();
                    }
                    return feedFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section_feed).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section_map).toUpperCase(l);
            }
            return null;
        }
    }

}
