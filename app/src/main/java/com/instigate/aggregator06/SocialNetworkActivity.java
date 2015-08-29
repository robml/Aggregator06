package com.instigate.aggregator06;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;

import java.util.HashMap;
import java.util.Timer;
import java.util.logging.Handler;

public class SocialNetworkActivity extends AppCompatActivity implements
        ConnectionCallbacks, OnConnectionFailedListener, View.OnClickListener,ResultCallback<People.LoadPeopleResult>
{

    //public static HashMap BasicDetails;

    private static final String TAG = "Social Network Activity";

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    /* A flag indicating that a PendingIntent is in progress and prevents
     * us from starting further intents.
     */
    //private boolean mIntentInProgress;

    /* Is there a ConnectionResult resolution in progress? */
    //private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;

    private static final int STATE_SIGNED_IN = 0;
    private static final int STATE_SIGN_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;
    private int mSignInProgress;

    private PendingIntent mSignInIntent;
    private int mSignInError;

    private static final int DIALOG_PLAY_SERVICES_ERROR = 0;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_network);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PLUS_ME))
                .addScope(new Scope(Scopes.PLUS_LOGIN))
                .build();
        mGoogleApiClient.disconnect();

        this.findViewById(R.id.sign_in_button).setOnClickListener(this);
        ((SignInButton) findViewById(R.id.sign_in_button)).setSize(SignInButton.SIZE_WIDE);

    }

    protected void onStart() {
        super.onStart();
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
        else
        {
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }
    }

    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        // onConnected indicates that an account was selected on the device, that the selected
        // account has granted any requested permissions to our app and that we were able to
        // establish a service connection to Google Play services.
        Log.d(TAG, "onConnected");

        //if ((Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) == null)) //|| (bundle == null))
        //{
        //    onStart();
        //} else {


        Plus.PeopleApi.loadVisible(mGoogleApiClient, null).setResultCallback(this);

            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                Person.Image personPhoto = currentPerson.getImage();
                String personGooglePlusProfile = currentPerson.getUrl();


                /*BasicDetails.put("currentPersonName", personName);
                BasicDetails.put("currentPersonImage", personPhoto);
                BasicDetails.put("currentPersonGooglePlusProfile", personGooglePlusProfile);*/

                Intent intent = new Intent(this, AccountPageActivity.class);
                intent.putExtra("currentName", personName);
                startActivity(intent);

            }


        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                // Show the signed-in UI
                Intent k = new Intent(SocialNetworkActivity.this, AccountPageActivity.class);
                startActivity(k);
            }
        };
        new Thread(runnable).start();


            mShouldResolve = false;

            mSignInProgress = STATE_SIGNED_IN;




    }
  //  }


    // Code in case second one doesn't work

    @Override
public void onConnectionFailed(ConnectionResult connectionResult) {
    // Could not connect to Google Play Services.  The user needs to select an account,
    // grant permissions or resolve an error in order to sign in. Refer to the javadoc for
    // ConnectionResult to see possible error codes.
    Log.d(TAG, "onConnectionFailed:" + connectionResult);

        //TEST 1

    /*if (!mIsResolving && mShouldResolve) {
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
    } else {
        Intent refresh = new Intent(this, SocialNetworkActivity.class);
        startActivity(refresh);
    }*/

        if (mSignInProgress != STATE_IN_PROGRESS)
        {
            mSignInIntent = connectionResult.getResolution();
            mSignInError = connectionResult.getErrorCode();

            if (mSignInProgress == STATE_SIGNED_IN)
            {
                resolveSignInError();
            }

        }


}

    public void resolveSignInError()
    {
        if (mSignInIntent != null) {
            try {
                mSignInProgress = STATE_IN_PROGRESS;
                startIntentSenderForResult(mSignInIntent.getIntentSender(), RC_SIGN_IN, null, 0, 0, 0);

            } catch (IntentSender.SendIntentException e) {
                Log.i(TAG, "Sign in intent could not be sent: " + e.getLocalizedMessage());
                mSignInProgress = STATE_SIGN_IN;
                mGoogleApiClient.connect();
            }
        }
        else
            {
                showDialog(DIALOG_PLAY_SERVICES_ERROR);
            }
    }

    public void showErrorDialog(ConnectionResult error)
    {
        //Toast shown for  short period of time
        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();

    }
/*

    public void onConnectionFailed(ConnectionResult result) {
        if (!mIntentInProgress && result.hasResolution()) {
            try {
                mIntentInProgress = true;
                startIntentSenderForResult(result.getResolution().getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }*/

    /*protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            //mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }*/

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }

            mIsResolving = false;
            mGoogleApiClient.connect();
        }
    }*/


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch(requestCode)
        {
            case RC_SIGN_IN:
                if (resultCode == RESULT_OK)
                {
                    mSignInProgress = STATE_SIGN_IN;
                }
                else
                {
                    mSignInProgress = STATE_SIGNED_IN;
                }

                if (!mGoogleApiClient.isConnecting())
                {
                    mGoogleApiClient.connect();
                }
                break;


        }
    }

    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sign_in_button) {
            //onSignInClicked();
            mGoogleApiClient.connect();
            mShouldResolve=true;
        }

    }

    /* I find this unnecesary

    private void onSignInClicked() {
        // User clicked the sign-in button, so begin the sign-in process and automatically
        // attempt to resolve any errors that occur.
        //mShouldResolve = true;
        mGoogleApiClient.connect();

        // Show a message to the user that we are signing in.
        //mStatusTextView.setText(R.string.signing_in);
    }*/

    public void backToMainPage(View view) {
        Intent j = new Intent(SocialNetworkActivity.this, MainActivity.class);
        startActivity(j);
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
}
