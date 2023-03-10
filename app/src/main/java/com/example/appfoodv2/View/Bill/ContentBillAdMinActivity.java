package com.example.appfoodv2.View.Bill;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfoodv2.Adapter.SanPhamAdapter;
import com.example.appfoodv2.Model.HoaDonModels;
import com.example.appfoodv2.Model.SanPhamModels;
import com.example.appfoodv2.Presenter.GioHangPreSenter;
import com.example.appfoodv2.Presenter.GioHangView;
import com.example.appfoodv2.Presenter.HoaDonPreSenter;
import com.example.appfoodv2.Presenter.HoaDonView;
import com.example.appfoodv2.R;

import java.text.NumberFormat;
import java.util.ArrayList;

public class ContentBillAdMinActivity extends AppCompatActivity implements GioHangView, HoaDonView {
    private Intent intent;
    private HoaDonModels hoaDonModels;
    private TextView txtmaHD, txthoten, txtdiachi, txtsdt, txttongtien,txtrangthai;
    private Toolbar toolbar;
    private ImageView hinhanh;
    private Button btncapnhat;
    private GioHangPreSenter gioHangPreSenter;
    private ArrayList<SanPhamModels> arrayList;
    private  SanPhamAdapter sanPhamAdapter;
    private RecyclerView rcvBill;
    private HoaDonPreSenter hoaDonPreSenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_bill);
        InitWidget();
        Init();
    }

    private void Init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chi ti???t h??a ????n");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        intent=getIntent();
        hoaDonModels = (HoaDonModels) intent.getSerializableExtra("HD");
        int type = intent.getIntExtra("TYPE",0);
        txtdiachi.setText("?????a ch??? : "+hoaDonModels.getDiachi());
        txtmaHD.setText("M?? HD :"+hoaDonModels.getId());
        txthoten.setText("H??? t??n : "+hoaDonModels.getHoten());
        txtsdt.setText("Li??n h??? : "+hoaDonModels.getSdt());
        txttongtien.setText("Gia?? ti????n: "+NumberFormat.getNumberInstance().format(hoaDonModels.getTongtien()));

        switch ((int) hoaDonModels.getType()){
            case  1: txtrangthai.setText("Tr???ng Th??i : ??ang x??? l??");break;
            case  2: txtrangthai.setText("Tr???ng Th??i : ??ang giao h??ng");break;
            case  3: txtrangthai.setText("Tr???ng Th??i : Giao H??ng Th??nh C??ng");break;
            case  4: txtrangthai.setText("Tr???ng Th??i : H???y ????n H??ng");break;
        }



        gioHangPreSenter = new GioHangPreSenter(this);
        hoaDonPreSenter = new HoaDonPreSenter(this);
        arrayList = new ArrayList<>();
        if(type == 5){
            gioHangPreSenter.HandlegetDataCTHD(hoaDonModels.getId(),hoaDonModels.getUid());
        }else{
            gioHangPreSenter.HandlegetDataCTHD(hoaDonModels.getId());
        }

        btncapnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               DiaLogUpDate();
            }
        });

    }
//Ha??m ki????m tra hu??y ????n ha??ng
    private void DiaLogUpDate() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_update_trangthai);
        dialog.show();
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Spinner spiner = dialog.findViewById(R.id.spinerCapNhat);
        String[] s = {"Ch???n M???c","??ang x??? l??","??ang giao h??ng","Giao H??ng Th??nh C??ng","H???y ????n H??ng"} ;

        ArrayAdapter arrayAdapter  = new ArrayAdapter(this, android.R.layout.simple_list_item_1,s);
        spiner.setAdapter( arrayAdapter);
        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    if(hoaDonModels.getType() <3){
                        hoaDonPreSenter.CapNhatTrangThai(spiner.getSelectedItemPosition(),hoaDonModels.getId());
                        dialog.cancel();
                    }else if(hoaDonModels.getType() == 4){
                        Toast.makeText(ContentBillAdMinActivity.this, "????n h??ng ???? h???y!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ContentBillAdMinActivity.this, "????n h??ng b???n kh??ng th??? h???y", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void InitWidget() {
        toolbar = findViewById(R.id.toolbar);
        txtdiachi = findViewById(R.id.txtdiachi);
        txthoten = findViewById(R.id.txthoten);
        txtrangthai=findViewById(R.id.txtrangthaidonhang);
        txtsdt=findViewById(R.id.txtsdt);
        txttongtien=findViewById(R.id.txttongtien);
        txtmaHD=findViewById(R.id.txtmaHD);
        rcvBill=findViewById(R.id.rcvSP);

        btncapnhat=findViewById(R.id.btncapnhat);

    }

    @Override
    public void OnSucess() {
        Toast.makeText(this, "C???p nh???t th??nh c??ng ", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void OnFail() {
        Toast.makeText(this, "Th???t B???i ! L???i h??? th???ng b???o tr??", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getDataSanPham(String id, String idsp, String tensp, Long giatien, String hinhanh, String loaisp, Long soluong, String hansudung, Long type, String trongluong) {
        arrayList.add(new SanPhamModels(id,idsp,tensp,giatien,hinhanh,loaisp,soluong,hansudung,type,trongluong));
        sanPhamAdapter = new SanPhamAdapter(this,arrayList,1);
        rcvBill.setLayoutManager(new LinearLayoutManager(this));
        rcvBill.setAdapter(sanPhamAdapter);
    }


    @Override
    public void getDataHD(String id, String uid, String diachi, String hoten, String ngaydat, String phuongthuc, String sdt, Long tongtien, Long type) {

    }
}
