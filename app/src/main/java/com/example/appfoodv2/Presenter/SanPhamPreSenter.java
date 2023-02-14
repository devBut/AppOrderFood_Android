package com.example.appfoodv2.Presenter;

import com.example.appfoodv2.Model.SanPhamModels;

public class SanPhamPreSenter implements  ISanPham{
    private SanPhamModels sanPhamModels;
    private  SanPhamView callback;
    public   SanPhamPreSenter(SanPhamView callback){
        this.callback=callback;
        sanPhamModels = new SanPhamModels(this);

    }
    public void HandlegetDataSanPham(){
        sanPhamModels.HandlegetDataSanPham();
    }

    public void HandlegetDataSanPhamTU(){
        sanPhamModels.HandlegetDataSanPhamThucUong();
    }
    public void HandlegetDataSanPhamHQ(){
        sanPhamModels.HandlegetDataSanPhamHanQuoc();
    }

    public void HandlegetDataSanPhamMC(){
        sanPhamModels.HandlegetDataSanPhamMiCay();
    }
    public void HandlegetDataSanPhamYT(){
        sanPhamModels.HandlegetDataSanPhamYeuThich();
    }
    public void HandlegetDataSanPhamLau(){
        sanPhamModels.HandlegetDataSanPhamLau();
    }
    public void HandlegetDataSanPhamGY(){
        sanPhamModels.HandlegetDataSanPhamGoiY();
    }
    @Override
    public void getDataSanPham(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong,
                               String hansudung, Long type,String trongluong) {
       callback.getDataSanPham(id,tensp,giatien,hinhanh,loaisp,mota,soluong,hansudung,type,trongluong);
    }
    @Override
    public void getDataSanPhamTU(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong,
                               String hansudung, Long type,String trongluong) {
        callback.getDataSanPhamTU(id,tensp,giatien,hinhanh,loaisp,mota,soluong,hansudung,type,trongluong);
    }
    @Override
    public void getDataSanPhamHQ(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong,
                                 String hansudung, Long type,String trongluong) {
        callback.getDataSanPhamHQ(id,tensp,giatien,hinhanh,loaisp,mota,soluong,hansudung,type,trongluong);
    }
    @Override
    public void getDataSanPhamMC(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong,
                                 String hansudung, Long type,String trongluong) {
        callback.getDataSanPhamMC(id,tensp,giatien,hinhanh,loaisp,mota,soluong,hansudung,type,trongluong);
    }
    @Override
    public void getDataSanPhamYT(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong,
                                 String hansudung, Long type,String trongluong) {
        callback.getDataSanPhamYT(id,tensp,giatien,hinhanh,loaisp,mota,soluong,hansudung,type,trongluong);
    }
    @Override
    public void getDataSanPhamLau(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong,
                                 String hansudung, Long type,String trongluong) {
        callback.getDataSanPhamLau(id,tensp,giatien,hinhanh,loaisp,mota,soluong,hansudung,type,trongluong);
    }
    @Override
    public void getDataSanPhamGY(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong,
                                 String hansudung, Long type,String trongluong) {
        callback.getDataSanPhamGY(id,tensp,giatien,hinhanh,loaisp,mota,soluong,hansudung,type,trongluong);
    }
    @Override
    public void OnEmptyList() {
        callback.OnEmptyList();
    }
// truyen dữ liệu qua màn hình
    @Override
    public void getDataSanPhamNB(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String hansudung, Long type, String trongluong) {
        callback.getDataSanPhamNB(id,tensp,giatien,hinhanh,loaisp,mota,soluong,hansudung,type,trongluong);
    }

    public void HandlegetDataSanPham(String loaisp,int type) {
        sanPhamModels.HandlegetDataSanPham(loaisp,type);
    }


    public void HandlegetDataSanPhamNB() {
        sanPhamModels.HandlegetDataSanPhamNoiBat();
    }

}
