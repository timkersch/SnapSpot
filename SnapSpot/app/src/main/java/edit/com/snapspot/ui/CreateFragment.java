package edit.com.snapspot.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.maps.model.LatLng;

import edit.com.snapspot.R;
import edit.com.snapspot.models.Spot;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    private final String TAG = "CreateFragment";
    private String name, address;
    private LatLng location;
    private EditText description;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateFragment newInstance() {
        return new CreateFragment();
    }

    public CreateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_create_spot, container, false);
        ImageButton cancel = (ImageButton) view.findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        ImageButton submit = (ImageButton) view.findViewById(R.id.submit_button);
        submit.setOnClickListener(this);
        description = (EditText) view.findViewById(R.id.cs_description);
        ((MainActivity) getActivity()).getCurrentPlace(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer placeLikelihoods) {
                Place best = placeLikelihoods.get(0).getPlace();
                name = best.getName().toString();
                address = best.getAddress().toString();
                location = best.getLatLng();
                ((TextView) view.findViewById(R.id.name)).setText(name);
                ((TextView) view.findViewById(R.id.adress)).setText(address);
                Log.d(TAG, "Place: " + name + " " + address);
                description.requestFocus();
                description.setText("");
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    @Override
    public void onClick(View v) {
        Spot spot = new Spot(name, description.getText().toString(), address,
                System.currentTimeMillis(), (float)location.latitude, (float)location.longitude);
        mListener.onSend(spot);
        Log.d(TAG, "Submit");
        getActivity().onBackPressed();
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
        void onFragmentInteraction(Uri uri);
        void onSend(Spot spot);
    }

}
