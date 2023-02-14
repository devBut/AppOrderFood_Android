package com.example.appfoodv2.View.Admin;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.appfoodv2.R;

import java.util.ArrayList;

public class ChartBillActivity  extends AppCompatActivity  {
    private PieChart pieChart;
    private  float dangxuly = 0,danggiaohang=0,giaohangthanhcong=0,huyhang=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_bill);
        Toolbar toolbar = findViewById(R.id.toolbar);
        pieChart = findViewById(R.id.piechart);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Back");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ArrayList<PieEntry> barEntries = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("HoaDon").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot q : queryDocumentSnapshots){
                    if(q.getLong("trangthai")==1){
                        dangxuly++;
                    }else if(q.getLong("trangthai")==2){
                        danggiaohang++;
                    }else if(q.getLong("trangthai")==3){
                        giaohangthanhcong++;
                    }else{
                        huyhang++;
                    }
                }
                barEntries.add(new PieEntry(dangxuly,"Đang Xử  Lý"));
                barEntries.add(new PieEntry(danggiaohang,"Đang giao hàng"));
                barEntries.add(new PieEntry(giaohangthanhcong,"Đã nhận hàng"));
                barEntries.add(new PieEntry(huyhang,"Hủy hàng"));
                ArrayList<Integer> arrayList=new ArrayList<>();

                for(int color : ColorTemplate.MATERIAL_COLORS){
                    arrayList.add(color);
                }
                for(int color : ColorTemplate.VORDIPLOM_COLORS){
                    arrayList.add(color);
                }
                Legend l = pieChart.getLegend();

                l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                l.setOrientation(Legend.LegendOrientation.VERTICAL);
                PieDataSet pieDataSet = new PieDataSet(barEntries,"Biểu Đồ Trạng Thái Đơn Hàng");
                pieDataSet.setColors(arrayList);
                PieData pieData = new PieData(pieDataSet);
                pieData.setDrawValues(true);
                pieData.setValueFormatter(new PercentFormatter());
                pieData.setValueTextSize(20);
                pieChart.setData(pieData);
                pieChart.invalidate();
                pieChart.setDrawEntryLabels(true);
                pieChart.setUsePercentValues(true);
                pieChart.setEntryLabelTextSize(20);
                pieChart.setEntryLabelColor(Color.WHITE);
                pieChart.setCenterText("Trạng Thái Đơn Hàng");

                pieChart.getDescription().setEnabled(false);

            }
        });











    }
}
