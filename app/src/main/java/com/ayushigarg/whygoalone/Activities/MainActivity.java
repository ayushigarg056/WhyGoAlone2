package com.ayushigarg.whygoalone.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.ayushigarg.whygoalone.R;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";

    Button btnLogin, btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignup = (Button) findViewById(R.id.btnSignup);

        btnLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();


        if (id == R.id.btnLogin){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        if (id == R.id.btnSignup){
            startActivity(new Intent(MainActivity.this, SignupActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
    }
}
