package com.riteshknayak.masterq;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.riteshknayak.masterq.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {
    String mName;
    Integer mScore;
    FirebaseFirestore database;
    FirebaseAuth auth;
    String UId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);


        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        auth = FirebaseAuth.getInstance();
        UId = auth.getCurrentUser().getUid();

        database.collection("users")
                .document(UId)
                .get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.getString("name") != null) {
                mName = documentSnapshot.getString("name");
            }
            if (documentSnapshot.get("score") != null) {
                Long s = (long) documentSnapshot.get("score");
                mScore = s.intValue();
            }
            binding.name.setText(mName);
            if (mScore != null) {
                binding.score.setText(mScore.toString());
            }
        });

        binding.addProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileFragment.this.getActivity(), SettingActivity.class);
            startActivity(intent);
        });
        return binding.getRoot();
    }
}