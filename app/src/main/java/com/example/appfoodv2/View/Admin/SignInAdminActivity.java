package com.example.appfoodv2.View.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.appfoodv2.Presenter.UserPreSenter;
import com.example.appfoodv2.R;

public class SignInAdminActivity extends AppCompatActivity  implements  View.OnClickListener {
    private Button btndangnhap;
    private EditText editemail,editpass;
    private UserPreSenter userPreSenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_admin);
        InitWidget();
        Init();
    }

    private void Init() {

        btndangnhap.setOnClickListener(this);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void InitWidget() {
        btndangnhap = findViewById(R.id.btndangnhap);
        editemail=findViewById(R.id.editEmail);
        editpass = findViewById(R.id.editmatkhau);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.btndangnhap:
                String username=editemail.getText().toString();
                String pass =editpass.getText().toString().trim();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                if(username.length()>0 ){
                    if(pass.length()>0){
                        db.collection("Admin").
                                whereEqualTo("username",username)
                                .whereEqualTo("pass",pass).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                                if(queryDocumentSnapshots.size()>0){
                                    startActivity(new Intent(SignInAdminActivity.this,HomeAdminActivity.class));
                                    finish();
                                }else{
                                    Toast.makeText(SignInAdminActivity.this, "Sai tài khoản / Mật khẩu", Toast.LENGTH_SHORT).show();
                                }

                            }
                        })     ;
                    }else{

                    }
                }else{

                }



        }
    }
}

