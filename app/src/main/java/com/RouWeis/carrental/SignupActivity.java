package com.RouWeis.carrental;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private EditText name;
    private EditText mail;
    private EditText pass;
    private Button Sign_up;
    private Button return1;
    private FirebaseAuth Auth;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        init();


        if (Auth.getCurrentUser() != null) {
            Log.d("SigninActivity", "starting Home Activity ...");
            startActivity(new Intent(SignupActivity.this, Recherche.class));
            finish();
        }
        return1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(SignupActivity.this, SigninActivity.class);
                startActivity(intent2);
            }
        });
        Log.d("SigninActivity", "starting button");
        Sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mail.getText().toString();
                String password = pass.getText().toString();
                String nameBD = name.getText().toString();
                users user = new users(email, password, nameBD);

                Map<String, Object> userH = new HashMap<>();
                userH.put("Name", user.getName());
                userH.put("Email", user.getEmail());

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                // Add a new document with a generated ID
                db.collection("users")
                        .add(userH)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("TAG", "Error adding document", e);
                            }
                        });

                Log.d("SigninActivity", "checking if empty");
                if (email.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                    ;
                    return;
                }
                if (password.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                    ;
                    return;
                }
                //     pb.setVisibility(View.VISIBLE);
                Log.d("SigninActivity", "creating user in Firebase");
                Auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //  pb.setVisibility(View.INVISIBLE);

                            Toast.makeText(SignupActivity.this, "Registration Success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignupActivity.this, Recherche.class));
                            Log.d("SigninActivity", "User Created");
                        } else {
                            // pb.setVisibility(View.INVISIBLE);
                            Toast.makeText(SignupActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }
        });

    }

    void init() {
        name = findViewById(R.id.name);
        mail = findViewById(R.id.mail);
        pass = findViewById(R.id.pass);
        Sign_up = findViewById(R.id.sign_up_btn);
        return1 = findViewById(R.id.return1);
        Auth = FirebaseAuth.getInstance();
        pb = findViewById(R.id.progressBar);

    }

}