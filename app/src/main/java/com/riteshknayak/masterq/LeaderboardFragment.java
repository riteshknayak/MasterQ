package com.riteshknayak.masterq;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.riteshknayak.masterq.adapters.LeaderboardsAdapter;
import com.riteshknayak.masterq.databinding.FragmentLeaderboardBinding;
import com.riteshknayak.masterq.objects.User;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogAnimation;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.util.ArrayList;

public class LeaderboardFragment extends Fragment {

    FirebaseFirestore database;
    FirebaseAuth auth;
    String uid;

    public LeaderboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkIfOnline();
    }

    FragmentLeaderboardBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLeaderboardBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        uid = auth.getUid();
        database = FirebaseFirestore.getInstance();

        final ArrayList<User> users = new ArrayList<>();
        final LeaderboardsAdapter adapter = new LeaderboardsAdapter(getContext(), users);

        binding.leaderboardRecyclerView.setAdapter(adapter);
        binding.leaderboardRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        database.collection("users")
                .orderBy("score", Query.Direction.DESCENDING)
                .limit(100)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for(DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        User user = snapshot.toObject(User.class);
                        users.add(user);
                    }
                    adapter.notifyDataSetChanged();
                });

        final ArrayList<User> leaders = new ArrayList<>();
        database.collection("users")
                .orderBy("score", Query.Direction.DESCENDING)
                .limit(3)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for(DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        User user = snapshot.toObject(User.class);
                        leaders.add(user);
                    }
                    if (leaders.size() > 0){
                        if (isAdded()){
                            Glide.with(requireContext())
                                    .load(leaders.get(0).getImageUrl())
                                    .into(binding.leaderOne);
                        }
                        binding.l1Name.setText(String.valueOf(leaders.get(0).getName()));
                        binding.l1Score.setText(String.valueOf(leaders.get(0).getScore()));
                    }
                    if (leaders.size() > 1){
                        if (isAdded()){
                            Glide.with(requireContext())
                                    .load(leaders.get(1).getImageUrl())
                                    .into(binding.leaderTwo);
                        }
                        binding.l2Name.setText(String.valueOf(leaders.get(1).getName()));
                        binding.l2Score.setText(String.valueOf(leaders.get(1).getScore()));
                    }
                    if (leaders.size() > 2){
                        if (isAdded()){
                            Glide.with(requireContext())
                                    .load(leaders.get(2).getImageUrl())
                                    .into(binding.leaderThree);

                        }
                        binding.l3Name.setText(String.valueOf(leaders.get(2).getName()));
                        binding.l3Score.setText(String.valueOf(leaders.get(2).getScore()));
                    }
                    adapter.notifyDataSetChanged();
                });

        database.collection("users")
                .document(uid)
            .get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.getString("name") != null) {
                    String mName = documentSnapshot.getString("name");
                    binding.name.setText(mName);
                }
                if (documentSnapshot.get("score") != null) {
                    long s = (long) documentSnapshot.get("score");
                    int mScore = (int)s;
                    binding.score.setText(String.valueOf(mScore));
                    database.collection("users")
                            .whereGreaterThan("score",mScore)
                            .get().addOnSuccessListener(  Q ->{
                            int position = Q.size()+1;
                        binding.index.setText("#".concat(String.valueOf(position)));
                    });

                }
                if (documentSnapshot.getString("imageUrl") != null ){
                    if (isAdded()){
                        Glide.with(requireContext())
                                .load(documentSnapshot.getString("imageUrl"))
                                .into(binding.lProfileImage);

                    }
                }
            });
        return binding.getRoot();
    }

    void checkIfOnline() {
        if (!isConnected() && isAdded()) {
            new AestheticDialog.Builder(getActivity(), DialogStyle.CONNECTIFY, DialogType.ERROR)
                    .setTitle("NO CONNECTION FOUND!")
                    .setMessage("Check your Internet connection")
                    .setCancelable(true)
                    .setDarkMode(true)
                    .setGravity(Gravity.TOP)
                    .setAnimation(DialogAnimation.SWIPE_RIGHT)
                    .show();
        }
    }

    public boolean isConnected() {
        boolean connected;
        if (isAdded()) {
            try {
                ConnectivityManager cm = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo nInfo = cm.getActiveNetworkInfo();
                connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
                return connected;
            } catch (Exception e) {
                Log.e("Connectivity Exception", e.getMessage());
            }
        }
        return false;
    }
}