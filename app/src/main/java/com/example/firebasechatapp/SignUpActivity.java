package com.example.firebasechatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText username, password, email;
    private Button signUp;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private DocumentReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        signUp = findViewById(R.id.signUp);

        progressBar = findViewById(R.id.progress);

        firebaseAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Username = username.getText().toString().trim();
                String Password = password.getText().toString().trim();
                String Email = email.getText().toString().trim();

                if (Username.isEmpty() || Password.isEmpty() || Email.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Fill all three entries", Toast.LENGTH_SHORT).show();
                    return;
                }else if (Password.length() < 6){

                    Toast.makeText(SignUpActivity.this, "Password should have a minimum of six characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(Username, Email, Password);
                }

            }
        });
    }

    private void registerUser(final String username, String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()){

                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    assert user != null;
                    String userId = user.getUid();
                    reference = FirebaseFirestore.getInstance().collection("User").document(userId);

                    Map<String, String> map = new HashMap<>();
                    map.put("id", userId);
                    map.put("username", username);
                    map.put("imageURL", "default");
                    map.put("status", "offline");
                    map.put("search", username.toLowerCase());
                    reference.set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Intent intent = new Intent(SignUpActivity.this, MainPart.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                } else{
                    Toast.makeText(SignUpActivity.this, "Error registering with this email and password", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}