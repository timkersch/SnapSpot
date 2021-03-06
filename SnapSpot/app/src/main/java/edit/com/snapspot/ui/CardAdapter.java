package edit.com.snapspot.ui;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import edit.com.snapspot.R;
import edit.com.snapspot.models.Spot;

/**
 * Created by Joakim on 2015-05-16.
 */
public class CardAdapter extends ArrayAdapter<Spot> {
    private Context context;
    private List<Spot> cards;
    private SimpleDateFormat sdf;

    public CardAdapter(Context context, int resource, List<Spot> cards) {
        super(context, resource, cards);
        this.cards = cards;
        this.context = context;
        Log.d("CardAdapter", "Hej");
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    static class ViewHolder {
        TextView title;
        TextView description;
        TextView time;
        int position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){

            // inflate the layout
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            convertView = inflater.inflate(R.layout.card_layout, parent, false);

            // well set up the ViewHolder
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.description = (TextView) convertView.findViewById(R.id.description);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.position = position;

            ((ImageButton) convertView.findViewById(R.id.remove)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cards.remove(position);
                    notifyDataSetChanged();
                    /*if(context instanceof MainActivity){
                        ((MainActivity)context).removePOI(new Spot(((TextView)v.findViewById(R.id.name)).getText().toString(), "", "", null, null));
                    }*/
                    Log.d("CardAdapter", "Removing");
                }
            });

            // store the holder with the view.
            convertView.setTag(viewHolder);
        }else{
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // get the TextView from the ViewHolder and then set the text (item name) and tag (item ID) values
        viewHolder.title.setText(cards.get(position).getName());
        viewHolder.description.setText(cards.get(position).getDescription());
        viewHolder.time.setText(sdf.format(new Date(cards.get(position).getTimestamp().getValue())));

        return convertView;
    }


}
