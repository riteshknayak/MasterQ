package com.riteshknayak.masterq;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.riteshknayak.masterq.databinding.ActivitySignupBinding;
import com.riteshknayak.masterq.objects.User;

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

        FirebaseApp.initializeApp(this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(SafetyNetAppCheckProviderFactory.getInstance());

        //TODO add signup with google
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        //TODO app crashing when  clicking the signup button when edittext is empty. use try-catch to solve the error
//        if (auth.getCurrentUser() != null) {
//            String uid = auth.getCurrentUser().getUid();
//            SharedPreferences shared = getSharedPreferences("app", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = shared.edit();
//            editor.putString("UId", uid);
//            editor.apply();
//            startActivity(new Intent(SignupActivity.this, MainActivity.class));
//            finish();
//        }


        binding.signupBtn.setOnClickListener(v -> {
            String email, pass, name;

            if (isEmpty(binding.emailBtn) || isEmpty(binding.passwordBox) || isEmpty(binding.nameBox)) {
                Toast.makeText(SignupActivity.this, "Please provide the required data", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(SignupActivity.this, binding.emailBtn.getText().toString(), Toast.LENGTH_SHORT).show();

                email = binding.emailBtn.getText().toString();
                pass = binding.passwordBox.getText().toString();
                name = binding.nameBox.getText().toString();

                final User user = new User(name, email, pass);
//                user.seDefault();

                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        Toast.makeText(SignupActivity.this, "Success", Toast.LENGTH_SHORT).show();

                        String uid = Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).getUid();

                        SharedPreferences shared = getSharedPreferences("app", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = shared.edit();
                        editor.putString("UId", uid);
                        editor.apply();

                        user.setUid(uid);
                        database
                                .collection("users")
                                .document(uid)
                                .set(user).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
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
            }
        });
        binding.signinBtn.setOnClickListener(v -> startActivity(new Intent(SignupActivity.this, LoginActivity.class)));

    }
    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }
}