package com.example.shopping_list;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    Button signup , login;
    EditText email , password;
    FirebaseAuth mAuth;
    ProgressBar bar;
    Intent navigator;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Toast.makeText(this, "Welcome User!", Toast.LENGTH_SHORT).show();
            Intent navigator = new Intent(LoginActivity.this , MainPage.class);
            startActivity(navigator);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.loginActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initializing components
        init();

        // Login Functionality
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = email.getText().toString().trim() , userPassword = password.getText().toString().trim();
                if(TextUtils.isEmpty(userEmail) || TextUtils.isEmpty((userPassword))){
                    Toast.makeText(LoginActivity.this, "Please Enter Your Email & Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Login Successful!.", Toast.LENGTH_SHORT).show();
                                    Intent navigator = new Intent(LoginActivity.this , MainPage.class);
                                    startActivity(navigator);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Login failed!.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // Signup page navigation
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigator = new Intent(LoginActivity.this , SignupActivity.class);
                startActivity(navigator);
            }
        });
    }


    private void init(){
        signup = findViewById(R.id.Lsignup);
        login = findViewById(R.id.Llogin);
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        bar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
    }
}
