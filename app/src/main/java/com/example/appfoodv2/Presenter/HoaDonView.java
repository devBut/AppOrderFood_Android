package com.example.appfoodv2.Presenter;

public interface HoaDonView {
    void getDataHD(String id, String uid, String diachi, String hoten, String ngaydat, String phuongthuc, String sdt, Long tongtien,Long type);

    void OnFail();

    void OnSucess();
}
