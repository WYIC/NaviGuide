package my.edu.tarc.naviguide;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUserName, editTextPassword;
    private Button buttonLogin;
    private ProgressDialog pDialog;
    public static final String TAG = "my.edu.tarc.tester";
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");

        pDialog = new ProgressDialog(this);
        editTextUserName = findViewById(R.id.editTextUserName);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
    }

    public void Login(View view)
    {
        checkLogin(this, "https://naviguide.000webhostapp.com/check_login.php?UserName=" + editTextUserName.getText().toString() + "&Password=" + editTextPassword.getText().toString());
    }

    public void goSignup(View view)
    {
        Intent signupIntent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(signupIntent);
    }

    private void checkLogin(Context context, String url) {
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

                            if (response.length() > 0) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject userResponse = (JSONObject) response.get(i);
                                    String UserName = userResponse.getString("UserName");
                                    String Password = userResponse.getString("Password");
                                    String UserDescription = userResponse.getString("UserDescription");
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.putExtra("UserName", UserName);
                                    intent.putExtra("Password", Password);
                                    intent.putExtra("UserDescription", UserDescription);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Login Info Incorrect!", Toast.LENGTH_LONG).show();
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
