package com.anime.rezero.appshop.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anime.rezero.appshop.R;
import com.anime.rezero.appshop.adapter.DienThoaiAdapter;
import com.anime.rezero.appshop.adapter.DienThoaiAdapter1;
import com.anime.rezero.appshop.model.SanPham;
import com.anime.rezero.appshop.ultil.CheckConnection;
import com.anime.rezero.appshop.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

public class DienThoaiActivity1 extends AppCompatActivity {
    Toolbar toolbarDienThoai;
    GridViewWithHeaderAndFooter gridView;
    ListView lv;
    DienThoaiAdapter1 dienThoaiAdapter;
    ArrayList<SanPham> arrayDT;
    int iddt=0;
    int page=1;
    boolean isLoading = false;
    View footerView;
    mHanler mHanler;
    boolean limitData =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dien_thoai1);
        initWidget();
        if (CheckConnection.haveNetworkConnection(getApplicationContext())){
            GetIdloaisp();
            ActionToolBar();
            GetData(page);
            LoadMoreData();
        }else {
            CheckConnection.ShowToast_short(getApplicationContext(),"Bạn hãy kiểm tra lại kết nối");
            finish();
        }
    }
    private void LoadMoreData() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DienThoaiActivity1.this,ChiTietSanPham.class);
                intent.putExtra("thongtinsanpham",arrayDT.get(position));
                startActivity(intent);
            }
        });
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem+visibleItemCount==totalItemCount && totalItemCount!=0 &&isLoading==false &&limitData==false){
                    isLoading=true;
                    ThreadData threadData = new ThreadData();
                    threadData.start();
                }
            }
        });
    }

    private void GetData(int page) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Server.urlDienThoai+String.valueOf(page);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int ID = 0;
                String tensanpham = "";
                Integer giasanpham = 0;
                String hinhanhsanpham = "";
                String motasanpham = "";
                int IDsanpham = 0;
                if (response != null && response.length()!=2) { // luon tra ve cap ngoac vuông nên luôn có 2 phần tử
                    gridView.removeFooterView(footerView);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for(int i =0 ;i <jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            ID = jsonObject.getInt("id");
                            tensanpham = jsonObject.getString("tensp");
                            giasanpham = jsonObject.getInt("giasp");
                            hinhanhsanpham = jsonObject.getString("hinhanhsp");
                            motasanpham = jsonObject.getString("motasp");
                            IDsanpham = jsonObject.getInt("idsp");
                            arrayDT.add(new SanPham(ID, tensanpham, giasanpham, hinhanhsanpham, motasanpham, IDsanpham));
                            dienThoaiAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    limitData=true;
                    gridView.removeFooterView(footerView);
                    Toast.makeText(DienThoaiActivity1.this, "Đã hết dữ liệu", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<String, String>();
                param.put("idsanpham",String.valueOf(iddt));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbarDienThoai);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarDienThoai.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void GetIdloaisp() {
        iddt = getIntent().getIntExtra("idloaisp",-1);
        Log.d("giatriloaisp",iddt+"");
    }

    public void initWidget(){
        toolbarDienThoai = (Toolbar) findViewById(R.id.toolbar_dienthoai);
        gridView = (GridViewWithHeaderAndFooter) findViewById(R.id.grid_view);
        arrayDT = new ArrayList<>();
        dienThoaiAdapter = new DienThoaiAdapter1(getApplicationContext(),R.layout.dong_dien_thoai, arrayDT);
        gridView.setAdapter(dienThoaiAdapter);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerView=inflater.inflate(R.layout.processbar,null);
        mHanler = new mHanler();
    }

    public  class mHanler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
//                    gridView.addFooterView(footerView);
                    break;
                case 1:
                    GetData(++page);
                    isLoading =false;
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public class ThreadData extends Thread{
        @Override
        public void run() {
            mHanler.sendEmptyMessage(0);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = mHanler.obtainMessage(1);//Phương thức liên kết thread vs handle
            mHanler.sendEmptyMessage(1);
            super.run();
        }
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
