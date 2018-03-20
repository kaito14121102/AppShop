package com.anime.rezero.appshop.model;

/**
 * Created by zing on 2/11/2018.
 */

public class GioHangModel {
    public int id;
    public String tensanpham;
    public Long giasanpham;
    public String hinhanhsanpham;
    public int soluong;

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public GioHangModel(int id, String tensanpham, Long giasanpham, String hinhanhsanpham, int soluong) {
        this.id = id;
        this.tensanpham = tensanpham;
        this.giasanpham = giasanpham;
        this.hinhanhsanpham = hinhanhsanpham;
        this.soluong=soluong;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTensanpham() {
        return tensanpham;
    }

    public void setTensanpham(String tensanpham) {
        this.tensanpham = tensanpham;
    }

    public Long getGiasanpham() {
        return giasanpham;
    }

    public void setGiasanpham(Long giasanpham) {
        this.giasanpham = giasanpham;
    }

    public String getHinhanhsanpham() {
        return hinhanhsanpham;
    }

    public void setHinhanhsanpham(String hinhanhsanpham) {
        this.hinhanhsanpham = hinhanhsanpham;
    }
}
