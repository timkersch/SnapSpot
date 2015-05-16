package edit.com.snapspot.ui;

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.card_layout, parent, false);
        TextView title = (TextView) rowView.findViewById(R.id.title);
        TextView description = (TextView) rowView.findViewById(R.id.description);
        title.setText(cards.get(position).getSpot().getName());
        description.setText(cards.get(position).getSpot().getDescription());
        return rowView;
    }


}
