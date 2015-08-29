package com.instigate.aggregator06;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.*;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
/**
 * Minimal activity demonstrating basic Google Sign-In.
 */
public class SelectSNActivity extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    private static final String TAG = "SelectSNActivity";
    /* RequestCode for resolutions involving sign-in */
    private static final int RC_SIGN_IN = 0;
    /* Keys for persisting instance variables in savedInstanceState */
    private static final String KEY_IS_RESOLVING = "is_resolving";
    private static final String KEY_SHOULD_RESOLVE = "should_resolve";
    /* Client for accessing Google APIs */
    private GoogleApiClient mGoogleApiClient;

    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;
    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sn);

        findViewById(R.id.googleSignInBtn).setOnClickListener(this);


// Restore from saved instance state
// [START restore_saved_instance_state]
        if (savedInstanceState != null) {
            mIsResolving = savedInstanceState.getBoolean(KEY_IS_RESOLVING);
            mShouldResolve = savedInstanceState.getBoolean(KEY_SHOULD_RESOLVE);
        }
// [END restore_saved_instance_state]
// Set up button click listeners
        findViewById(R.id.googleSignInBtn).setOnClickListener(this);

// Large sign-in
        ((SignInButton) findViewById(R.id.googleSignInBtn)).setSize(SignInButton.SIZE_WIDE);
// Start with sign-in button disabled until sign-in either succeeds or fails
        findViewById(R.id.googleSignInBtn).setEnabled(true);

// [START create_google_api_client]
// Build GoogleApiClient with access to basic profile
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .build();
// [END create_google_api_client]
    }

    // [START on_start_on_stop]
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }
    @Override
    protected void onStop() {
        super.onStop();
        if(mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
    // [END on_start_on_stop]

    public void goToSignInPage(View view)
    {
        Intent j = new Intent(SelectSNActivity.this, SignInActivity.class);
        startActivity(j);
    }

    public void backToAccountList(View view)
    {
        Intent j = new Intent(SelectSNActivity.this, AccountListActivity.class);
        startActivity(j);
    }

// [START on_save_instance_state]
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IS_RESOLVING, mIsResolving);
        outState.putBoolean(KEY_SHOULD_RESOLVE, mIsResolving);
    }
    // [END on_save_instance_state]
// [START on_activity_result]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);
        if (requestCode == RC_SIGN_IN) {
// If the error resolution was not successful we should not resolve further errors.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }
            mIsResolving = false;
            mGoogleApiClient.connect();
        }
    }
    // [END on_activity_result]
    @Override
    public void onConnected(Bundle bundle) {
// onConnected indicates that an account was selected on the device, that the selected
// account has granted any requested permissions to our app and that we were able to
// establish a service connection to Google Play services.
        Log.d(TAG, "onConnected:" + bundle);
// Show the signed-in UI
        //updateUI(true);

        //Intent d = new Intent(SelectSNActivity.this, AccountPageActivity.class);
        //startActivity(d);
    }
    @Override
    public void onConnectionSuspended(int i) {
// The connection to Google Play services was lost. The GoogleApiClient will automatically
// attempt to re-connect. Any UI elements that depend on connection to Google APIs should
// be hidden or disabled until onConnected is called again.
        Log.w(TAG, "onConnectionSuspended:" + i);
    }
    // [START on_connection_failed]
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
// Could not connect to Google Play Services.  The user needs to select an account,
// grant permissions or resolve an error in order to sign in. Refer to the javadoc for
// ConnectionResult to see possible error codes.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            } else {
// Could not resolve the connection result, show the user an
// error dialog.
                showErrorDialog(connectionResult);
            }
        }
    }
    // [END on_connection_failed]
    private void showErrorDialog(ConnectionResult connectionResult) {
        int errorCode = connectionResult.getErrorCode();
        if (GooglePlayServicesUtil.isUserRecoverableError(errorCode)) {
// Show the default Google Play services error dialog which may still start an intent
// on our behalf if the user can resolve the issue.
            GooglePlayServicesUtil.getErrorDialog(errorCode, this, RC_SIGN_IN,
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            mShouldResolve = false;

                        }
                    }).show();
        } else {
// No default Google Play Services error, display a message to the user.
            String errorString = getString(R.string.play_services_error_fmt, errorCode);
            Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show();
            mShouldResolve = false;

        }
    }
    /*@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.googleSignInBtn:
// User clicked the sign-in button, so begin the sign-in process and automatically
// attempt to resolve any errors that occur.
                Intent d = new Intent(SelectSNActivity.this, AccountPageActivity.class);
                startActivity(d);
                //mStatus.setText(R.string.signing_in);
// [START sign_in_clicked]
                mShouldResolve = true;
                mGoogleApiClient.connect();
// [END sign_in_clicked]
                break;
        }
    }*/

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.googleSignInBtn) {
            onSignInClicked();

            //Intent d = new Intent(SelectSNActivity.this, AccountPageActivity.class);
            //startActivity(d);
        }

        // ...
    }

    private void onSignInClicked() {
        // User clicked the sign-in button, so begin the sign-in process and automatically
        // attempt to resolve any errors that occur.
        mShouldResolve = true;
        mGoogleApiClient.connect();

        //Intent d = new Intent(SelectSNActivity.this, AccountPageActivity.class);
        //startActivity(d);
    }

}