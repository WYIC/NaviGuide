package my.edu.tarc.naviguide;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class Profile2Activity extends AppCompatActivity {

    private String UserName, Password, UserDescription;
    private TextView textViewUserNameOther;
    private TextView textViewUserDescriptionOther;

    public static final String TAG = "my.edu.tarc.tester";
    private ProgressDialog pDialog;
    RequestQueue queue;
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);

        pDialog = new ProgressDialog(this);

        UserName = getIntent().getStringExtra("username");

        textViewUserNameOther = findViewById(R.id.textViewUserNameOther);
        textViewUserDescriptionOther = findViewById(R.id.textViewUserDescriptionOther);

        loadInfo(this, "https://naviguide.000webhostapp.com/select_user.php?UserName=" + UserName);
    }

    private void loadInfo(Context context, String url) {

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
                                JSONObject userResponse = (JSONObject) response.get(i);
                                String UserName = userResponse.getString("UserName");
                                String Password = userResponse.getString("Password");
                                String UserDescription = userResponse.getString("UserDescription");
                                textViewUserNameOther.setText(UserName);
                                textViewUserDescriptionOther.setText(UserDescription);
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
}
