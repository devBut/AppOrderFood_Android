package com.example.appfoodv2.View.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfoodv2.Model.SanPhamModels;
import com.example.appfoodv2.Presenter.ISanPham;
import com.example.appfoodv2.R;
import com.example.appfoodv2.dangsanphamActivity;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {
    ProductAdapter adapter;
    RecyclerView rcv;
    private SanPhamModels sanPhamModels;
    private ArrayList<SanPhamModels> arr_sp = new ArrayList<>();
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        rcv = findViewById(R.id.rcv);
        dialog = new ProgressDialog(this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        sanPhamModels = new SanPhamModels(new ISanPham() {
            @Override
            public void getDataSanPham(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String nhasanxuat, Long type, String trongluong) {
                arr_sp.add(new SanPhamModels(id, tensp, giatien, hinhanh, loaisp, mota, soluong, nhasanxuat, type, trongluong));
                adapter = new ProductAdapter(ProductActivity.this, arr_sp);
                rcv.setAdapter(adapter);
                dialog.dismiss();
            }

            @Override
            public void OnEmptyList() {
                dialog.dismiss();
            }

            @Override
            public void getDataSanPhamNB(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String nhasanxuat, Long type, String trongluong) {
                arr_sp.add(new SanPhamModels(id, tensp, giatien, hinhanh, loaisp, mota, soluong, nhasanxuat, type, trongluong));
                adapter = new ProductAdapter(ProductActivity.this, arr_sp);
                rcv.setAdapter(adapter);
                dialog.dismiss();
            }


            @Override
            public void getDataSanPhamTU(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String nhasanxuat, Long type, String trongluong) {
                arr_sp.add(new SanPhamModels(id, tensp, giatien, hinhanh, loaisp, mota, soluong, nhasanxuat, type, trongluong));
                adapter = new ProductAdapter(ProductActivity.this, arr_sp);
                rcv.setAdapter(adapter);
                dialog.dismiss();
            }
            @Override
            public void getDataSanPhamHQ(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String nhasanxuat, Long type, String trongluong) {
                arr_sp.add(new SanPhamModels(id, tensp, giatien, hinhanh, loaisp, mota, soluong, nhasanxuat, type, trongluong));
                adapter = new ProductAdapter(ProductActivity.this, arr_sp);
                rcv.setAdapter(adapter);
                dialog.dismiss();
            }
            @Override
            public void getDataSanPhamMC(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String nhasanxuat, Long type, String trongluong) {
                arr_sp.add(new SanPhamModels(id, tensp, giatien, hinhanh, loaisp, mota, soluong, nhasanxuat, type, trongluong));
                adapter = new ProductAdapter(ProductActivity.this, arr_sp);
                rcv.setAdapter(adapter);
                dialog.dismiss();
            }
            @Override
            public void getDataSanPhamYT(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String nhasanxuat, Long type, String trongluong) {
                arr_sp.add(new SanPhamModels(id, tensp, giatien, hinhanh, loaisp, mota, soluong, nhasanxuat, type, trongluong));
                adapter = new ProductAdapter(ProductActivity.this, arr_sp);
                rcv.setAdapter(adapter);
                dialog.dismiss();
            }
            @Override
            public void getDataSanPhamLau(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String nhasanxuat, Long type, String trongluong) {
                arr_sp.add(new SanPhamModels(id, tensp, giatien, hinhanh, loaisp, mota, soluong, nhasanxuat, type, trongluong));
                adapter = new ProductAdapter(ProductActivity.this, arr_sp);
                rcv.setAdapter(adapter);
                dialog.dismiss();
            }
            @Override
            public void getDataSanPhamGY(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String nhasanxuat, Long type, String trongluong) {
                arr_sp.add(new SanPhamModels(id, tensp, giatien, hinhanh, loaisp, mota, soluong, nhasanxuat, type, trongluong));
                adapter = new ProductAdapter(ProductActivity.this, arr_sp);
                rcv.setAdapter(adapter);
                dialog.dismiss();
            }
        });
        dialog.show();
        sanPhamModels.HandlegetDataSanPhamAll();

        findViewById(R.id.image_add).setOnClickListener(view -> {
            startActivityForResult(new Intent(ProductActivity.this, dangsanphamActivity.class), 100);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                dialog.show();
                arr_sp.clear();
                sanPhamModels.HandlegetDataSanPhamAll();

            }
        }
    }
}