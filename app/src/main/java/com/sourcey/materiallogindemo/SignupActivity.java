package com.sourcey.materiallogindemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Bind;


public class SignupActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private static final String URL = "http://mobile.if.its.ac.id/healthfood/user_signup.php";
    private StringRequest request;
    private final int IMG_REQUEST = 1;
    private Bitmap bitmap;
    private static final int REQUEST_LOGIN = 0;

    private static final String TAG = "SignupActivity";

    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.iv_user) ImageView _imageUser;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.input_repassword) EditText _repasswordText;
    @Bind(R.id.input_name) EditText _nameText;
    @Bind(R.id.input_phone) EditText _phoneText;
    @Bind(R.id.input_date) EditText _dateText;
    @Bind(R.id.btn_signup) Button _signupButton;
    @Bind(R.id.position_spinner) Spinner hintSpinner;
    @Bind(R.id.gender_spinner) Spinner hintSpinner2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        if (SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }
        ButterKnife.bind(this);
        requestQueue = Volley.newRequestQueue(this);

        _imageUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        Spinner hintSpinner = (Spinner) findViewById(R.id.position_spinner);
//        hintSpinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        Spinner hintSpinner2 = (Spinner) findViewById(R.id.gender_spinner);
//        hintSpinner2.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.position_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.gender_spinner, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        hintSpinner.setAdapter(new HintSpinnerAdapter(
                adapter, R.layout.hint_row_item, this));
        hintSpinner2.setAdapter(new HintSpinnerAdapter2(
                adapter2, R.layout.hint_row_item2, this));

        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    public void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMG_REQUEST && resultCode==RESULT_OK && data!=null){
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                _imageUser.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String imageToString (Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgbytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgbytes,Base64.DEFAULT);

    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
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
                                    if(jsonObject.names().get(0).equals("success")){
                                        Toast.makeText(getApplicationContext(), jsonObject.getString("success"),Toast.LENGTH_SHORT).show();
//                                        onLoginSuccess();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    } else {
                                        Toast.makeText(getApplicationContext(), jsonObject.getString("error"),Toast.LENGTH_SHORT).show();
                                        onSignupFailed();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Web Service Error",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String, String> hashMap = new HashMap<String, String>();
                                hashMap.put("name",_nameText.getText().toString());
                                hashMap.put("imagepath",imageToString(bitmap));
                                hashMap.put("email", _emailText.getText().toString());
                                hashMap.put("usertype",hintSpinner.getSelectedItem().toString());
                                hashMap.put("password", _passwordText.getText().toString());
                                hashMap.put("gender",hintSpinner2.getSelectedItem().toString());
                                hashMap.put("phone", _phoneText.getText().toString());
                                hashMap.put("dob",_dateText.getText().toString());
                                return hashMap;
                            }
                        };

                        requestQueue.add(request);

                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Signing Up failed", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
        startActivity(new Intent(getApplicationContext(), SignupActivity.class));
    }

    public boolean validate() {
        boolean valid = true;
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String repassword = _repasswordText.getText().toString();
        String name = _nameText.getText().toString();
        String phone = _phoneText.getText().toString();
//        String date = _dateText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (repassword.isEmpty() || repassword.length() < 4 || repassword.length() > 10 || !(repassword.equals(password))) {
            _repasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _repasswordText.setError(null);
        }


        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (phone.isEmpty() || phone.length() > 12 ) {
            _phoneText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _phoneText.setError(null);
        }

        return valid;
    }
}