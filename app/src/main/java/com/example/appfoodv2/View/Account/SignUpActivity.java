package com.example.appfoodv2.View.Account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.example.appfoodv2.Presenter.UserPreSenter;
import com.example.appfoodv2.Presenter.UserView;
import com.example.appfoodv2.R;
import com.example.appfoodv2.View.HomeActivity;

public class SignUpActivity extends AppCompatActivity  implements UserView , View.OnClickListener {
    private Button btndangky;
    private EditText editemail,editpass,editpass_repeat;
    private UserPreSenter userPreSenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        InitWidget();
        Init();
    }

    private void Init() {
        userPreSenter = new UserPreSenter(this);
        btndangky.setOnClickListener(this);

    }

    private void InitWidget() {
        btndangky = findViewById(R.id.btndangky);
        editemail=findViewById(R.id.editEmail);
        editpass = findViewById(R.id.editmatkhau);
        editpass_repeat = findViewById(R.id.editmatkhau_repeat);
    }

    @Override
    public void OnLengthEmail() {
        Toast.makeText(this, "Email không để trống", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnValidEmail() {
        Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnPass() {
        Toast.makeText(this, "Mật khẩu không để trống", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void OnSucess() {
        startActivity(new Intent(this, HomeActivity.class));
        Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
        finish();

    }

    @Override
    public void OnAuthEmail() {
        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();

        Toast.makeText(this, "Hãy vào gamil để xác thực tài khoản của bạn !", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void OnFail() {
        Toast.makeText(this, "Tài khoản đã được đăng ký !", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnPassNotSame() {
        Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.btndangky:
                String email=editemail.getText().toString();
                String pass =editpass.getText().toString().trim();
                String repass =editpass_repeat.getText().toString().trim();
                userPreSenter.HandleRegist(email,pass,repass);
        }
    }
}

