package com.sourcey.materiallogindemo;

/**
 * Created by ASUS on 5/18/2017.
 */
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InputReportActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private static final String URL = "http://mobile.if.its.ac.id/healthfood/insert_report.php";
    private StringRequest request;

    private static final String TAG = "InputReportActivity";

    @Bind(R.id.input_title) EditText _titleText;
    @Bind(R.id.input_description) EditText _descriptionText;
    @Bind(R.id.btn_send_report) Button _sendButton;
    @Bind(R.id.tv_longitude) TextView _longitude;
    @Bind(R.id.tv_latitude) TextView _latitude;
    @Bind(R.id.tv_id_staff) TextView _staff;
    @Bind(R.id.tv_isvalidated) TextView _isvalidated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_report);
        ButterKnife.bind(this);
        requestQueue = Volley.newRequestQueue(this);


        // check if GPS enabled
        GPSTracker gpsTracker = new GPSTracker(this);

        String stringLongitude = String.valueOf(gpsTracker.longitude);
        _longitude.setText(stringLongitude);

        String stringLatitude = String.valueOf(gpsTracker.latitude);
        _latitude.setText(stringLatitude);

        String intId = String.valueOf(SharedPrefManager.getInstance(this).getIdUser());
        _staff.setText(intId);


        _sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });

    }
    public void send() {
        Log.d(TAG, "Send");

        if (!validate()) {
            onSendFailed();
            return;
        }

        _sendButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(InputReportActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sending Report...");
        progressDialog.show();


        // TODO: Implement your own signup logic here.
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.names().get(0).equals("success")) {
                                        Toast.makeText(getApplicationContext(), jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    } else {
                                        Toast.makeText(getApplicationContext(), jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                                        onSendFailed();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String, String> hashMap = new HashMap<String, String>();
                                hashMap.put("title", _titleText.getText().toString());
                                hashMap.put("description", _descriptionText.getText().toString());
                                hashMap.put("longitude", _longitude.getText().toString());
                                hashMap.put("latitude", _latitude.getText().toString());
                                hashMap.put("isvalidated", _isvalidated.getText().toString());
                                hashMap.put("staff", _staff.getText().toString());
                                return hashMap;
                            }
                        };
                        requestQueue.add(request);

                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSendSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSendSuccess() {
        _sendButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSendFailed() {
        Toast.makeText(getBaseContext(), "Failed to send report", Toast.LENGTH_LONG).show();
        _sendButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String title = _titleText.getText().toString();
        String description = _descriptionText.getText().toString();

        if (title.isEmpty() || title.length() < 5) {
            _titleText.setError("Title must be input minimum 5 characters");
            valid = false;
        } else {
            _titleText.setError(null);
        }


        if (description.isEmpty() || description.length() < 10) {
            _descriptionText.setError("Description must be input minimum 10 characters");
            valid = false;
        } else {
            _descriptionText.setError(null);
        }

        return valid;

    }
}