package com.example.appfoodv2.Presenter;

public interface SanPhamView {
    void getDataSanPham(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong,
                        String hansudung, Long type,String trongluong);
    void OnEmptyList();
    void getDataSanPhamNB(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String hansudung, Long type, String trongluong);
    void getDataSanPhamTU(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String hansudung, Long type, String trongluong);
    void getDataSanPhamHQ(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String hansudung, Long type, String trongluong);
    void getDataSanPhamMC(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String hansudung, Long type, String trongluong);
    void getDataSanPhamYT(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String hansudung, Long type, String trongluong);
    void getDataSanPhamLau(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String hansudung, Long type, String trongluong);
    void getDataSanPhamGY(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String hansudung, Long type, String trongluong);

}
