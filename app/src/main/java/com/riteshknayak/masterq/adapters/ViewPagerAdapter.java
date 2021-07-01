package com.riteshknayak.masterq.adapters;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.annotation.NonNull;

import com.riteshknayak.masterq.HomeFragment;
import com.riteshknayak.masterq.LeaderboardFragment;
import com.riteshknayak.masterq.ProfileFragment;
import com.riteshknayak.masterq.databinding.ActivityMainBinding;

public class ViewPagerAdapter extends FragmentStateAdapter {

    ActivityMainBinding binding;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity)
    {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 1:
                return  new LeaderboardFragment();
            case 2:
                return  new ProfileFragment();
            default:
                return new HomeFragment();
        }
    }
    @Override
    public int getItemCount() {return 3; }
}