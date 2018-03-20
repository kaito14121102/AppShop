package com.anime.rezero.appshop.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.anime.rezero.appshop.R;
import com.anime.rezero.appshop.model.GioHangModel;
import com.anime.rezero.appshop.model.SanPham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class ChiTietSanPham extends AppCompatActivity {
    Toolbar toolbarChiTiet;
    ImageView imgChiTiet;
    TextView txtTenChiTiet,txtGiaChiTiet,txtMota;
    Spinner spinner;
    Button btnMuaHang;
    SanPham sanPham;
    int id=0;
    String ten ="";
    int gia =0;
    String hinhanh="";
    String mota="";
    int idsp=0;
    boolean exist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);
        initWidget();
        ActionToolbar();
        GetInformation();
        CatchEvenSpiner();
        EvenButton();

    }

    private void EvenButton() {
        btnMuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.manggiohang.size()>0){
                    int soluong =Integer.parseInt(spinner.getSelectedItem().toString());
                    for (int i = 0 ; i <MainActivity.manggiohang.size();i++){
                        if(MainActivity.manggiohang.get(i).getId()==id){
                            MainActivity.manggiohang.get(i).setSoluong(MainActivity.manggiohang.get(i).getSoluong()+soluong);
                            if(MainActivity.manggiohang.get(i).getSoluong()>10){
                                MainActivity.manggiohang.get(i).setSoluong(10);
                            }
                            MainActivity.manggiohang.get(i).setGiasanpham((long)gia * MainActivity.manggiohang.get(i).getSoluong());
                            exist=true;
                        }
                    }
                    if(exist==false){
                        long giamoi = soluong* gia;
                        MainActivity.manggiohang.add(new GioHangModel(id,ten,giamoi,hinhanh,soluong));
                    }
                }else {
                    int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
                    long giamoi = soluong* gia;
                    MainActivity.manggiohang.add(new GioHangModel(id,ten,giamoi,hinhanh,soluong));
                }
                Intent intent = new Intent(ChiTietSanPham.this,GioHang.class);
                startActivity(intent);

            }
        });
    }

    private void CatchEvenSpiner() {
        Integer[] soluong = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_dropdown_item,soluong);
        spinner.setAdapter(arrayAdapter);
    }

    private void GetInformation() {

        sanPham = (SanPham) getIntent().getSerializableExtra("thongtinsanpham");
        id = sanPham.getId();
        ten = sanPham.getTensanpham();
        gia = sanPham.getGiasanpham();
        hinhanh = sanPham.getHinhanhsanpham();
        mota = sanPham.getMotasanpham();
        idsp = sanPham.getIdSanPham();
        txtTenChiTiet.setText(ten);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtGiaChiTiet.setText("Giá : "+decimalFormat.format(gia)+" Đ");
        txtMota.setText(mota);
        Picasso.with(getApplicationContext()).load(hinhanh)
                .placeholder(R.drawable.camera)
                .error(R.drawable.error)
                .into(imgChiTiet);
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbarChiTiet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarChiTiet.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initWidget(){
        toolbarChiTiet = (Toolbar) findViewById(R.id.tool_bar_chitietsp);
        imgChiTiet = (ImageView) findViewById(R.id.img_chitiet);
        txtTenChiTiet = (TextView) findViewById(R.id.txt_tenchitietsp);
        txtGiaChiTiet = (TextView) findViewById(R.id.txt_chitietgiasp);
        txtMota = (TextView) findViewById(R.id.txt_mota);
        spinner = (Spinner) findViewById(R.id.spiner);
        btnMuaHang = (Button) findViewById(R.id.btn_muahang);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_giohang :
                Intent intent = new Intent(getApplicationContext(),GioHang.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
