package com.instigate.aggregator06;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToAccountListPage(View view)
    {
        Intent n = new Intent(MainActivity.this,AccountListActivity.class);
        startActivity(n);
    }

    public void snTest(View view)
    {
        Intent n = new Intent(MainActivity.this,SelectSNActivity.class);
        startActivity(n);
    }


   /* This code is here for testing purposes

    public void goToSNPage(View view)

    {
        Intent l = new Intent(MainActivity.this,SelectSNActivity.class);
        startActivity(l);
    }
    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

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
}
