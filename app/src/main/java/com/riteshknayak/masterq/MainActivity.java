package com.riteshknayak.masterq;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.riteshknayak.masterq.databinding.ActivityMainBinding;



public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, new HomeFragment());
        transaction.commit();

        binding.bottomBar.setOnItemSelectedListener(i -> {
            FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
            switch (i) {
                case 0:
                    transaction1.replace(R.id.content, new HomeFragment());
                    transaction1.commit();
                    break;
                case 1:
                    transaction1.replace(R.id.content, new LeaderboardFragment());
                    transaction1.commit();
                    break;
                case 2:
                    transaction1.replace(R.id.content, new ProfileFragment());
                    transaction1.commit();
                    break;
            }
            return false;
        });

    }
}