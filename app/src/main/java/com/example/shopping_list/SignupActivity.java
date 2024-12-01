package com.example.shopping_list;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    EditText  email , password;
    Button signup , login;
    ProgressBar bar;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signupActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // initializing all the needed views
        init();

        // signup and send details to firebase
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = email.getText().toString().trim() , userPassword = password.getText().toString().trim();
                if(TextUtils.isEmpty(userEmail) || TextUtils.isEmpty((userPassword))){
                    Toast.makeText(SignupActivity.this, "Please Enter Your Email & Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                signup.setVisibility(View.GONE);
                                bar.setVisibility(View.VISIBLE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Signup Successful!.", Toast.LENGTH_SHORT).show();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }, 2000);
                                } else {
                                    signup.setVisibility(View.VISIBLE);
                                    Toast.makeText(SignupActivity.this, "Signup failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // login screen navigation
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent navigator = new Intent(SignupActivity.this , LoginActivity.class);
                startActivity(navigator);
            }
        });
    }

    private void init() {
        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);
        signup = findViewById(R.id.signupBtn);
        login = findViewById(R.id.loginAlready);
        mAuth = FirebaseAuth.getInstance();
        bar = findViewById(R.id.progressBar);
    }
}