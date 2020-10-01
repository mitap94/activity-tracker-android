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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.petrovic.m.dimitrije.activitytracker.data.model.LoggedInUser;
import com.petrovic.m.dimitrije.activitytracker.databinding.ActivityMainBinding;
import com.petrovic.m.dimitrije.activitytracker.fit.StepCounterService;
import com.petrovic.m.dimitrije.activitytracker.rest.APIUtils;
import com.petrovic.m.dimitrije.activitytracker.ui.activities.ActivitiesFragment;
import com.petrovic.m.dimitrije.activitytracker.ui.login.LoginActivity;
import com.petrovic.m.dimitrije.activitytracker.ui.me.FoodFragment;
import com.petrovic.m.dimitrije.activitytracker.ui.me.ItemsFragment;
import com.petrovic.m.dimitrije.activitytracker.ui.me.MeFragment;
import com.petrovic.m.dimitrije.activitytracker.ui.meals.MealsFragment;
import com.petrovic.m.dimitrije.activitytracker.utils.Utils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ItemsFragment.OnFoodSelectedListener, MealsFragment.OnAddFoodSelectedListener {

    private static final String LOG_TAG = Utils.getLogTag(MainActivity.class);


    private ActivityMainBinding binding;

    private AppBarConfiguration appBarConfiguration;
    private NavController navController;
    private View headerView;
    private MainViewModel viewModel;

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
                .setOpenableLayout(binding.drawerLayout)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.drawerView, navController);
        NavigationUI.setupWithNavController(binding.navMain.bottomNavView, navController);

        headerView = binding.drawerView.getHeaderView(0);

        headerView.setOnClickListener(v -> {
            Log.d(LOG_TAG, "Header view clicked");
            if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            }
            Bundle bundle = new Bundle();
            bundle.putInt(MeFragment.INIT_FRAGMENT, MeFragment.FRAGMENT_POSITION.PROFILE_FRAGMENT.ordinal()); // show the profile fragment
            navController.navigate(R.id.navigation_me, bundle);
        });

        binding.drawerView.setNavigationItemSelectedListener(this);

        viewModel = new ViewModelProvider(this, new MainViewModelFactory(getApplication())).get(MainViewModel.class);

        viewModel.getSessionManagerMutableLiveData().observe(this, sessionManager -> {
            Log.d(LOG_TAG, "loadHeaderInfo");
            if (sessionManager.getUser() != null) {
                loadHeaderInformation(sessionManager.getUser());
            } else { // logout
                APIUtils.getGoogleClient(this).signOut().addOnCompleteListener(this,
                        task -> {
                            Intent loginActivityIntent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(loginActivityIntent);
                            finish();

                            Toast.makeText(getApplicationContext(), "Signed out!", Toast.LENGTH_LONG).show();
                        });
            }
        });

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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(LOG_TAG, "onNavigationItemSelected id " + item.getItemId());
        if (item.getItemId() == R.id.navigation_logout) {
            viewModel.logout();
            return true;
        } else {
            navController.navigate(item.getItemId());
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void loadHeaderInformation(LoggedInUser user) {
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

    @Override
    public void onFoodSelected() {
        navController.navigate(R.id.navigation_my_food);
    }

    @Override
    public void onAddFoodSelected() {
        navController.navigate(R.id.navigation_daily_food);
    }
}