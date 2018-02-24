package com.sourcey.materiallogindemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import static android.widget.Toast.LENGTH_SHORT;


public class SupervisorActivity extends AppCompatActivity {

    @Bind(R.id.iv_user_login) ImageView show_image;
    //    ImageView show_image = (ImageView) this.findViewById(R.id.iv_user_login);
    //@Bind(R.id.iv_user_login) ImageView show_image;
    @Bind(R.id.tvId) TextView show_id;
    @Bind(R.id.tvUsertype) TextView show_usertype;
    @Bind(R.id.tvName) TextView show_name;
    @Bind(R.id.tvEmail) TextView show_email;
    @Bind(R.id.btn_show_all_report) Button show_all_report;
    @Bind(R.id.btn_validate_report) Button validate_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_main);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        ButterKnife.bind(this);


        Glide.with(this).load(SharedPrefManager.getInstance(this).getImageUser()).into(show_image);
//        com.squareup.picasso.Picasso.with(this).load(SharedPrefManager.getInstance(this).getImageUser()).into(show_image);
        show_id.setText(SharedPrefManager.getInstance(this).getIdUser());
        show_usertype.setText(SharedPrefManager.getInstance(this).getUserType());
        show_email.setText(SharedPrefManager.getInstance(this).getEmail());
        show_name.setText(SharedPrefManager.getInstance(this).getUserName());

        show_all_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SupervisorActivity.this, ValidateReportActivity.class);
                startActivity(intent);
            }
        });

        validate_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SupervisorActivity.this, ValidateReportActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_logout :
                Toast.makeText(getBaseContext(),"You are log out", LENGTH_SHORT).show();
                SharedPrefManager.getInstance(this).logout();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;
            default: return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }
}
