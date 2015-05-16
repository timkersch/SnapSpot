package edit.com.snapspot.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edit.com.snapspot.R;

/**
 * Created by Joakim on 2015-05-16.
 */
public class CardAdapter extends ArrayAdapter<Card> {
    private Context context;
    private List<Card> cards;

    public CardAdapter(Context context, int resource, List<Card> cards) {
        super(context, resource);
        this.cards = cards;
        this.context = context;
    }

    static class ViewHolder {
        TextView title;
        TextView description;
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
        viewHolder.description.setTag(cards.get(position).getSpot().getDescription());

        return convertView;
    }


}
