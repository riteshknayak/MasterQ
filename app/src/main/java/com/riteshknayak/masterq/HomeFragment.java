package com.riteshknayak.masterq;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.riteshknayak.masterq.adapters.CategoryAdapter;
import com.riteshknayak.masterq.databinding.FragmentHomeBinding;
import com.riteshknayak.masterq.objects.Category;
import com.riteshknayak.masterq.objects.User;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    FirebaseFirestore database;
    FirebaseAuth auth;
    String UId;
    User mUser;
    ArrayList<User> users = new ArrayList<>();

    public FragmentHomeBinding getBinding() {
        return binding;
    }

    public HomeFragment() {
        // Required empty public constructor for Firebase
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        database = FirebaseFirestore.getInstance();

        auth = FirebaseAuth.getInstance();
        UId = auth.getCurrentUser().getUid();

        final ArrayList<Category> categories = new ArrayList<>();
        final CategoryAdapter adapter = new CategoryAdapter(getContext(), categories);

        database.collection("categories")
                .addSnapshotListener((value, error) -> {
                    categories.clear();
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Category model = snapshot.toObject(Category.class);
                        if (model.isVisibility()) {
                            model.setCategoryId(snapshot.getId());
                            categories.add(model);
                        }
                    }
                    adapter.notifyDataSetChanged();
                });


        database.collection("users")
                .document(UId)
                .get().addOnSuccessListener(documentSnapshot -> {
            mUser = documentSnapshot.toObject(User.class);
            binding.name.setText(mUser.getName());
            binding.topScore.setText("Score- ".concat(mUser.getScore().toString()));
        });


        binding.categoryList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.categoryList.setAdapter(adapter);

        database.collection("users")
                .orderBy("score", Query.Direction.DESCENDING)
                .limit(5)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        User user = snapshot.toObject(User.class);
                        users.add(user);
                    }

                    if (1 <= users.size()) {
                        binding.name1.setText(users.get(0).getName());
                        binding.topScore1.setText(users.get(0).getScore().toString());
                    }

                    if (2 <= users.size()){
                        binding.name1.setText(users.get(1).getName());
                        binding.topScore1.setText(users.get(1).getScore().toString());
                    }

                    if (3 <= users.size()){
                        binding.name1.setText(users.get(2).getName());
                        binding.topScore1.setText(users.get(2).getScore().toString());
                    }

                    if (4 <= users.size()){
                        binding.name1.setText(users.get(1).getName());
                        binding.topScore1.setText(users.get(1).getScore().toString());
                    }

                    if (5 <= users.size()){
                        binding.name1.setText(users.get(4).getName());
                        binding.topScore1.setText(users.get(4).getScore().toString());
                    }

                });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }
    //TODO add data about user when the user was created
    //TODO null score in leaderboard
    //TODO store retrieved data in sharedpreferences and update when new data comes so that the second time the data will load immediate
}