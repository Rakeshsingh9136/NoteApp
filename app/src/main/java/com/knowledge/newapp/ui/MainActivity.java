package com.knowledge.newapp.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;

import com.knowledge.newapp.R;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private View bottomMenu;
    private LinearLayout menuHome, menuSearch, menuNotifications, menuProfile;
    private NavController navController;

    // Fragments where bottom menu is visible
    private final Set<Integer> fragmentsWithMenu = new HashSet<>(Arrays.asList(
            R.id.AddNote,
            R.id.TaskManager,
            R.id.Reminder,
            R.id.Insights
    ));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "TASK_REMINDER_CHANNEL",
                    "Task Reminders",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Channel for Task reminders");
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) manager.createNotificationChannel(channel);
        }


        bottomMenu = findViewById(R.id.bottomMenu);
        menuHome = findViewById(R.id.menuHome);
        menuSearch = findViewById(R.id.menuSearch);
        menuNotifications = findViewById(R.id.menuNotifications);
        menuProfile = findViewById(R.id.menuProfile);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        // Show/hide bottom menu depending on fragment
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller,
                                             @NonNull NavDestination destination,
                                             @Nullable Bundle arguments) {
                if (fragmentsWithMenu.contains(destination.getId())) {
                    showBottomMenu();
                } else {
                    hideBottomMenu();
                }
            }
        });

        // Click listeners for BottomMenu
        menuHome.setOnClickListener(v -> navigateToFragment(R.id.AddNote));
        menuSearch.setOnClickListener(v -> navigateToFragment(R.id.TaskManager));
        menuNotifications.setOnClickListener(v -> navigateToFragment(R.id.Reminder));
        menuProfile.setOnClickListener(v -> navigateToFragment(R.id.Insights));
    }

    private void navigateToFragment(int fragmentId) {
        // Only navigate if we are not already on the target fragment
        if (navController.getCurrentDestination() != null &&
                navController.getCurrentDestination().getId() != fragmentId) {
            navController.navigate(fragmentId);
        }
    }

    private void showBottomMenu() {
        if (bottomMenu.getVisibility() != View.VISIBLE) {
            bottomMenu.setVisibility(View.VISIBLE);
            bottomMenu.setAlpha(0f);
            bottomMenu.animate().alpha(1f).setDuration(200).start();
        }
    }

    private void hideBottomMenu() {
        if (bottomMenu.getVisibility() == View.VISIBLE) {
            bottomMenu.animate().alpha(0f).setDuration(200).withEndAction(() ->
                    bottomMenu.setVisibility(View.GONE)).start();
        }
    }
}
