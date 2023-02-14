package com.example.appfoodv2.Model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.appfoodv2.Presenter.IGioHang;

import java.util.ArrayList;
import java.util.HashMap;

public class GioHangModels {
    private  String id;
    private  String id_sanpham;
    private  long soluong;
    private IGioHang callback;
    private FirebaseFirestore db;
    public  GioHangModels(){

    }

    public GioHangModels(String id, String id_sanpham, long soluong) {
        this.id = id;
        this.id_sanpham = id_sanpham;
        this.soluong = soluong;
    }

    public GioHangModels(String id_sanpham, long soluong) {
        this.id_sanpham = id_sanpham;
        this.soluong = soluong;
    }

    public  GioHangModels(IGioHang callback){
        this.callback=callback;
        db= FirebaseFirestore.getInstance();

    }
    //check giỏ hàng đúng id user
    public  void AddCart(String idsp){
        db.collection("GioHang").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("ALL").whereEqualTo("id_sanpham",idsp).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                Log.d("CHECKED",queryDocumentSnapshots.size()+"");
                if(queryDocumentSnapshots.size()!=0){
                    if(queryDocumentSnapshots.size()>0){
                       for(QueryDocumentSnapshot d : queryDocumentSnapshots){
                         long   soluong_sp = d.getLong("soluong");
                         //check so luong sp tăng lên 1
                         if(soluong_sp>0){
                             soluong_sp+=1;
                             db.collection("GioHang").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                     .collection("ALL").document(d.getId()).update("soluong",soluong_sp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {
                                     if(task.isSuccessful()){
                                      callback.OnSucess();
                                     }else{
                                         callback.OnFail();
                                     }
                                 }
                             });
                         }
                       }
                    }
                }else{
                    GioHangModels hangModels = new GioHangModels(idsp,1);
                    db.collection("GioHang").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .collection("ALL").add(hangModels).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){
                                callback.OnSucess();
                            }else {
                                callback.OnFail();
                            }
                        }
                    });
                }
            }
        });
    }
    public  void HandlegetDataGioHang(){
        db.collection("GioHang").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("ALL").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.size()>0){
                    for(QueryDocumentSnapshot s : queryDocumentSnapshots){
                        db.collection("SanPham").document(s.getString("id_sanpham"))
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(@NonNull DocumentSnapshot d) {

                                callback.getDataSanPham(s.getId(),s.getString("id_sanpham"),d.getString("tensp"),
                                        d.getLong("giatien"),d.getString("hinhanh"),
                                        d.getString("loaisp"),
                                        s.getLong("soluong"),d.getString("nhasanxuat"),
                                        d.getLong("type"),d.getString("trongluong"));


                            }
                        });
                    }
                }

            }
        });
    }
    public  void HandlegetDataGioHang(String id){
        db.collection("GioHang").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("ALL").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
               if(task.isSuccessful()){
                   callback.OnSucess();
               }else {
                   callback.OnFail();
               }
            }
        });
    }

    public long getSoluong() {
        return soluong;
    }

    public String getId() {
        return id;
    }

    public String getId_sanpham() {
        return id_sanpham;
    }

    public void HandleThanhToan(String ngaydat, String diachi, String hoten, String sdt, String phuongthuc, long tongtien, ArrayList<SanPhamModels> arrayList) {

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("ngaydat",ngaydat);
        hashMap.put("diachi",diachi);
        hashMap.put("sdt",sdt);
        hashMap.put("hoten",hoten);
        hashMap.put("phuongthuc",phuongthuc);
        hashMap.put("tongtien",tongtien);
        hashMap.put("trangthai",1);
        hashMap.put("UID",FirebaseAuth.getInstance().getCurrentUser().getUid());
        db.collection("HoaDon")
                .add(hashMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    for(SanPhamModels sanPhamModels : arrayList){
                        HashMap<String,Object> map_chitiet = new HashMap<>();
                        map_chitiet.put("id_hoadon",task.getResult().getId());
                        map_chitiet.put("id_sanpham",sanPhamModels.getIdsp());
                        map_chitiet.put("soluong",sanPhamModels.getSoluong());

                        db.collection("ChitietHoaDon").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .collection("ALL").add(map_chitiet).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if(task.isSuccessful()){
                                    db.collection("GioHang").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .collection("ALL").document(sanPhamModels.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                callback.OnSucess();
                                            }else{
                                                callback.OnFail();
                                            }
                                        }
                                    });
                                }

                            }
                        });

                    }

                }else{

                }

            }
        });
    }

    public void HandleGetDataCTHD(String id) {

        db.collection("ChitietHoaDon").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("ALL").whereEqualTo("id_hoadon",id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.size()>0){
                    for(QueryDocumentSnapshot s : queryDocumentSnapshots){
                        Log.d("CHECKED",s.getString("id_sanpham"));
                        db.collection("SanPham").document(s.getString("id_sanpham"))
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(@NonNull DocumentSnapshot d) {
                                callback.getDataSanPham(s.getId(),s.getString("id_sanpham"),d.getString("tensp"),
                                        d.getLong("giatien"),d.getString("hinhanh"),
                                        d.getString("loaisp"),
                                        s.getLong("soluong"),d.getString("nhasanxuat"),
                                        1l,d.getString("trongluong"));
                            }
                        });
                    }
                }

            }
        });
    }
    public void HandleGetDataCTHD(String id,String uid) {

         if(uid!=null){
             db.collection("ChitietHoaDon").document(uid)
                     .collection("ALL").whereEqualTo("id_hoadon",id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                 @Override
                 public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                     if(queryDocumentSnapshots.size()>0){
                         for(QueryDocumentSnapshot s : queryDocumentSnapshots){
                             Log.d("CHECKED",s.getString("id_sanpham"));
                             db.collection("SanPham").document(s.getString("id_sanpham"))
                                     .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                 @Override
                                 public void onSuccess(@NonNull DocumentSnapshot d) {
                                     callback.getDataSanPham(s.getId(),s.getString("id_sanpham"),d.getString("tensp"),
                                             d.getLong("giatien"),d.getString("hinhanh"),
                                             d.getString("loaisp"),
                                             s.getLong("soluong"),d.getString("nhasanxuat"),
                                             1l,d.getString("trongluong"));
                                 }
                             });
                         }
                     }

                 }
             });

         }else{

         }


    }
}
