package com.anime.rezero.appshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.anime.rezero.appshop.R;
import com.anime.rezero.appshop.activity.ChiTietSanPham;
import com.anime.rezero.appshop.model.SanPham;
import com.anime.rezero.appshop.ultil.CheckConnection;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by zing on 2/7/2018.
 */

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.ItemHolder> {
    Context context;
    ArrayList<SanPham> sanPhamArrayList;

    public SanPhamAdapter(Context context, ArrayList<SanPham> sanPhamArrayList) {
        this.context = context;
        this.sanPhamArrayList = sanPhamArrayList;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dong_san_pham_moi_nhat,null);
        ItemHolder itemHolder = new ItemHolder(v);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        SanPham sanPham = sanPhamArrayList.get(position);
        holder.txtTenSanPham.setText(sanPham.getTensanpham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtGiaSanPham.setText("Giá : "+decimalFormat.format(sanPham.getGiasanpham())+"Đ");
        Picasso.with(context).load(sanPham.getHinhanhsanpham()).placeholder(R.drawable.camera)
                                                                .error(R.drawable.error)
                                                                .into(holder.hinhsanpham);
    }

    @Override
    public int getItemCount() {
        return sanPhamArrayList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        public ImageView hinhsanpham;
        public TextView txtTenSanPham,txtGiaSanPham;

        public ItemHolder(View itemView) {
            super(itemView);
            hinhsanpham = (ImageView) itemView.findViewById(R.id.img_sanpham);
            txtTenSanPham = (TextView) itemView.findViewById(R.id.txt_tensanpham);
            txtGiaSanPham = (TextView) itemView.findViewById(R.id.txt_giasanpham);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChiTietSanPham.class);
                    intent.putExtra("thongtinsanpham",sanPhamArrayList.get(getPosition()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //CheckConnection.ShowToast_short(context,sanPhamArrayList.get(getPosition()).getTensanpham());
                    context.startActivity(intent);
                }
            });
        }
    }
}
