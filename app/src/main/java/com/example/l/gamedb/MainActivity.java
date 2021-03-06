package com.example.l.gamedb;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.l.gamedb.data.GameContract;
import com.example.l.gamedb.view.FavoriteFragment;
import com.example.l.gamedb.view.GamesFragment;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener, LoaderCallbacks{

    String action = "", ON_SAVE_INSTANCE_STATE = "state";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if(savedInstanceState != null){
            action = savedInstanceState.getString(ON_SAVE_INSTANCE_STATE);
        }else {
            action = "games";
        }
        changeFragment(action);


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ON_SAVE_INSTANCE_STATE, action);
    }

    public void changeFragment(String argument){
        Bundle bundle = new Bundle();
        bundle.putString("key", argument);
        GamesFragment gamesFragment = new GamesFragment();
        gamesFragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_framelayout, gamesFragment, "tag");
        fragmentTransaction.commit();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search..");
        searchView.setOnQueryTextListener(this);
        //searchView.setIconified(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorites) {
            getSupportLoaderManager().initLoader(1, null, this);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id){
            case R.id.companies:
                action = "companies";
                changeFragment(action);
                break;
            case R.id.franchises:
                action = "franchises";
                changeFragment(action);
                break;
            case R.id.pages:
                action = "pages";
                changeFragment(action);
                break;
            case R.id.reviews:
                action = "reviews";
                changeFragment(action);
                break;
            case R.id.action:
                action = "action";
                changeFragment(action);
                break;
            case R.id.historical:
                action = "historical";
                changeFragment(action);
                break;
            case R.id.horror:
                action = "horror";
                changeFragment(action);
                break;
            case R.id.drama:
                action = "drama";
                changeFragment(action);
                break;
            case R.id.mystery:
                action = "mystery";
                changeFragment(action);
                break;
            case R.id.single_player:
                action = "singlePlayer";
                changeFragment(action);
                break;
            case R.id.multi_player:
                action = "multiPlayer";
                changeFragment(action);
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        changeFragment(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @NonNull
    @Override
    public android.support.v4.content.Loader onCreateLoader(int id, @Nullable Bundle args) {
        Uri CONTENT_URI = GameContract.GameEntry.CONTENT_URI;
        CursorLoader cursorLoader = new CursorLoader(this, CONTENT_URI, null, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader loader, Object cursor) {
        if(cursor != null) {
            FavoriteFragment favoriteFragment = FavoriteFragment.createYourFragmentWithCursor((Cursor) cursor);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_framelayout, favoriteFragment, "tag");
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader loader) {

    }
}
