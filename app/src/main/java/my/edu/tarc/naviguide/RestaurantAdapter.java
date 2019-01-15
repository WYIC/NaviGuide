package my.edu.tarc.naviguide;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by TARC on 8/6/2015.
 */
public class RestaurantAdapter extends ArrayAdapter<Restaurant> {

    public RestaurantAdapter(Activity context, int resource, List<Restaurant> list) {
        super(context, resource, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Restaurant restaurant = getItem(position);

        LayoutInflater inflater  = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.restaurant_record, parent, false);
        TextView textViewName, textViewRestaurantRating;

        textViewName = rowView.findViewById(R.id.textViewName);
        textViewRestaurantRating = rowView.findViewById(R.id.textViewRestaurantRating);

        textViewName.setText(restaurant.getName());
        textViewRestaurantRating.setText(textViewRestaurantRating.getText() + ":" + restaurant.getRating());

        return rowView;
    }
}