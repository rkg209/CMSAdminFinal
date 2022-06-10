package com.example.cms_admin;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;
import papaya.in.sendmail.SendMail;

public class CreateClub extends AppCompatActivity
{
    public EditText club_email;
    public EditText club_pass;
    public EditText club_conf_pass;
    public EditText dialog_pass;
    public AppCompatButton dialog_btn;
    public MaterialCheckBox cb_agree;
    public MaterialCheckBox cb_terms;
    public AppCompatButton btn_create;

    private FirebaseAuth auth;
    private DatabaseReference myRef;
    private static String ClubCreationPassWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_club);

        club_email = findViewById(R.id.ed_club_email);
        club_pass = findViewById(R.id.ed_pass);
        club_conf_pass = findViewById(R.id.ed_conf_pass);
        cb_agree = findViewById(R.id.cb_agree);
        cb_terms = findViewById(R.id.cb_terms);
        btn_create = findViewById(R.id.btn_create);

        auth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("CreatePass");
        getCreationPass();

        cb_agree.setOnClickListener(view -> {
            if (cb_agree.isChecked() == true && cb_terms.isChecked() == true)
            {
                btn_create.setEnabled(true);
            }
            else
            {
                btn_create.setEnabled(false);
            }
        });

        cb_terms.setOnClickListener(view -> {
            if (cb_terms.isChecked() == true && cb_agree.isChecked() == true)
            {
                btn_create.setEnabled(true);
            }
            else
            {
                btn_create.setEnabled(false);
            }
        });

        btn_create.setOnClickListener(view -> {
            Toast.makeText(CreateClub.this, "Clicked", Toast.LENGTH_SHORT).show();
            if(club_email.getText().toString().equals("") || club_pass.getText().toString().equals("")
                    || club_conf_pass.getText().toString().equals(""))
            {
                new SweetAlertDialog(
                        CreateClub.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Something went wrong!")
                        .show();

            }
            else if(!(club_pass.getText().toString().equals(club_conf_pass.getText().toString())))
            {
                new SweetAlertDialog(
                        CreateClub.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText("Password not matches!")
                        .show();
            }
            else
            {
                final AlertDialog.Builder alert = new AlertDialog.Builder(CreateClub.this);
                View view2 = getLayoutInflater().inflate(R.layout.password_dialog,null);
                alert.setView(view2);
                final AlertDialog alertDialog = alert.create();

                dialog_pass = view2.findViewById(R.id.ed_dialog_pass);
                dialog_btn = view2.findViewById(R.id.ed_dialog_btn_create);

                dialog_btn.setOnClickListener(view1 -> {
                    if (dialog_pass.getText().toString().equals(ClubCreationPassWord))
                    {
                        alertDialog.cancel();
                        String email = club_email.getText().toString();
                        String pass = club_pass.getText().toString();

                        createClub(email, pass);

                        SendMail mail = new SendMail("clubmanagementsystem84@gmail.com","cms@989810",
                                email,
                                "Club Credentials",
                                "Club account username : "+email+"\n"+"Club account password : "+pass);
                        mail.execute();
                        SweetAlertDialog dialog = new SweetAlertDialog(CreateClub.this,SweetAlertDialog.SUCCESS_TYPE);
                        dialog.setCancelable(false);
                        dialog.setTitleText("Success");
                        dialog.setContentText("Club credentials created");
                        dialog.setConfirmClickListener(sweetAlertDialog -> finish());
                        dialog.show();
                    }
                });

                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();

            }
        });
    }

    private void getCreationPass(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ClubCreationPassWord = dataSnapshot.getValue(String.class);
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private void createClub(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CreateClub.this, "Club Account Created", Toast.LENGTH_SHORT).show();

                            FirebaseUser user = auth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(email).build();

                            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                           //Username get set to email
                                        }
                                    });

                        } else {
                            Toast.makeText(CreateClub.this, "Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
