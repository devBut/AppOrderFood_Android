package com.example.appfoodv2.Presenter;

public interface IGioHang {
    void OnSucess();

    void OnFail();

    void getDataSanPham(String id, String id_sp,String tensp, Long giatien, String hinhanh, String loaisp, Long soluong, String hansudung, Long type, String trongluong);
}
