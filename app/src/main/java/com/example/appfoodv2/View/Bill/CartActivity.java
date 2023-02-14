package com.example.appfoodv2.View.Bill;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.example.appfoodv2.Adapter.GioHangAdapter;
import com.example.appfoodv2.Model.SanPhamModels;
import com.example.appfoodv2.Presenter.GioHangPreSenter;
import com.example.appfoodv2.Presenter.GioHangView;
import com.example.appfoodv2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import vn.momo.momo_partner.AppMoMoLib;

public class CartActivity extends AppCompatActivity implements GioHangView {
    private RecyclerView rcVBill;
    private GioHangAdapter sanPhamAdapter;
    private GioHangPreSenter gioHangPreSenter;
    private ArrayList<SanPhamModels> arrayList;
    private Button btnthanhtoan;
    private  String s[]={"Thanh toán khi nhận hàng","Thanh toán MOMO"};
    private  long tongtien = 0;
    private ProgressBar progressBar;
    private  String hoten="",diachi="",sdt="";
    private  Spinner spinner;
    private  int check =  0 ;
    private Toolbar toolbar;

   
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_cart);
        InitWidget();
        Init();
    }

    private void Init() {
        arrayList = new ArrayList<>();
        gioHangPreSenter = new GioHangPreSenter(this);
        gioHangPreSenter.HandlegetDataGioHang();
        btnthanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arrayList.size()>0){
                    DiaLogThanhToan();
                }else{
                    Toast.makeText(CartActivity.this, "Sản phẩm không có trong giỏ hàng !", Toast.LENGTH_SHORT).show();
                }

            }
        });
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return true;
            }
      //chức năng xóa sp trong giỏ hàng
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                AlertDialog.Builder buidler = new AlertDialog.Builder(CartActivity.this);
                buidler.setMessage("Bạn có muôn xóa  sản phẩm "+arrayList.get(pos).getTensp());
                buidler.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sanPhamAdapter.notifyDataSetChanged();
                        gioHangPreSenter.HandlegetDataGioHang(arrayList.get(pos).getId());
                        arrayList.remove(pos);
                        check = 1;

                    }
                });
                buidler.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sanPhamAdapter.notifyDataSetChanged();
                    }
                });
                buidler.show();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rcVBill);
    }

    private void DiaLogThanhToan() {
        Dialog dialog = new Dialog(CartActivity.this);
        dialog.setContentView(R.layout.dialog_thanhtoan);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        CustomInit(dialog);

    }
//Dien thong tin đặt hàng
    private void CustomInit(Dialog dialog) {
        spinner = dialog.findViewById(R.id.spinerphguongthuc);
        EditText edithoten=dialog.findViewById(R.id.edithoten);
        EditText editdiachi=dialog.findViewById(R.id.editdiachi);
        EditText editsdt=dialog.findViewById(R.id.editsdt);
        TextView txttongtien= dialog.findViewById(R.id.txttongtien);
        Button btnxacnhan = dialog.findViewById(R.id.btnxacnhan);
        dialog.setCanceledOnTouchOutside(false);

        ArrayAdapter arrayAdapter = new ArrayAdapter(CartActivity.this, android.R.layout.simple_list_item_1,s);
        spinner.setAdapter(arrayAdapter);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        tongtien = 0 ;

            for (SanPhamModels sanPhamModels : arrayList) {
                tongtien += sanPhamModels.getGiatien()  * sanPhamModels.getSoluong();
            }



        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        txttongtien.setText("Tổng Tiền : "+ NumberFormat.getInstance().format(tongtien));
        btnxacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hoten=edithoten.getText().toString().trim();
                diachi =editdiachi.getText().toString().trim();
                sdt = editsdt.getText().toString().trim();
                if(hoten.length()>0){
                    if(diachi.length()>0){
                        if(sdt.length()>0){
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            Calendar calendar = Calendar.getInstance();
                            String ngaydat = simpleDateFormat.format(calendar.getTime());
                            String phuongthuc = spinner.getSelectedItem().toString();
                            switch (spinner.getSelectedItemPosition()){
                                case 0:
                                    gioHangPreSenter.HandleAddHoaDon(ngaydat,diachi,hoten,sdt,phuongthuc,tongtien,arrayList);
                                    dialog.cancel();break;
                                case 1:
                                    AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);

                                    requestPayment();
                                    dialog.cancel();
                                    break;

                            }
                            progressBar.setVisibility(View.VISIBLE);

                        }else{
                            Toast.makeText(CartActivity.this, "Số điện thoại không để trống", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(CartActivity.this, "Địa chỉ không để trống", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CartActivity.this, "Họ tên không để trống", Toast.LENGTH_SHORT).show();
                }

            }
        });




    }

    private void InitWidget() {
        rcVBill = findViewById(R.id.rcvBill);
        btnthanhtoan = findViewById(R.id.btnthanhtoan);
        progressBar= findViewById(R.id.progressbar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void OnSucess() {
       if(check == 0){
           Toast.makeText(CartActivity.this, "Đặt Hàng Thành Công!", Toast.LENGTH_SHORT).show();
       }else{
           Toast.makeText(CartActivity.this, "Thao tác thành công!", Toast.LENGTH_SHORT).show();
       }
        progressBar.setVisibility(View.GONE);
        sanPhamAdapter.notifyDataSetChanged();


    }

    @Override
    public void OnFail() {
        Toast.makeText(CartActivity.this, "Đặt Hàng thất bại !", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void getDataSanPham(String id, String idsp,String tensp, Long giatien, String hinhanh, String loaisp, Long soluong, String hansudung, Long type, String trongluong) {
        try{
            arrayList.add(new SanPhamModels(id,idsp,tensp,giatien,hinhanh,loaisp,soluong,hansudung,type,trongluong));
            sanPhamAdapter = new GioHangAdapter(CartActivity.this,arrayList,1);
            rcVBill.setLayoutManager(new LinearLayoutManager(CartActivity.this));
            rcVBill.setAdapter(sanPhamAdapter);
        }catch (Exception e){

        }
        progressBar.setVisibility(View.GONE);

    }
    //Thanh toán momo
    private void requestPayment() {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);
        Map<String, Object> eventValue = new HashMap<>();
        //client Required
        long mahd =   System.currentTimeMillis();
        eventValue.put("merchantname", "Thanh Nhan"); //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
        eventValue.put("merchantcode", "MOMO1NRV20220112"); //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
        eventValue.put("amount", tongtien); //Kiểu integer
        eventValue.put("orderId", "order"+mahd); //uniqueue id cho Bill order, giá trị duy nhất cho mỗi đơn hàng
        eventValue.put("orderLabel", "Mã đơn hàng"); //gán nhãn

        //client Optional - bill info
        eventValue.put("merchantnamelabel", "Dịch vụ");//gán nhãn
        eventValue.put("fee", tongtien); //Kiểu integer
        eventValue.put("description", "Mô tả"); //mô tả đơn hàng - short description

        //client extra data
        eventValue.put("requestId",  "MOMO1NRV20220112"+"merchant_billId_"+System.currentTimeMillis());
        eventValue.put("partnerCode", "MOMO1NRV20220112");
        //Example extra data
        JSONObject objExtraData = new JSONObject();
        try {
            objExtraData.put("site_code", "008");
            objExtraData.put("site_name", "Thanh Toán Đồ Điện Tử");
            objExtraData.put("screen_code", 0);
            objExtraData.put("screen_name", "Đặc Biệt");
            String name ="";
            for(SanPhamModels sanPham : arrayList){
                name+=sanPham.getTensp()+",";
            }
            objExtraData.put("movie_name", name);
            objExtraData.put("movie_format", "Đồ điện tử");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        eventValue.put("extraData", objExtraData.toString());

        eventValue.put("extra", "");
        AppMoMoLib.getInstance().requestMoMoCallBack(CartActivity.this, eventValue);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("CHECKED","checked1");
        if(requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            Log.d("CHECKED","checked2");
            if(data != null) {
                Log.d("CHECKED","checked3");
                if(data.getIntExtra("status", -1) == 0) {
                    //TOKEN IS AVAILABLE
                    Log.d("Messagesss","message: " + "Get token " + data.getStringExtra("message"));
                    String checked = data.getStringExtra("message");
                    Log.d("CHECKED",checked);
                    Calendar calendar=Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                    gioHangPreSenter.HandleAddHoaDon(simpleDateFormat.format(calendar.getTime()),diachi,hoten,sdt,spinner.getSelectedItem().toString(),tongtien,arrayList);
                    progressBar.setVisibility(View.GONE);


                    String token = data.getStringExtra("data"); //Token response
                    String phoneNumber = data.getStringExtra("phonenumber");
                    String env = data.getStringExtra("env");
                    if(env == null){
                        env = "app";
                    }

                    if(token != null && !token.equals("")) {
                        // TODO: send phoneNumber & token to your server side to process payment with MoMo server
                        // IF Momo topup success, continue to process your order
                    } else {
                        Log.d("Message Error : ","message: " + "Get token " + data.getStringExtra("message"));

                    }
                } else if(data.getIntExtra("status", -1) == 1) {
                    progressBar.setVisibility(View.GONE);
                    String message = data.getStringExtra("message") != null?data.getStringExtra("message"):"Thất bại";
                    Log.d("Message Fail : ","message: " + "Get token " + data.getStringExtra("message"));
                } else if(data.getIntExtra("status", -1) == 2) {
                    //TOKEN FAIL
                    Log.d("Message Fail 1 : ","message: " + "Get token " + data.getStringExtra("message"));
                } else {
                    //TOKEN FAIL
                    Log.d("Message Fail 2 : ","message: " + "Get token " + data.getStringExtra("message"));
                }
            } else {
                Log.d("Message Fail 3 : ","message: " + "Get token " + data.getStringExtra("message"));
            }
        } else {
            Log.d("Message Fail 4 : ","message: " + "Get token " + data.getStringExtra("message"));
        }
    }


}
