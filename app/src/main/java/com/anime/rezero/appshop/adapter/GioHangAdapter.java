package com.anime.rezero.appshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anime.rezero.appshop.R;
import com.anime.rezero.appshop.activity.GioHang;
import com.anime.rezero.appshop.activity.MainActivity;
import com.anime.rezero.appshop.model.GioHangModel;
import com.anime.rezero.appshop.model.SanPham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by zing on 2/11/2018.
 */

public class GioHangAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<GioHangModel> sanPhamArrayList;

    public GioHangAdapter(Context context, int layout, ArrayList<GioHangModel> sanPhamArrayList) {
        this.context = context;
        this.layout = layout;
        this.sanPhamArrayList = sanPhamArrayList;
    }

    @Override
    public int getCount() {
        return sanPhamArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return sanPhamArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView txtTen,txtGia,txtSoluong;
        Button btnTru,btnCong;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            holder.txtTen= (TextView) convertView.findViewById(R.id.txt_tengiohang);
            holder.txtSoluong= (TextView) convertView.findViewById(R.id.txt_giohangsoluong);
            holder.txtGia= (TextView) convertView.findViewById(R.id.txt_giagiohang);
            holder.imageView = (ImageView) convertView.findViewById(R.id.img_viewgiohang);
            holder.btnCong = (Button) convertView.findViewById(R.id.btn_giohangcong);
            holder.btnTru = (Button) convertView.findViewById(R.id.btn_giohangtru);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        GioHangModel sanPham = sanPhamArrayList.get(position);
        holder.txtTen.setText(sanPham.getTensanpham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtGia.setText("Giá : " + decimalFormat.format(sanPham.getGiasanpham()) + "đ");
        Picasso.with(context).load(sanPham.getHinhanhsanpham()).placeholder(R.drawable.camera)
                .error(R.drawable.error)
                .into(holder.imageView);
        holder.txtSoluong.setText(sanPham.getSoluong()+"");
        final int sl = Integer.parseInt(holder.txtSoluong.getText().toString());
        if(sl>=10){
            holder.btnCong.setVisibility(View.INVISIBLE);
            holder.btnTru.setVisibility(View.VISIBLE);
        }else if (sl <=1){
            holder.btnCong.setVisibility(View.VISIBLE);
            holder.btnTru.setVisibility(View.INVISIBLE);
        }
        holder.btnCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int slhientai = MainActivity.manggiohang.get(position).getSoluong();
                int slmoi = slhientai+1;
                long giahientai = MainActivity.manggiohang.get(position).getGiasanpham();
                long giamoinhat = (giahientai/slhientai)*slmoi;
                MainActivity.manggiohang.get(position).setSoluong(slmoi);
                MainActivity.manggiohang.get(position).setGiasanpham(giamoinhat);
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                holder.txtGia.setText("Giá : " + decimalFormat.format(giamoinhat) + "đ");
                holder.txtSoluong.setText(slmoi+"");
                GioHang.SetTongTien();
                if(slmoi>9){
                    holder.btnCong.setVisibility(View.INVISIBLE);
                    holder.btnTru.setVisibility(View.VISIBLE);
                }else {
                    holder.btnCong.setVisibility(View.VISIBLE);
                    holder.btnTru.setVisibility(View.VISIBLE);
                }
            }
        });
        holder.btnTru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int slhientai = MainActivity.manggiohang.get(position).getSoluong();
                int slmoi = slhientai-1;
                long giahientai = MainActivity.manggiohang.get(position).getGiasanpham();
                long giamoinhat = (giahientai/slhientai)*slmoi;
                MainActivity.manggiohang.get(position).setSoluong(slmoi);
                MainActivity.manggiohang.get(position).setGiasanpham(giamoinhat);
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                holder.txtGia.setText("Giá : " + decimalFormat.format(giamoinhat) + "đ");
                holder.txtSoluong.setText(slmoi+"");
                GioHang.SetTongTien();
                if(slmoi<2){
                    holder.btnCong.setVisibility(View.VISIBLE);
                    holder.btnTru.setVisibility(View.INVISIBLE);

                }else {
                    holder.btnCong.setVisibility(View.VISIBLE);
                    holder.btnTru.setVisibility(View.VISIBLE);
                }
            }
        });

        return convertView;
    }
}
