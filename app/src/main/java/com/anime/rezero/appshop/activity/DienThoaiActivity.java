package com.anime.rezero.appshop.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.anime.rezero.appshop.adapter.SanPhamAdapter;
import com.anime.rezero.appshop.model.SanPham;
import com.anime.rezero.appshop.ultil.CheckConnection;
import com.anime.rezero.appshop.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DienThoaiActivity extends AppCompatActivity {
    Toolbar toolbarDienThoai;
    RecyclerView recyclerView;
    DienThoaiAdapter dienThoaiAdapter;
    ArrayList<SanPham> arrayDT;
    int iddt=0;
    int page=1;
    View footerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dien_thoai);
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
                if (response != null) {
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
        recyclerView = (RecyclerView) findViewById(R.id.recycle_viewdt);
        arrayDT = new ArrayList<>();

        dienThoaiAdapter = new DienThoaiAdapter(getApplicationContext(), arrayDT);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerView.setAdapter(dienThoaiAdapter);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerView=inflater.inflate(R.layout.processbar,null);
    }
}
