package com.example.appfoodv2.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.example.appfoodv2.R;
import com.example.appfoodv2.ThongtinungdungFragment;
import com.example.appfoodv2.View.Account.SignInActivity;
import com.example.appfoodv2.View.Bill.CartActivity;
import com.example.appfoodv2.View.FragMent.FragMent_Bill;
import com.example.appfoodv2.View.FragMent.FragMent_Home;
import com.example.appfoodv2.View.FragMent.FragMent_ProFile;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity  extends AppCompatActivity implements FragMent_Home.FragMent_HomeListener {
    private NavigationView navigationView;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Fragment fm;
    private FirebaseAuth firebaseAuth;
    private EditText editsearch;
    private TextView tvusername,tvemail;
    private CircleImageView imaProfile;

    public  static CountDownTimer countDownTimer;

    private FirebaseFirestore db;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        InitWidget();
        Init();
        setProFile();
    }

    private void setProFile() {
        db= FirebaseFirestore.getInstance();
        tvemail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        db.collection("thongtinUser").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("Profile")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.size()>0){
                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                    if(documentSnapshot!=null){
                        try{
                            tvusername.setText(documentSnapshot.getString("hoten").length()>0 ?
                                    documentSnapshot.getString("hoten") : "");

                            if(documentSnapshot.getString("avatar").length()>0){
                                Picasso.get().load(documentSnapshot.getString("avatar").trim()).into(imaProfile);
                            }
                        }catch (Exception e){
                            Log.d("ERROR",e.getMessage());
                        }
                    }
                }
            }
        });
    }

    private void Init() {    // custom thanh toolbar
        setSupportActionBar(toolbar); //lay thanh toolbar
        getSupportActionBar(). setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.Open,
                R.string.Close);
        toggle.syncState();
        fm = new FragMent_Home();
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fm).commit();

         //Check user phân quyền tk đang nhap va chua dang nhap
         firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser f = firebaseAuth.getCurrentUser();
        if(f!=null){ // chua dang nhap
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.menu_logined);
        }else{ // da dang nhap chuyen sang menu chính
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.menu_dashboard);
        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) { //ánh xạ view và bắt sự kiện
                switch (item.getItemId()){
                    case  R.id.home: fm = new FragMent_Home();break;
                    case  R.id.dangnhap:startActivity(new Intent( HomeActivity.this, SignInActivity.class));break;
                    case  R.id.lienhe:startActivity(new Intent( HomeActivity.this, ContactActivity.class));break;
                    case  R.id.your_bill:fm=new FragMent_Bill();break;
                    case  R.id.your_cart:startActivity(new Intent(HomeActivity.this, CartActivity.class));break;
                    case  R.id.your_profile:fm = new FragMent_ProFile();break;
                    case  R.id.signout:FirebaseAuth.getInstance().signOut();startActivity(new Intent(HomeActivity.this,SignInActivity.class));finish();break;
                    case R.id.danhmuc: startActivity(new Intent( HomeActivity.this,ThongKeDanhMucActivity.class));break;
                    case  R.id.thongtinungdung:fm=new ThongtinungdungFragment();break;

                }
                if(fm!=null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fm).commit();
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
//search
        editsearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    String key = editsearch.getText().toString().trim();
                     if(key.length()>0){
                         startActivity(new Intent(HomeActivity.this,ThongKeDanhMucActivity.class).putExtra("KEY",key));
                     }else{
                         Toast.makeText(HomeActivity.this, "Tên sản phẩm không để trống", Toast.LENGTH_SHORT).show();
                     }
                }
                return true;
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        editsearch.setText("");
        if(countDownTimer!=null){
            countDownTimer.start();
        }
    }

    private void InitWidget() {
        navigationView = findViewById(R.id.navigationview);
        View headerLayout = navigationView.getHeaderView(0);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout= findViewById(R.id.drawerlayout);
        editsearch = findViewById(R.id.editSearch);
        tvusername = headerLayout.findViewById(R.id.tvusername);
        tvemail =headerLayout. findViewById(R.id.tvemail);
        imaProfile =headerLayout. findViewById(R.id.profile_image);

    }

    @Override
    public void onButtonClick() {
        Intent intent = new Intent(HomeActivity.this,ThongKeDanhMucActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setProFile();
    }
}
