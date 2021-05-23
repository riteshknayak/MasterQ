package com.riteshknayak.masterq;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.riteshknayak.masterq.category.CategoryAdapter;
import com.riteshknayak.masterq.category.CategoryModel;
import com.riteshknayak.masterq.databinding.FragmentHomeBinding;

import java.util.ArrayList;


public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    FragmentHomeBinding binding;
    FirebaseFirestore database;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        database = FirebaseFirestore.getInstance();

        final ArrayList<CategoryModel> categories = new ArrayList<>();

        final CategoryAdapter adapter = new CategoryAdapter(getContext(), categories);

        database.collection("categories")
                .addSnapshotListener((value, error) -> {
                    categories.clear();
                    assert value != null;  //TODO may change
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        CategoryModel model = snapshot.toObject(CategoryModel.class);
                        assert model != null; //TODO may change
                        model.setCategoryId(snapshot.getId());
                        categories.add(model);
                    }
                    adapter.notifyDataSetChanged();
                });

        binding.categoryList.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.categoryList.setAdapter(adapter);

        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}