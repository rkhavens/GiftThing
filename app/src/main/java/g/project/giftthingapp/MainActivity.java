package g.project.giftthingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.Toast;

import g.project.giftthingapp.dummy.DummyContent;

public class MainActivity extends AppCompatActivity
        implements fWishlistList.OnListFragmentInteractionListener,
                    fHomeList.OnListFragmentInteractionListener,
                    fFriendList.OnListFragmentInteractionListener,
                    fWishlist.OnListFragmentInteractionListener,
                    NavigationView.OnNavigationItemSelectedListener {


    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
    //defaults

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //setActionBarTitle("Boo");
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
            Context context = getApplicationContext();
            CharSequence text = "Settings Clicked!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();


            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FrameLayout frag_frame = (FrameLayout) findViewById(R.id.fragment_place);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (id == R.id.nav_home) {
            ft.replace(R.id.fragment_place, new fHomeList());
            ft.commit();
            getSupportActionBar().setTitle("Home");
        } else if (id == R.id.nav_profile) {
            ft.replace(R.id.fragment_place, new fProfile());
            ft.commit();
            getSupportActionBar().setTitle("My Profile");
        } else if (id == R.id.nav_friends) {
            ft.replace(R.id.fragment_place, new fFriendList());
            ft.commit();
            getSupportActionBar().setTitle("Friends List");
        } else if (id == R.id.nav_wishlists) {
            ft.replace(R.id.fragment_place, new fWishlist());
            ft.commit();
            getSupportActionBar().setTitle("My Wishlists");
        } else if (id == R.id.nav_manage) {
            //getSupportActionBar().setTitle("Settings");
            Context context = getApplicationContext();
            CharSequence text = "Settings Clicked!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));

        } else if (id == R.id.nav_share) {
            //getSupportActionBar().setTitle("Share");
            Context context = getApplicationContext();
            CharSequence text = "Share Clicked!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else if (id == R.id.nav_send) {
            //getSupportActionBar().setTitle("Send");
            Context context = getApplicationContext();
            CharSequence text = "Send Clicked!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
