package com.riteshknayak.masterq;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.riteshknayak.masterq.databinding.ActivitySignupBinding;

import java.util.Objects;


public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();


        if(auth.getCurrentUser() != null) {
            startActivity(new Intent(SignupActivity.this, MainActivity.class));
            finish();
        }


        binding.signupBtn.setOnClickListener(v -> {
            String email, pass, name;

            email = binding.emailBox.getText().toString();
            pass = binding.passwordBox.getText().toString();
            name = binding.nameBox.getText().toString();


            final User user = new User(name, email, pass);

            auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {


                if (task.isSuccessful()) {
                    Toast.makeText(SignupActivity.this, "Success", Toast.LENGTH_SHORT).show();

                    String uid = Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).getUid();

                    database
                            .collection("users")
                            .document(uid)
                            .set(user).addOnCompleteListener(task1 -> {
                                if(task1.isSuccessful()) {
                                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(SignupActivity.this, Objects.requireNonNull(task1.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(SignupActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
        binding.loginBtn.setOnClickListener(v -> startActivity(new Intent(SignupActivity.this, LoginActivity.class)));

    }
}