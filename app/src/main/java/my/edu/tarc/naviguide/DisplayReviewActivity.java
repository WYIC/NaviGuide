package my.edu.tarc.naviguide;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisplayReviewActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    ListView listViewReview;
    List<Review> caList;
    RequestQueue queue;
    public static final String TAG = "my.edu.tarc.tester";
    String searchId, Username;

    EditText editTextReview;
    Spinner spinnerRating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_review);
        Intent intent = getIntent();
        listViewReview = (ListView) findViewById(R.id.listViewRecords);
        pDialog = new ProgressDialog(this);
        caList = new ArrayList<>();
        searchId = getIntent().getStringExtra("key");
        Username = getIntent().getStringExtra("username");

        editTextReview = findViewById(R.id.editTextReview);
        spinnerRating = findViewById(R.id.spinnerRating);
        search();
    }

    public void post(View view)
    {
        Review review = new Review();
        if (editTextReview.getText().toString().matches(""))
        {
            Toast.makeText(getApplicationContext(), "Please type something.", Toast.LENGTH_LONG).show();
        }
        else if (spinnerRating.getSelectedItemPosition() == 0)
        {
            Toast.makeText(getApplicationContext(), "Please select a rating.", Toast.LENGTH_LONG).show();
        }
        else
        {
            review.setRating(spinnerRating.getSelectedItem().toString());
            review.setContent(editTextReview.getText().toString());
            review.setRestaurantId(searchId);
            review.setUsername(Username);

            addReview(this, "https://naviguide.000webhostapp.com/insert_review.php?", review);

            editTextReview.setText("");
            spinnerRating.setSelection(0);
        }
    }

    private void search()
    {

        try {

            downloadReview(this, "https://naviguide.000webhostapp.com/select_review.php?ReviewId=" + searchId);


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void downloadReview(Context context, String url) {

        // Instantiate the RequestQueue
        queue = Volley.newRequestQueue(context);

        //Get Data
        if (!pDialog.isShowing())
            pDialog.setMessage("Syn with server...");
        pDialog.show();

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            caList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject reviewResponse = (JSONObject) response.get(i);
                                String ReviewId = reviewResponse.getString("ReviewId");
                                String ReviewRating = reviewResponse.getString("ReviewRating");
                                String ReviewContent = reviewResponse.getString("ReviewContent");
                                String RestaurantId = reviewResponse.getString("RestaurantId");
                                String UserName = reviewResponse.getString("UserName");
                                Review review = new Review(ReviewId, ReviewRating, ReviewContent, RestaurantId, UserName);
                                caList.add(review); //Add a course record to List
                            }
                            loadReview();
                            if (pDialog.isShowing())
                                pDialog.dismiss();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "Error" + volleyError.getMessage(), Toast.LENGTH_LONG).show();
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                    }
                });

        // Set the tag on the request.
        jsonObjectRequest.setTag(TAG);

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    private void loadReview() {
        final ReviewAdapter adapter = new ReviewAdapter(this, R.layout.content_main, caList);
        listViewReview.setAdapter(adapter);
        Toast.makeText(getApplicationContext(), "Count :" + caList.size(), Toast.LENGTH_LONG).show();
        listViewReview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Review tempUser = caList.get(position);
                Intent otherprofileIntent = new Intent(getApplicationContext(), Profile2Activity.class);
                otherprofileIntent.putExtra("username", tempUser.getUsername());
                startActivity(otherprofileIntent);
            }
        });
    }

    public void addReview(Context context, String url, final Review review) {
        //mPostCommentResponse.requestStarted();
        RequestQueue queue = Volley.newRequestQueue(context);

        //Send data
        try {
            StringRequest postRequest = new StringRequest(
                    Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(response);
                                int success = jsonObject.getInt("success");
                                String message = jsonObject.getString("message");
                                if (success==0) {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error. " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();

                    params.put("ReviewRating", review.getRating());
                    params.put("ReviewContent", review.getContent());
                    params.put("RestaurantId", review.getRestaurantId());
                    params.put("UserName", review.getUsername());
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }
            };
            queue.add(postRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
