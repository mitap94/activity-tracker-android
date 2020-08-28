package com.petrovic.m.dimitrije.activitytracker;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.petrovic.m.dimitrije.activitytracker.data.model.LoggedInUser;
import com.petrovic.m.dimitrije.activitytracker.data.pojo.User;
import com.petrovic.m.dimitrije.activitytracker.databinding.ActivityMainBinding;
import com.petrovic.m.dimitrije.activitytracker.databinding.DrawerHeaderBinding;
import com.petrovic.m.dimitrije.activitytracker.rest.SessionManager;
import com.petrovic.m.dimitrije.activitytracker.ui.login.LoginActivity;
import com.petrovic.m.dimitrije.activitytracker.utils.Utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = Utils.getLogTag(MainActivity.class);

    private AppBarConfiguration appBarConfiguration;

    private ActivityMainBinding binding;

    private SessionManager sessionManager;
    private LoggedInUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(LOG_TAG, "onCreate");

        // View binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.navMain.appBar.toolbar);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_meals, R.id.navigation_activities, R.id.navigation_me)
                .setDrawerLayout(binding.drawerLayout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.drawerView, navController);
        NavigationUI.setupWithNavController(binding.navMain.bottomNavView, navController);

        sessionManager = SessionManager.getInstance(this);
        user = sessionManager.getUser();

        if (user != null) {
            loadHeaderInformation();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            /*case R.id.new_game:
                newGame();
                return true;
            case R.id.help:
                showHelp();
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void loadHeaderInformation() {
        View headerView = binding.drawerView.getHeaderView(0);

        // load profile picture
        ImageView profilePicture = (ImageView) headerView.findViewById(R.id.profile_picture);
        String profilePictureUrl = user.getUser().getProfilePicture();

        if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
            Glide.with(this).asBitmap().load(profilePictureUrl).centerCrop().into(profilePicture);
        } else {
            profilePicture.setImageResource(R.mipmap.ic_launcher_round);
        }

        // load name
        TextView name = (TextView) headerView.findViewById(R.id.name);
        name.setText(user.getUser().getName());

        // load email
        TextView email = (TextView) headerView.findViewById(R.id.email);
        email.setText(user.getUser().getEmail());

    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

}