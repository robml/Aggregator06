package com.instigate.aggregator06;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;


public class AccountListActivity extends AppCompatActivity {

    public HashMap accountList = new HashMap();

    private static final String TAG = "AccountListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_list);

        TextView emptyview = (TextView) findViewById(R.id.empty);
        ListView accountslist=(ListView) findViewById(R.id.list);
        emptyview.setVisibility(View.VISIBLE);
        accountslist.setVisibility(View.GONE);


        /*if (accountList.length() == 0)
        {
            emptyview.setVisibility(View.GONE);
            accountslist.setVisibility(View.VISIBLE);
        }
        else
        {
            accountslist.setVisibility(View.VISIBLE);
            emptyview.setVisibility(View.GONE);
        } */

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account_list, menu);
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

    public void goToSelectSNPage(View view)
    {
        Intent k = new Intent(AccountListActivity.this,SocialNetworkActivity.class);
        startActivity(k);
    }

    public void backToMain(View view)
    {
        Intent l = new Intent(AccountListActivity.this,MainActivity.class);
        startActivity(l);
    }

}
