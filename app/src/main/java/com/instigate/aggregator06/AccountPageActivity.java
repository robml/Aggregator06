package com.instigate.aggregator06;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;


public class AccountPageActivity extends AppCompatActivity implements
         GoogleApiClient.ConnectionCallbacks, ResultCallback<People.LoadPeopleResult> {

    private static final String TAG = "AccountPageActivity";

    /* RequestCode for resolutions involving sign-in */
    private static final int RC_SIGN_IN = 9001;

    /* View to display current status (signed-in, signed-out, disconnected, etc) */
    private TextView mStatus;

    /* Client for accessing Google APIs */
    private GoogleApiClient mGoogleApiClient;

    private TextView nameValue;
    private String nameValueFromHash;
    //private String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_page);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PLUS_ME))
                .addScope(new Scope(Scopes.PLUS_LOGIN))
                .build();


        //findViewById(R.id.sign_out_button).setOnClickListener(this);
        //findViewById(R.id.disconnect_button).setOnClickListener(this);

        //Set up view instances
        mStatus = (TextView) findViewById(R.id.mStatus);

        nameValue = (TextView) findViewById(R.id.nameValue);

        Intent intent = getIntent();

        String fullName = intent.getStringExtra("currentName");

        nameValue.setText("Name: " + fullName);

    }


    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
// Show signed-in user's name
            String name = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient).getDisplayName();
            mStatus.setText("Signed in as " + name);

        } else {
// Show signed-out message
            //mStatus.setText(R.string.signed_out);
            Intent back_to_list = new Intent(AccountPageActivity.this, AccountListActivity.class);
            startActivity(back_to_list);
        }

    }

    @Override
    public void onResult(People.LoadPeopleResult peopleData) {
        if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
            PersonBuffer personBuffer = peopleData.getPersonBuffer();
            try {
                int count = personBuffer.getCount();
                for (int i = 0; i < count; i++) {
                    Log.d(TAG, "Display name: " + personBuffer.get(i).getDisplayName());
                }
            } finally {
                personBuffer.close();
            }
        } else {
            Log.e(TAG, "Error requesting visible circles: " + peopleData.getStatus());
        }
    }


    @Override
    public void onConnected(Bundle bundle) {

        mGoogleApiClient.connect();

        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            String personName = currentPerson.getDisplayName();
            Person.Image personPhoto = currentPerson.getImage();
            String personGooglePlusProfile = currentPerson.getUrl();

            Plus.PeopleApi.loadVisible(mGoogleApiClient, null)
                    .setResultCallback(this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account_page, menu);
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


    /*public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_out_button:

// Clear the default account so that GoogleApiClient will not automatically
// connect in the future.
// [START sign_out_clicked]
                if (mGoogleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                }

                Intent k = new Intent(AccountPageActivity.this, AccountListActivity.class);
                startActivity(k);
// [END sign_out_clicked]

                break;
            case R.id.disconnect_button:
// Revoke all granted permissions and clear the default account.  The user will have
// to pass the consent screen to sign in again.
// [START disconnect_clicked]
                if (mGoogleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                }

                Intent i = new Intent(AccountPageActivity.this, AccountListActivity.class);
                startActivity(i);
// [END disconnect_clicked]

                break;
            case R.id.backbtn3:
                if (mGoogleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                }
                Intent j = new Intent(AccountPageActivity.this, SocialNetworkActivity.class);
                startActivity(j);
                break;


        }
    }*/


    /*@Override
    public void onClick(View view) {
        // ...

        if (view.getId() == R.id.sign_out_button) {
            onSignOutClicked();
        }
    }*/

    public void onSignOutClicked(View view)
    {
        // Clear the default account so that GoogleApiClient will not automatically
        // connect in the future.
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }

        Intent k = new Intent(AccountPageActivity.this, AccountListActivity.class);
        startActivity(k);
    }

    public void backToSN1(View view)
    {

        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }
        Intent j = new Intent(AccountPageActivity.this, SocialNetworkActivity.class);
        startActivity(j);
    }

    public void backToAccountListforSignOut(View view)
    {

        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }


        Intent k = new Intent(AccountPageActivity.this, AccountListActivity.class);
        startActivity(k);
    }
}

