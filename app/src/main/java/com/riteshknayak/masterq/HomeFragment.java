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
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.annotation.AnimRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mlsdev.animatedrv.AnimatedRecyclerView;
import com.riteshknayak.masterq.adapters.CategoryAdapter;
import com.riteshknayak.masterq.adapters.ImagerSliderAdapter;
import com.riteshknayak.masterq.databinding.FragmentHomeBinding;
import com.riteshknayak.masterq.objects.Category;
import com.riteshknayak.masterq.objects.SliderItem;
import com.riteshknayak.masterq.objects.User;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogAnimation;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    FirebaseFirestore database;
    FirebaseAuth auth;
    String UId;
    User mUser;

    public HomeFragment() {
        // Required empty public constructor for Firebase
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkIfOnline();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        database = FirebaseFirestore.getInstance();

        auth = FirebaseAuth.getInstance();
        UId = auth.getCurrentUser().getUid();

        ImagerSliderAdapter sliderAdapter = new ImagerSliderAdapter(getContext(), getActivity());

        database.collection("MasterQ")
                .document("updates")
                .collection("imageSlider")
                .addSnapshotListener((value, error) -> {
                    sliderAdapter.clearAllSlides();
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        sliderAdapter.addItem(snapshot.toObject(SliderItem.class));
                    }
                    sliderAdapter.notifyDataSetChanged();
                });

        binding.imageSlider.setSliderAdapter(sliderAdapter);
        binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.DROP); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.CUBEINROTATIONTRANSFORMATION);
        binding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        binding.imageSlider.setIndicatorSelectedColor(0xFFCA1395);
        binding.imageSlider.setIndicatorUnselectedColor(0xFFA9A9A9);
        binding.imageSlider.setScrollTimeInSec(3); //set scroll delay in seconds :
        binding.imageSlider.startAutoCycle();


        final ArrayList<Category> categories = new ArrayList<>();
        final CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), categories, getActivity());

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
                    categoryAdapter.notifyDataSetChanged();
                });

        AnimatedRecyclerView recyclerView = binding.categoryList;
        recyclerView.setAdapter(categoryAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        @AnimRes int layoutAnimation = R.anim.layout_animation_from_bottom;
        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(getContext(), layoutAnimation);
        recyclerView.setLayoutAnimation(animationController);
        categoryAdapter.notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();

        database.collection("users")
                .document(UId)
                .get().addOnSuccessListener(documentSnapshot -> {
            mUser = documentSnapshot.toObject(User.class);
            assert mUser != null;
            if (mUser.getName() != null) {
                binding.name.setText(mUser.getName());
            }
            if (mUser.getScore() != null) {
                binding.topScore.setText(String.valueOf(mUser.getScore()));
            }
            if (mUser.getImageUrl() != null) {
                if (isAdded()) {
                    Glide.with(requireContext())
                            .load(mUser.getImageUrl())
                            .into(binding.profileImage);

                }
            }
        });

        // Inflate the layout for this fragment
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

    //TODO add data about user when the user was created
    //TODO null score in leaderboard
    //TODO store retrieved data in sharedpreferences and update when new data comes so that the second time the data will load immediate
}