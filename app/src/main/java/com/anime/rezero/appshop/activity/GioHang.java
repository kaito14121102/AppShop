package com.anime.rezero.appshop.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anime.rezero.appshop.R;
import com.anime.rezero.appshop.adapter.GioHangAdapter;
import com.anime.rezero.appshop.model.SanPham;
import com.anime.rezero.appshop.ultil.CheckConnection;
import com.anime.rezero.appshop.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GioHang extends AppCompatActivity {
    ListView listView;
    Toolbar toolbar;
    static TextView txtTongTien;
    GioHangAdapter adapter;
    Button btnThanhToan;
    EditText editTen,editSDT,editYeuCauKhac;
    TextInputLayout inputName,inputSDT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        initWidget();
        ActionToolbar();
        SetTongTien();
        //CacthOnItemListView();
        if(CheckConnection.haveNetworkConnection(getApplication())){
            EvenButtonThanhToan();
        }else {
            CheckConnection.ShowToast_short(getApplicationContext(),"Bạn hãy kiểm tra lại kết nối!");
        }

    }

    private void EvenButtonThanhToan() {
        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.manggiohang.size()>0){
                    final String ten = editTen.getText().toString().trim();
                    final String sdt = editSDT.getText().toString().trim();
                    final String yeucaukhac = editYeuCauKhac.getText().toString().trim();
                    if(ten.length()>0&&sdt.length()>0){
                        RequestQueue requestQueue  = Volley.newRequestQueue(getApplicationContext());
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlDonHang, new Response.Listener<String>() {
                            @Override
                            public void onResponse(final String madonhang) {
                                Log.d("madonhang",madonhang);
                                if(Integer.parseInt(madonhang) >0){
                                    RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                                    StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Server.urlChiTietDonHang, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            if(response.equals("1")){
                                                MainActivity.manggiohang.clear();
                                                CheckConnection.ShowToast_short(getApplicationContext(),"Bạn đã thêm dữ liệu giỏ hàng thành công");
                                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                                startActivity(intent);
                                                CheckConnection.ShowToast_short(getApplicationContext(),"Mời bạn tiếp tục mua hàng");
                                            }else {
                                                CheckConnection.ShowToast_short(getApplicationContext(),"Dữ liệu giỏ hàng của bạn đã bị lỗi");
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    }){
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            JSONArray jsonArray = new JSONArray();
                                            for (int i = 0 ; i <MainActivity.manggiohang.size();i++){
                                                JSONObject jsonObject = new JSONObject();
                                                try {
                                                    jsonObject.put("madonhang",madonhang);
                                                    jsonObject.put("masanpham",MainActivity.manggiohang.get(i).getId());
                                                    jsonObject.put("tensanpham",MainActivity.manggiohang.get(i).getTensanpham());
                                                    jsonObject.put("giasanpham",MainActivity.manggiohang.get(i).getGiasanpham());
                                                    jsonObject.put("soluongsanpham",MainActivity.manggiohang.get(i).getSoluong());
                                                    jsonArray.put(jsonObject);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            HashMap<String,String> hashMap = new HashMap<String, String>();
                                            hashMap.put("json",jsonArray.toString());
                                            return hashMap;
                                        }
                                    };
                                    requestQueue1.add(stringRequest1);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String,String> hashmap = new HashMap<String, String>();
                                hashmap.put("tenkhachhang",ten);
                                hashmap.put("sodienthoai",sdt);
                                hashmap.put("yeucaukhac",yeucaukhac);
                                return hashmap;
                            }
                        };
                        requestQueue.add(stringRequest);
                    }else {
                        Toast.makeText(GioHang.this, "Hãy kiểm tra lại dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(GioHang.this, "Giỏ hàng của bạn chưa có sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void CacthOnItemListView() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(GioHang.this, "dđ", Toast.LENGTH_SHORT).show();
            }
        });
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//                Toast.makeText(GioHang.this, "fff", Toast.LENGTH_SHORT).show();
//                AlertDialog.Builder builder = new  AlertDialog.Builder(GioHang.this);
//                builder.setTitle("Xác nhận xóa sản phẩm");
//                builder.setMessage("Bạn có chắc muốn xóa sản phẩm này");
//                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        MainActivity.manggiohang.remove(position);
//                        adapter.notifyDataSetChanged();
//                        SetTongTien();
//                    }
//                });
//
//                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        adapter.notifyDataSetChanged();
//                        SetTongTien();
//                    }
//                });
//                builder.show();
//                return true;
//            }
//        });
    }

    public static void SetTongTien() {
        long tongtien=0;
        for(int i = 0 ; i <MainActivity.manggiohang.size();i++){
            tongtien += MainActivity.manggiohang.get(i).getGiasanpham();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtTongTien.setText(decimalFormat.format(tongtien)+"đ");
    }



    private void initWidget() {
        listView = (ListView) findViewById(R.id.list_giohang);
        toolbar = (Toolbar) findViewById(R.id.tool_bargiohang);
//        btnCong = (Button) findViewById(R.id.btn_giohangcong);
//        btnTru = (Button) findViewById(R.id.btn_giohangtru);
        editTen = (EditText) findViewById(R.id.edit_ten);
        editSDT = (EditText) findViewById(R.id.edit_sdt);
        inputName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputSDT = (TextInputLayout) findViewById(R.id.input_layout_sdt);
        editYeuCauKhac = (EditText) findViewById(R.id.edit_yeucaukhac);
        btnThanhToan = (Button) findViewById(R.id.btn_giohangthanhtoan);
        txtTongTien = (TextView) findViewById(R.id.txt_giohangtongtien);
        adapter = new GioHangAdapter(GioHang.this,R.layout.dong_gio_hang,MainActivity.manggiohang);
        listView.setAdapter(adapter);
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
