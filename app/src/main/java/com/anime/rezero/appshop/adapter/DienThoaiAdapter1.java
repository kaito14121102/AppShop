package com.anime.rezero.appshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.anime.rezero.appshop.R;
import com.anime.rezero.appshop.model.SanPham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by zing on 2/9/2018.
 */

public class DienThoaiAdapter1 extends BaseAdapter {
    public Context context;
    public int layout;
    public ArrayList<SanPham> sanPhamArrayList;

    public DienThoaiAdapter1(Context context, int layout, ArrayList<SanPham> sanPhamArrayList) {
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

    public class ViewHolder {
        public TextView txtTen, txtGia;
        ImageView imgSanPham;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder.txtTen = (TextView) convertView.findViewById(R.id.txt_tensanpham1);
            holder.txtGia = (TextView) convertView.findViewById(R.id.txt_giasanpham1);
            holder.imgSanPham = (ImageView) convertView.findViewById(R.id.img_sanpham1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SanPham sanPham = sanPhamArrayList.get(position);
        holder.txtTen.setText(sanPham.getTensanpham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtGia.setText("Giá : " + decimalFormat.format(sanPham.getGiasanpham()) + "Đ");
        Picasso.with(context).load(sanPham.getHinhanhsanpham()).placeholder(R.drawable.camera)
                .error(R.drawable.error)
                .into(holder.imgSanPham);
        return convertView;
    }
}
