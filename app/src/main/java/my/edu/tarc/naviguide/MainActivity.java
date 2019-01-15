package my.edu.tarc.naviguide;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "my.edu.tarc.tester";
    ListView listViewRestaurant;
    List<Restaurant> caList;
    private ProgressDialog pDialog;
    private static String GET_URL = "https://naviguide.000webhostapp.com/select_restaurant.php";
    RequestQueue queue;
    String UserName, Password, UserDescription;
    Spinner spinnerLocation;
    Button buttonSearch;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_profile:
                    Intent profileIntent = new Intent(getApplicationContext(), Profile.class);
                    profileIntent.putExtra("UserName", UserName);
                    profileIntent.putExtra("Password", Password);
                    profileIntent.putExtra("UserDescription", UserDescription);
                    startActivity(profileIntent);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        listViewRestaurant = findViewById(R.id.listViewRecords);
        pDialog = new ProgressDialog(this);
        caList = new ArrayList<>();
        spinnerLocation = findViewById(R.id.spinnerLocation);
        buttonSearch = findViewById(R.id.buttonSearch);

        downloadRestaurant(getApplicationContext(), GET_URL);

        UserName = getIntent().getStringExtra("UserName");
        Password = getIntent().getStringExtra("UserName");
        UserDescription = getIntent().getStringExtra("UserDescription");
    }



    private void downloadRestaurant(Context context, String url) {
        // Instantiate the RequestQueue
        queue = Volley.newRequestQueue(context);

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
                                JSONObject restaurantResponse = (JSONObject) response.get(i);
                                String RestaurantId = restaurantResponse.getString("RestaurantId");
                                String RestaurantName = restaurantResponse.getString("RestaurantName");
                                String RestaurantRating = restaurantResponse.getString("RestaurantRating");
                                String RestaurantAddress = restaurantResponse.getString("RestaurantAddress");
                                Restaurant restaurant = new Restaurant(RestaurantId, RestaurantName, RestaurantRating, RestaurantAddress);
                                caList.add(restaurant); //Add a course record to List
                            }
                            loadRestaurant();
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

    private void loadRestaurant() {
        final RestaurantAdapter adapter = new RestaurantAdapter(this, R.layout.content_main, caList);
        listViewRestaurant.setAdapter(adapter);
        Toast.makeText(getApplicationContext(), "Count :" + caList.size(), Toast.LENGTH_LONG).show();
        listViewRestaurant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Restaurant tempRestaurant = caList.get(position);
                Intent displayReviewIntent = new Intent(getApplicationContext(), RestaurantInfoActivity.class);
                displayReviewIntent.putExtra("key", tempRestaurant.getId());
                displayReviewIntent.putExtra("username", UserName);
                startActivity(displayReviewIntent);
            }
        });
    }

    public void search(View view)
    {
        if (spinnerLocation.getSelectedItemPosition() == 0)
        {
            downloadRestaurant(getApplicationContext(), GET_URL);
        }
        else
        {
            downloadRestaurant(getApplicationContext(), "https://naviguide.000webhostapp.com/select_restaurantarea.php?Area=" + spinnerLocation.getSelectedItem().toString());
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        downloadRestaurant(getApplicationContext(), GET_URL);
    }
}
