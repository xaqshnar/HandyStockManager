package com.divani.android.handystockmanager;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.divani.android.handystockmanager.database.Product_Type;
import com.divani.android.handystockmanager.database.StockData;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        BuyFragment.OnFragmentInteractionListener, SellFragment.OnFragmentInteractionListener,
        ViewFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener,
        ContactFragment.OnFragmentInteractionListener, AddFragment.OnFragmentInteractionListener {

    private StockData dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create the database
        openConnection();
        insertDummyData();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //add this line to display menu1 when the activity is loaded
        displaySelectedScreen(R.id.nav_home);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if(dbHelper != null)
            dbHelper.closeConnection();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        displaySelectedScreen(item.getItemId());

        return true;
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;
        Class fragmentClass;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_buy_stock:
                fragmentClass = BuyFragment.class;
                break;
            case R.id.nav_sell_stock:
                fragmentClass = SellFragment.class;
                break;
            case R.id.nav_view_stock:
                fragmentClass = ViewFragment.class;
                break;
            case R.id.nav_contact:
                fragmentClass = ContactFragment.class;
                break;
            case R.id.nav_add:
                fragmentClass = AddFragment.class;
                break;
            default:
                fragmentClass = HomeFragment.class;
                break;
        }


        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void openConnection() {

        Log.d("Opening: ", "Opening connection ..");
        dbHelper = StockData.getInstance(this);
    }

    public void insertDummyData() {

        //insert dummy values
        Log.d("Inserting: ", "Inserting data ..");
        dbHelper.addProduct_Type(new Product_Type("Mobile"));
        dbHelper.addProduct_Type(new Product_Type("TV"));
        dbHelper.addProduct_Type(new Product_Type("Laptop"));
        dbHelper.addProduct_Type(new Product_Type("Tablet"));
    }

}
