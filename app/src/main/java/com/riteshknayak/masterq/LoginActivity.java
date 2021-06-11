package com.riteshknayak.masterq;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.riteshknayak.masterq.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseApp.initializeApp(this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(SafetyNetAppCheckProviderFactory.getInstance());


        auth = FirebaseAuth.getInstance();

        //TODO isEmpty(binding.emailBox) ||

//        binding.emailBtn.setOnClickListener(v -> {
//            if ( isEmpty(binding.passwordBox) ) {
//                Toast.makeText(LoginActivity.this, "Please provide the required data", Toast.LENGTH_SHORT).show();
//            } else {
//
//                String email, pass;
////                email = binding.emailBox.getText().toString();
//                pass = binding.passwordBox.getText().toString();
//
//
//                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                        finish();
//                        SharedPreferences shared = getSharedPreferences("app", Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = shared.edit();
//                        editor.putString("UId", auth.getCurrentUser().getUid());
//                        editor.apply();
//
//                    } else {
//                        Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });

        binding.createBtn.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignupActivity.class)));


    }
    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }
}