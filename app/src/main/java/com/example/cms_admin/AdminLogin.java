package com.example.cms_admin;

import android.app.admin.SystemUpdateInfo;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminLogin extends AppCompatActivity
{
    public EditText user_name;
    public EditText user_pass;
    public Button btn_login;
    private FirebaseAuth auth;
    private DatabaseReference myRef;
    private static String AdminEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_login);

        user_name = findViewById(R.id.ed_username);
        user_pass = findViewById(R.id.ed_pass);
        btn_login = findViewById(R.id.btn_login);

        auth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("AdminEmail");
        getAdminEmail();

        btn_login.setOnClickListener(view -> {

            String uName = user_name.getText().toString();
            String pass = user_pass.getText().toString();

            //Toast.makeText(AdminLogin.this, "Clicked", Toast.LENGTH_SHORT).show();

            if(uName.equals(AdminEmail)){
                signIn(uName,pass);
            }else {
                Toast.makeText(AdminLogin.this, "Only Admin can Login", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();           //To check if user is logged in or not
        if(currentUser != null){
            //go to Home page of Admin
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }
    }

    private void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AdminLogin.this, "Validated", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(AdminLogin.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getAdminEmail(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AdminEmail= dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

}
