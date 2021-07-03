package com.riteshknayak.masterq;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.messaging.FirebaseMessaging;
import com.riteshknayak.masterq.adapters.ViewPagerAdapter;
import com.riteshknayak.masterq.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MasterQ);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewPager2 = binding.content;
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                switch (position) {
                    case 0:
                        binding.bottomBar.setItemActiveIndex(0);
                        break;
                    case 1:
                        binding.bottomBar.setItemActiveIndex(1);
                        break;
                    case 2:
                        binding.bottomBar.setItemActiveIndex(2);
                        break;
                }
            }
        });

        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(adapter);

        binding.bottomBar.setOnItemSelectedListener(i -> {
            FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
            switch (i) {
                case 0:
                    viewPager2.setCurrentItem(0, true);  // false value will prevent effects while bottom menu is pressed;
                    transaction1.commit();
                    break;
                case 1:
                    viewPager2.setCurrentItem(1, true);  // false value will prevent effects while bottom menu is pressed
                    transaction1.commit();
                    break;
                case 2:
                    viewPager2.setCurrentItem(2, true);  // false value will prevent effects while bottom menu is pressed
                    transaction1.commit();
                    break;
            }
            return false;
        });

        FirebaseMessaging.getInstance().subscribeToTopic("general");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel("notifications", "notifications", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}