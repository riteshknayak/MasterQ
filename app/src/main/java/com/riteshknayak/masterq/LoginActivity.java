package com.riteshknayak.masterq;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.riteshknayak.masterq.databinding.ActivityLoginBinding;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();


        if(auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        //TODO app crashing when  clicking the sign in button when edittext is empty . use try-catch to solve the error
        //TODO Check weather the quizme app also crash when empty edittext is clicked

        binding.emailBtn.setOnClickListener(v -> {
            String email, pass;
            email = binding.emailBox.getText().toString();
            pass = binding.passwordBox.getText().toString();


            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        binding.createBtn.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignupActivity.class)));


    }
}