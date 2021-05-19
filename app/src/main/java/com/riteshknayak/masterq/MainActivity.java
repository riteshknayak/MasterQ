package com.riteshknayak.masterq;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.riteshknayak.masterq.databinding.ActivityMainBinding;


import me.ibrahimsn.lib.OnItemSelectedListener;

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

        binding.bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (i) {
                    case 0:
                        transaction.replace(R.id.content, new HomeFragment());
                        transaction.commit();
                        break;
                    case 1:
                        transaction.replace(R.id.content, new LeaderboardFragment());
                        transaction.commit();
                        break;
                    case 2:
                        transaction.replace(R.id.content, new ProfileFragment());
                        transaction.commit();
                        break;
                }
                return false;
            }
        });

    }
}