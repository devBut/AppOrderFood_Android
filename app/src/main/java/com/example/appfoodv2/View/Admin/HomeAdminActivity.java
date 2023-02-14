package com.example.appfoodv2.View.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appfoodv2.R;

public class HomeAdminActivity  extends AppCompatActivity implements View.OnClickListener {
    private ImageView imgdangsanpham;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);
        findViewById(R.id.cHoaDon).setOnClickListener(this);
        findViewById(R.id.cThongKe).setOnClickListener(this);
        findViewById(R.id.cSignOut).setOnClickListener(this);
        findViewById(R.id.imgdangsanpham).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cHoaDon: startActivity(new Intent( HomeAdminActivity.this,BillADMiNActivity.class));break;

            case R.id.cThongKe: startActivity(new Intent( HomeAdminActivity.this,ChartBillActivity.class));break;
            case R.id.cSignOut: finish();break;
            case R.id.imgdangsanpham: startActivity(new Intent( HomeAdminActivity.this, ProductActivity.class));break;



        }
    }

}
