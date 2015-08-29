package com.instigate.aggregator06;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.HashMap;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.*;


public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private HashMap userInfoCheck = new HashMap();
    private Button mSignInBtn = (Button)findViewById(R.id.signInButton);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        final EditText mUser   = (EditText)findViewById(R.id.user);
        final EditText mPass   = (EditText)findViewById(R.id.pass);


        mSignInBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        userInfoCheck.put("username", mUser.getText().toString());
                        userInfoCheck.put("password", mPass.getText().toString());
                    }
                });

        //mSignInBtn.setOnClickListener(this);
        mSignInBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                System.out.println("This works");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void backToSN2(View view)
    {
        Intent j = new Intent(SignInActivity.this, SelectSNActivity.class);
        startActivity(j);
    }


    @Override
    public void onClick(View v) {

    }
}
