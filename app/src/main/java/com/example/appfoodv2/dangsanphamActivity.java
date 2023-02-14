package com.example.appfoodv2;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.example.appfoodv2.Model.SanPhamModels;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class dangsanphamActivity extends AppCompatActivity {
    private static final int LIBRARY_PICKER = 12312;
    EditText edtNsx, edtTenSp, edtTien, edtBh, edtSl, edtType, edtMt;
    ImageView imageView;
    Button btnDm, btnDel, btnEdit;
    private Spinner spinerthongke;
    private List<String> list;
    FirebaseFirestore db;
    private String image = "";
    ProgressDialog dialog;
    private SanPhamModels sanPhamModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangsanpham);
        initView();
        db = FirebaseFirestore.getInstance();
        list = new ArrayList<>();
        list.add("Chọn Danh Mục");
        if (getIntent() != null && getIntent().hasExtra("SP")) {
            sanPhamModels = (SanPhamModels) getIntent().getSerializableExtra("SP");
        }
        if (sanPhamModels != null) {
            edtBh.setText(sanPhamModels.getTrongluong());
            edtMt.setText(sanPhamModels.getMota());
            edtNsx.setText(sanPhamModels.getHansudung());
            edtSl.setText(sanPhamModels.getSoluong() + "");
            edtTien.setText(sanPhamModels.getGiatien() + "");
            edtTenSp.setText(sanPhamModels.getTensp());
            edtType.setText(sanPhamModels.getType()+"");
            btnEdit.setVisibility(View.VISIBLE);
            btnDel.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(sanPhamModels.getHinhanh())) {
                Picasso.get().load(sanPhamModels.getHinhanh()).into(imageView);
                image = sanPhamModels.getHinhanh();
            }
        }
        db.collection("LoaiSP").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
                    list.add(q.getString("tenloai"));
                    Log.d("TAG", "onSuccess: " + q.getString("tenloai"));
                }
                ArrayAdapter arrayAdapter = new ArrayAdapter(dangsanphamActivity.this, android.R.layout.simple_list_item_1, list);
                spinerthongke.setAdapter(arrayAdapter);
                if (list.size() > 0) {
                    spinerthongke.setSelection(1);
                    if (sanPhamModels != null) {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).equals(sanPhamModels.getLoaisp())) {
                                spinerthongke.setSelection(i);
                                break;
                            }
                        }
                    }
                }

            }
        });
    }

    private void initView() {
        dialog = new ProgressDialog(this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Upload image");
        dialog.setMessage("Uploading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        edtBh = findViewById(R.id.edt_thuonghieu);
        edtNsx = findViewById(R.id.edt_nhasanxuat);
        edtTenSp = findViewById(R.id.edt_tensp);
        edtTien = findViewById(R.id.edt_giatien);
        edtSl = findViewById(R.id.edt_xuatxu);
        edtType = findViewById(R.id.edt_type);
        edtMt = findViewById(R.id.edt_mota);
        imageView = findViewById(R.id.image_add);
        spinerthongke = findViewById(R.id.spinner);
        btnDm = findViewById(R.id.btn_danhmuc);
        btnDel = findViewById(R.id.btn_delete);
        btnEdit = findViewById(R.id.btn_edit);
        btnDm.setOnClickListener(view -> spinerthongke.performClick());
        imageView.setOnClickListener(view -> {
            pickImage();
        });
        spinerthongke.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    btnDm.setText(spinerthongke.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.btn_refresh).setOnClickListener(view -> {
            edtBh.setText("");
            edtNsx.setText("");
            edtTenSp.setText("");
            edtTien.setText("");
            edtType.setText("");
            edtSl.setText("");
            edtMt.setText("");
            image = "";
            imageView.setImageResource(R.drawable.pl);
        });
        findViewById(R.id.btn_save).setOnClickListener(view -> {
            if (!validate()) {
                return;
            }
            try {
                SanPhamModels sp = new SanPhamModels();
                sp.setGiatien(Long.parseLong(edtTien.getText().toString()));
                sp.setMota(edtMt.getText().toString());
                sp.setHansudung(edtNsx.getText().toString());
                sp.setType(Long.parseLong(edtType.getText().toString()));
                sp.setTensp(edtTenSp.getText().toString());
                sp.setSoluong(Long.parseLong(edtSl.getText().toString()));
                sp.setTrongluong(edtBh.getText().toString());
                sp.setLoaisp(spinerthongke.getSelectedItem().toString());
                sp.setHinhanh(image);
                db.collection("SanPham").add(sp).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(@NonNull DocumentReference documentReference) {
                        Toast.makeText(dangsanphamActivity.this, "Thành công!!!", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(dangsanphamActivity.this, "Thất bại!!!", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        btnDel.setOnClickListener(view -> {
            db.collection("SanPham").document(sanPhamModels.getId()).delete().addOnSuccessListener(unused -> {
                Toast.makeText(dangsanphamActivity.this, "Xoá sản phẩm thành công!!!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }).addOnFailureListener(e -> {
                Toast.makeText(dangsanphamActivity.this, "Xoá sản phẩm thất bại!!!", Toast.LENGTH_SHORT).show();
            });

        });

        btnEdit.setOnClickListener(view -> {
            if (!validate()) {
                return;
            }
            try {
                SanPhamModels sp = new SanPhamModels();
                sp.setGiatien(Long.parseLong(edtTien.getText().toString()));
                sp.setMota(edtMt.getText().toString());
                sp.setHansudung(edtNsx.getText().toString());
                sp.setType(Long.parseLong(edtType.getText().toString()));
                sp.setTensp(edtTenSp.getText().toString());
                sp.setSoluong(Long.parseLong(edtSl.getText().toString()));
                sp.setTrongluong(edtBh.getText().toString());
                sp.setLoaisp(spinerthongke.getSelectedItem().toString());
                sp.setHinhanh(image);
                db.collection("SanPham").document(sanPhamModels.getId()).set(sp)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(@NonNull Void unused) {
                                Toast.makeText(dangsanphamActivity.this, "Cập nhật sản phẩm thành công!!!", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(dangsanphamActivity.this, "Cập nhật sản phẩm thất bại!!!", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private boolean validate() {
        if (TextUtils.isEmpty(image)) {
            Toast.makeText(this, "Vui lòng chọn hình ảnh", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(edtTien.getText().toString())) {
            Toast.makeText(this, "Vui lòng nhập giá tiền", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(edtTenSp.getText().toString())) {
            Toast.makeText(this, "Vui lòng nhập tên sản phẩm", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(edtNsx.getText().toString())) {
            Toast.makeText(this, "Vui lòng nhập nhà sản xuất", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(edtBh.getText().toString())) {
            Toast.makeText(this, "Vui lòng nhập bảo hành", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(edtSl.getText().toString())) {
            Toast.makeText(this, "Vui lòng nhập số lượng", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(edtType.getText().toString())) {
            Toast.makeText(this, "Vui lòng nhập type", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(edtMt.getText().toString())) {
            Toast.makeText(this, "Vui lòng nhập mô tả", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void pickImage() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    LIBRARY_PICKER);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(intent, "Select Image"), LIBRARY_PICKER);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // pick image after request permission success
            pickImage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LIBRARY_PICKER && resultCode == RESULT_OK && null != data) {
            try {

                dialog.show();


                Uri uri = data.getData();
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] datas = baos.toByteArray();
                String filename = System.currentTimeMillis() + "";
                StorageReference storageReference;
                storageReference = FirebaseStorage.getInstance("gs://doan-dc57a.appspot.com/").getReference();
                storageReference.child("Profile").child(filename + ".jpg").putBytes(datas).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        if (taskSnapshot.getTask().isSuccessful()) {
                            storageReference.child("Profile").child(filename + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(@NonNull Uri uri) {
                                    imageView.setImageBitmap(bitmap);
                                    image = uri.toString();
                                }
                            });
                            Toast.makeText(dangsanphamActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
            } catch (FileNotFoundException e) {
                Log.d("CHECKED", e.getMessage());
                dialog.dismiss();
            }

        }


    }
}