package com.sem7project.sehatmitr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    // user data varibles
    private String recievedUID;
    private String title = "";

    // ui elements variables (default private)
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recievedUID = getIntent().getExtras().getString("uid");
        Toast.makeText(this, "Welcome, UID : " + recievedUID, Toast.LENGTH_SHORT).show();

        // Initialize the drawer and toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);  // Set the Toolbar as ActionBar

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        // Optionally, enable the home button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);  // Enable the Up button
            actionBar.setTitle("Home");
        }

        // Load the default fragment (HomeFragment)
//        if (savedInstanceState == null) {
//            loadFragment(new HomeFragment());
//        }
        loadFragment(new HomeFragment());  // default fragment

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch(item.getItemId())
                {
                    case R.id.home:
                    {
                        selectedFragment = new HomeFragment();
                        title = "Home";
                        break;
                    }
                    case R.id.locate_hospitals:
                    {
                        selectedFragment = new LocateHospitalsFragment();
                        title = "Locate Hospitals";
                        break;
                    }
                    case R.id.chatbot:
                    {
                        Toast.makeText(MainActivity.this, "Chatbot", Toast.LENGTH_SHORT).show();
                        title = "ChatBot";
                        break;
                    }
                    case R.id.add_info:
                    {
                        Toast.makeText(MainActivity.this, "Information page", Toast.LENGTH_SHORT).show();
                        title = "Additional Information";
                        break;
                    }
                    case R.id.about:
                    {
                        Toast.makeText(MainActivity.this, "About page", Toast.LENGTH_SHORT).show();
                        title = "About";
                        break;
                    }
                    case R.id.logout:
                    {
//                        Toast.makeText(MainActivity.this, "Logout process", Toast.LENGTH_SHORT).show();
                        confirmAndLogOut();
                        break;
                    }
                    case R.id.share:
                    {
                        Toast.makeText(MainActivity.this, "Share", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.rate_us:
                    {
                        Toast.makeText(MainActivity.this, "Rate us", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                    if(getSupportActionBar() != null){
                        getSupportActionBar().setTitle(title);
                    }
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
//                return false;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed(){
        // asks for confirmation to exit
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            new AlertDialog.Builder(this)
                    .setTitle("Exit Sehat Mitr")
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Exit app if user clicks yes
                            finishAffinity();  // exits and closes all activities
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

    void confirmAndLogOut(){
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Clear activity stack and navigate to login activity
                        Intent intent = new Intent(MainActivity.this, LoginPage.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();  // Removing mainactivity from memory
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}