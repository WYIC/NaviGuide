package my.edu.tarc.naviguide;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    EditText editTextUserName2, editTextPassword2, editTextRePassword;
    Button buttonSignup2;

    public static final String TAG = "my.edu.tarc.tester";
    private ProgressDialog pDialog;
    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        pDialog = new ProgressDialog(this);

        editTextUserName2 = findViewById(R.id.editTextUserName2);
        editTextPassword2 = findViewById(R.id.editTextPassword2);
        editTextRePassword = findViewById(R.id.editTextRePassword);
        buttonSignup2 = findViewById(R.id.buttonSignup2);
    }

    public void Signup(View view)
    {
        User user = new User();

        if (editTextUserName2.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(), "Please enter a user name", Toast.LENGTH_LONG).show();
        }

        else if (!editTextPassword2.getText().toString().matches(editTextRePassword.getText().toString()))
        {
            Toast.makeText(getApplicationContext(), "The 2 password are not the same.", Toast.LENGTH_LONG).show();
        }

        else {
            user.setUserName(editTextUserName2.getText().toString());
            user.setPassword(editTextPassword2.getText().toString());
            user.setDescription("");
            checkSignup(this, "https://naviguide.000webhostapp.com/signup.php", user);
        }
    }

    public void checkSignup(Context context, String url, final User user) {
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
                                    finish();
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
                    params.put("UserName", user.getUserName());
                    params.put("Password", user.getPassword());
                    params.put("UserDescription", user.getDescription());
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
