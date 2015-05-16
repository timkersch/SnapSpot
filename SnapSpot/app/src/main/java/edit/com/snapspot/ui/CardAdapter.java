package edit.com.snapspot.ui;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import edit.com.snapspot.R;

/**
 * Created by Joakim on 2015-05-16.
 */
public class CardAdapter extends ArrayAdapter<Card> {
    private Context context;
    private List<Card> cards;

    public CardAdapter(Context context, int resource, List<Card> cards) {
        super(context, resource, cards);
        this.cards = cards;
        this.context = context;
        Log.d("CardAdapter", "Hej");
    }

    static class ViewHolder {
        TextView title;
        TextView description;
        TextView time;
        int position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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

            // store the holder with the view.
            convertView.setTag(viewHolder);
        }else{
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // get the TextView from the ViewHolder and then set the text (item name) and tag (item ID) values
        viewHolder.title.setText(cards.get(position).getSpot().getName());
        viewHolder.description.setText(cards.get(position).getSpot().getDescription());
        viewHolder.time.setText(cards.get(position).getSpot().getTimestamp().toString());

        /*LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.card_layout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.title);
        textView.setText(cards.get(position).getSpot().getName());
        Log.d("CardAdapter", "Adding card: " + position);*/

        return convertView;
    }


}
