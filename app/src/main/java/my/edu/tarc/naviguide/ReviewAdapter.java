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
public class ReviewAdapter extends ArrayAdapter<Review> {

    public ReviewAdapter(Activity context, int resource, List<Review> list) {
        super(context, resource, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Review review = getItem(position);

        LayoutInflater inflater  = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.review_record, parent, false);
        TextView textViewRating, textViewContent, textViewAUserName;

        textViewAUserName = rowView.findViewById(R.id.textViewUserName);
        textViewRating = rowView.findViewById(R.id.textViewReviewRating);
        textViewContent = rowView.findViewById(R.id.textViewContent);

        textViewAUserName.setText(textViewAUserName.getText() + ":" + review.getUsername());
        textViewRating.setText(textViewRating.getText() + ":" + review.getRating());
        textViewContent.setText(textViewContent.getText() + ":" + review.getContent());

        return rowView;
    }
}