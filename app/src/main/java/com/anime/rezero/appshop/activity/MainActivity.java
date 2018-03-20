package com.anime.rezero.appshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.anime.rezero.appshop.R;
import com.anime.rezero.appshop.adapter.LoaispAdapter;
import com.anime.rezero.appshop.adapter.SanPhamAdapter;
import com.anime.rezero.appshop.model.GioHangModel;
import com.anime.rezero.appshop.model.Loaisp;
import com.anime.rezero.appshop.model.SanPham;
import com.anime.rezero.appshop.ultil.CheckConnection;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.sun.xml.internal.ws.api.message.Packet.Status.Response;
import static com.sun.xml.internal.ws.api.message.Packet.Status.Response;
import static sun.net.www.protocol.http.AuthCacheValue.Type.Server;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbarManHinhChinh;
    ViewFlipper viewFlipper;
    RecyclerView recyclerView;
    NavigationView navigationView;
    ListView listViewManHinhChinh;
    ArrayList<Loaisp> mangloaisp;
    LoaispAdapter loaispAdapter;
    ArrayList<SanPham> mangsanpham;
    SanPhamAdapter sanphamadapter;
    public static ArrayList<GioHangModel> manggiohang;
    int id = 0;
    String tenloaisp = "", hinhanhloaisp = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidget();
        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
            ActionBar();
            ActionViewFlipper();
            GetDuLieuLoaisp();
            GetDuLieuSPMoiNhat();
            CatchOnItemListView();
        } else {
            CheckConnection.ShowToast_short(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối!");
            finish();
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

    private void CatchOnItemListView() {
        listViewManHinhChinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(intent);
                        }else {
                            CheckConnection.ShowToast_short(getApplicationContext(),"Bạn hãy kiểm tra lại kết nối");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 1:
                        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
                            Intent intent = new Intent(MainActivity.this, DienThoaiActivity1.class);
                            intent.putExtra("idloaisp",mangloaisp.get(position).getId());
                            startActivity(intent);
                        }else {
                            CheckConnection.ShowToast_short(getApplicationContext(),"Bạn hãy kiểm tra lại kết nối");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 2:
                        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
                            Intent intent = new Intent(MainActivity.this, LaptopActivity.class);
                            intent.putExtra("idloaisp",mangloaisp.get(position).getId());
                            startActivity(intent);
                        }else {
                            CheckConnection.ShowToast_short(getApplicationContext(),"Bạn hãy kiểm tra lại kết nối");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 4:
                        startActivity(new Intent(MainActivity.this,ThongTin.class));
                        break;
                }
            }
        });
    }

    private void GetDuLieuSPMoiNhat() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlSanPhamMoiNhat, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    int ID = 0;
                    String tensanpham = "";
                    Integer giasanpham = 0;
                    String hinhanhsanpham = "";
                    String motasanpham = "";
                    int IDsanpham = 0;
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            ID = jsonObject.getInt("id");
                            tensanpham = jsonObject.getString("tensp");
                            giasanpham = jsonObject.getInt("giasp");
                            hinhanhsanpham = jsonObject.getString("hinhanhsp");
                            motasanpham = jsonObject.getString("motasp");
                            IDsanpham = jsonObject.getInt("idsanpham");
                            mangsanpham.add(new SanPham(ID, tensanpham, giasanpham, hinhanhsanpham, motasanpham, IDsanpham));
                            sanphamadapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void GetDuLieuLoaisp() {
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlLoaisp, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            tenloaisp = jsonObject.getString("tenloaisp");
                            hinhanhloaisp = jsonObject.getString("hinhanhloaisp");
                            mangloaisp.add(new Loaisp(id, tenloaisp, hinhanhloaisp));
                            loaispAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    mangloaisp.add(new Loaisp(0, "Liên hệ", "http://stlukeschurchri.org/wp-content/uploads/2015/08/telephone.png"));
                    mangloaisp.add(new Loaisp(0, "Thông tin", "https://www.businessexchange.ca/wp-content/uploads/2016/08/email-icon.png"));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CheckConnection.ShowToast_short(getApplicationContext(), error.toString());
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void ActionViewFlipper() {
        ArrayList<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("https://i.ytimg.com/vi/-ZJV7_S6a4U/maxresdefault.jpg");
        mangquangcao.add("https://i.ytimg.com/vi/xT0rv5Vhkkc/maxresdefault.jpg");
        mangquangcao.add("https://vn-live.slatic.net/v2/resize/page_manager/39f264b4af49309cbc458328b8020a3d.jpg?format=webp&width=1024");
        mangquangcao.add("https://fptshop.com.vn/Uploads/images/2015/Tin-Tuc/BinhNN02/Tintuc/TINTUC01/xiaomi-Mi-NoteBook-Pro-ra-mat-fptshop-02.jpg");
        for (int i = 0; i < mangquangcao.size(); i++) {
            ImageView img = new ImageView(getApplicationContext());
            Picasso.with(getApplicationContext()).load(mangquangcao.get(i)).into(img);
            img.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(img);
        }
        viewFlipper.setFlipInterval(5000);//Chạy trong 5s
        viewFlipper.setAutoStart(true);//cho view flipper tự chạy
        Animation animation_slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in);
        Animation animation_slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out);
        viewFlipper.setInAnimation(animation_slide_in);
        viewFlipper.setOutAnimation(animation_slide_out);
    }

    private void ActionBar() {
        setSupportActionBar(toolbarManHinhChinh);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//set nut menu cho action bar
        toolbarManHinhChinh.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);//Đặt lại hình ảnh nút menu cho toolbar
        toolbarManHinhChinh.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);//Bắt sự kiện cho menu
            }
        });
    }

    public void initWidget() {
        toolbarManHinhChinh = (Toolbar) findViewById(R.id.toolbar_manhinhchinh);
        viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        listViewManHinhChinh = (ListView) findViewById(R.id.list_viewmanhinhchinh);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mangloaisp = new ArrayList<>();
        mangloaisp.add(0, new Loaisp(0, "Trang chính", "http://www.rangdonghotel.com.vn/uploads/userfiles/image/home%20icon.png"));
        loaispAdapter = new LoaispAdapter(getApplicationContext(), R.layout.dong_loai_san_pham, mangloaisp);
        listViewManHinhChinh.setAdapter(loaispAdapter);

        mangsanpham = new ArrayList<>();
        sanphamadapter = new SanPhamAdapter(getApplicationContext(), mangsanpham);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerView.setAdapter(sanphamadapter);

        if(manggiohang !=null){

        }else {
            manggiohang = new ArrayList<>();
        }
    }
}
