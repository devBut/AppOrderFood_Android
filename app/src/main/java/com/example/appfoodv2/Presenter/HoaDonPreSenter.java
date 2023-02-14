package com.example.appfoodv2.Presenter;

import com.example.appfoodv2.Model.HoaDonModels;

public class HoaDonPreSenter  implements IHoaDon{

    private HoaDonModels hoaDonModels;
    private HoaDonView callback;

    public HoaDonPreSenter(HoaDonView callback) {
        this.callback = callback;
        hoaDonModels = new HoaDonModels(this);
    }
    public  void HandleReadDataHD(){
        hoaDonModels.HandleReadData();
    }

    @Override
    public void getDataHD(String id, String uid, String diachi, String hoten, String ngaydat, String phuongthuc, String sdt, Long tongtien,Long type) {
        callback.getDataHD(id,uid,diachi,hoten,ngaydat,phuongthuc,sdt,tongtien,type);
    }

    @Override
    public void OnSucess() {
        callback.OnSucess();

    }

    @Override
    public void OnFail() {
      callback.OnFail();
    }

    public void CapNhatTrangThai(int i,String id) {
        hoaDonModels.HandleUpdateStatusBill(i,id);
    }

    public void HandleReadDataHD(int position) {
        hoaDonModels.HandleReadData(position);
    }
}
