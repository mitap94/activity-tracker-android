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
import com.petrovic.m.dimitrije.activitytracker.fit.StepCounterService;
import com.petrovic.m.dimitrije.activitytracker.rest.SessionManager;
import com.petrovic.m.dimitrije.activitytracker.ui.activities.ActivitiesFragment;
import com.petrovic.m.dimitrije.activitytracker.ui.login.LoginActivity;
import com.petrovic.m.dimitrije.activitytracker.ui.me.MeFragment;
import com.petrovic.m.dimitrije.activitytracker.utils.Utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.Navigator;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = Utils.getLogTag(MainActivity.class);

    private AppBarConfiguration appBarConfiguration;

    private ActivityMainBinding binding;

    NavController navController;
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
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.drawerView, navController);
        NavigationUI.setupWithNavController(binding.navMain.bottomNavView, navController);

        sessionManager = SessionManager.getInstance(this);
        user = sessionManager.getUser();

        // Populate header with user info
        if (user != null) {
            loadHeaderInformation();
        }

        // Start ActivitiesFragment if clicked from service
        Intent intent = getIntent();
        if (intent != null) {
            handleIntent(intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(LOG_TAG, "onStart");

        Intent startServiceIntent = new Intent(this, StepCounterService.class);
        ContextCompat.startForegroundService(this, startServiceIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "onResume");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(LOG_TAG, "onCreateOptionsMenu");

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

        profilePicture.setOnClickListener(v -> {
            Log.d(LOG_TAG, "Profile picture clicked");
            if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            }
            Bundle bundle = new Bundle();
            bundle.putInt(MeFragment.INIT_FRAGMENT, 2); // init the profile fragment
            navController.navigate(R.id.navigation_me, bundle);
        });

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

    private void handleIntent(Intent intent) {
        Log.d(LOG_TAG, "intent action = " + intent.getAction());
        String fragmentExtra = intent.getStringExtra(StepCounterService.FRAGMENT_EXTRA);
        Log.d(LOG_TAG, "fragment extra = " + fragmentExtra);
        if (fragmentExtra != null) {
            if (fragmentExtra.equals(ActivitiesFragment.class.getSimpleName())) {
                Log.d(LOG_TAG, "Start ActivitiesFragment");
                navController.navigate(R.id.navigation_activities);
            }
        }
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