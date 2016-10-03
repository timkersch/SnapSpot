package edit.com.snapspot.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.api.client.util.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edit.com.snapspot.R;

import edit.com.snapspot.models.Spot;
import edit.com.snapspot.ui.dummy.DummyContent;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class FeedFragment extends Fragment implements AbsListView.OnItemClickListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    private final String TAG = "FeedFragment";

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private CardAdapter mAdapter;
    private List<Spot> cards;
    private SwipeRefreshLayout swipeRefreshLayout;

    // TODO: Rename and change types of parameters
    public static FeedFragment newInstance() {
        return new FeedFragment();
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FeedFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Change Adapter to display your content

        cards = new ArrayList<>();

        mAdapter = new CardAdapter(getActivity(), R.layout.card_layout, cards);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        FloatingActionButton floatingButton = (FloatingActionButton) view.findViewById(R.id.floating_button);
        floatingButton.setOnClickListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        }, 500);

        Log.d(TAG, "onCreateView");

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, "Attach");
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void updateFeed(List<Spot> spots){
        // Update the adapter with new spots
        cards.clear();
        for(Spot s : spots){
            cards.add(s);
        }
        mAdapter.notifyDataSetChanged();
        // Disable refreshing
        swipeRefreshLayout.setRefreshing(false);
        Log.d(TAG, "updateFeed");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(cards.get(position).getName());
            Log.d(TAG, "OnItemClick");
        }
    }

    @Override
    public void onClick(View v) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onCreateNew();
            Log.d(TAG, "OnFloatingClick");
        }
    }

    @Override
    public void onRefresh() {
        mListener.requestRefresh();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String id);
        void onCreateNew();
        void requestRefresh();
    }

}
