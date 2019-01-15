package my.edu.tarc.naviguide;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RestaurantInfoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private MapView mMapView;

    private ProgressDialog pDialog;
    RequestQueue queue;
    public static final String TAG = "my.edu.tarc.tester";
    String searchId;
    String UserName;

    Restaurant restaurant = new Restaurant();
    TextView textViewRestaurantName, textViewRestaurantRating, textViewAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_info);

        textViewRestaurantName = findViewById(R.id.textViewRestaurantName);
        textViewRestaurantRating = findViewById(R.id.textViewRestaurantRating2);
        textViewAddress = findViewById(R.id.textViewAddress);
        mMapView = (MapView)findViewById(R.id.mapView);

        pDialog = new ProgressDialog(this);
        searchId = getIntent().getStringExtra("key");
        UserName = getIntent().getStringExtra("username");
        search();

        initGoogleMap(savedInstanceState);
    }

    private void initGoogleMap(Bundle savedInstanceState)
    {
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
    }

    public void goReview(View view)
    {
        Intent intent = new Intent(getApplicationContext(), DisplayReviewActivity.class);
        intent.putExtra("key", searchId);
        intent.putExtra("username", UserName);
        startActivity(intent);
    }

    private void search()
    {

        try {

            downloadReview(this, "https://naviguide.000webhostapp.com/select_restaurant2.php?RestaurantId=" + searchId);


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
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject restaurantResponse = (JSONObject) response.get(i);
                                String RestaurantId = restaurantResponse.getString("RestaurantId");
                                String RestaurantName = restaurantResponse.getString("RestaurantName");
                                String RestaurantRating = restaurantResponse.getString("RestaurantRating");
                                String Address = restaurantResponse.getString("RestaurantAddress");
                                restaurant = new Restaurant(RestaurantId, RestaurantName, RestaurantRating, Address);

                                textViewRestaurantName.setText(RestaurantName);
                                textViewRestaurantRating.setText(RestaurantRating);
                                textViewAddress.setText(Address);
                            }
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

    @Override
    protected void onResume()
    {
        super.onResume();
        downloadReview(this, "https://naviguide.000webhostapp.com/select_restaurant2.php?RestaurantId=" + searchId);
        mMapView.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
