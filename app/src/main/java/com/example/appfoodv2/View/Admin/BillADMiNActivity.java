package com.example.appfoodv2.View.Admin;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfoodv2.Adapter.HoaDonAdMinAdapter;
import com.example.appfoodv2.Model.HoaDonModels;
import com.example.appfoodv2.Presenter.HoaDonPreSenter;
import com.example.appfoodv2.Presenter.HoaDonView;
import com.example.appfoodv2.R;

import java.util.ArrayList;

public class BillADMiNActivity extends AppCompatActivity implements HoaDonView {
    private Toolbar toolbar;
    private RecyclerView rcvBill;
    private  String[] s = {"Tất cả","Đang xử lý","Đang giao hàng","Giao hàng thành công","Hủy hàng"};
    private Spinner spinner;
    private HoaDonPreSenter hoaDonPreSenter;
    private ArrayList<HoaDonModels> arrayList;
    private HoaDonAdMinAdapter hoaDonAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_admin);
        spinner = findViewById(R.id.spiner);
        toolbar = findViewById(R.id.toolbar);
        rcvBill = findViewById(R.id.rcvBill);
        Init();
    }

    private void Init() {
        hoaDonPreSenter = new HoaDonPreSenter(this);
        arrayList = new ArrayList<>();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,s);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                arrayList.clear();
                if(hoaDonAdapter!=null){
                    hoaDonAdapter.notifyDataSetChanged();
                }
                hoaDonPreSenter.HandleReadDataHD(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Back");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                int pos = viewHolder.getAdapterPosition();
                DiaLogUpDate(pos);


            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rcvBill);
    }

    private void DiaLogUpDate(int pos) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_update_trangthai);
        dialog.show();
         hoaDonAdapter.notifyDataSetChanged();

        Spinner spiner = dialog.findViewById(R.id.spinerCapNhat);
        String[] s = {"Chọn Mục","Đang xử lý","Đang giao hàng","Giao hàng thành công","Hủy hàng"} ;
        ArrayAdapter arrayAdapter  = new ArrayAdapter(this, android.R.layout.simple_list_item_1,s);
        spiner.setAdapter( arrayAdapter);
        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                       if(position>0){
                           hoaDonPreSenter.CapNhatTrangThai(4,arrayList.get(pos).getId());
                           dialog.cancel();
                       }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void getDataHD(String id, String uid, String diachi, String hoten, String ngaydat, String phuongthuc, String sdt, Long tongtien, Long type) {
        arrayList.add(new HoaDonModels(id,uid,diachi,hoten,ngaydat,phuongthuc,sdt,tongtien,type));
        hoaDonAdapter = new HoaDonAdMinAdapter(this,arrayList,5);
        rcvBill.setLayoutManager(new LinearLayoutManager(this));
        rcvBill.setAdapter(hoaDonAdapter);

    }

    @Override
    public void OnFail() {

    }

    @Override
    public void OnSucess() {
        arrayList.clear();
        if(hoaDonAdapter!=null){
            hoaDonAdapter.notifyDataSetChanged();
        }
        hoaDonPreSenter.HandleReadDataHD(0);
    }
}
