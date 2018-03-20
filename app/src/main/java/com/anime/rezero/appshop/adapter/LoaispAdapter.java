package com.anime.rezero.appshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.anime.rezero.appshop.R;
import com.anime.rezero.appshop.model.Loaisp;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zing on 1/31/2018.
 */

public class LoaispAdapter extends BaseAdapter {
    public Context context;
    public int layout;
    public ArrayList<Loaisp> loaispList;

    public LoaispAdapter(Context context, int layout, ArrayList<Loaisp> loaispList) {
        this.context = context;
        this.layout = layout;
        this.loaispList = loaispList;
    }

    @Override
    public int getCount() {
        return loaispList.size();
    }

    @Override
    public Object getItem(int position) {
        return loaispList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public class ViewHolder{
        TextView txtLoaisp;
        ImageView imgLoaisp;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(layout,null);
            holder.txtLoaisp = (TextView) convertView.findViewById(R.id.txt_loaisp);
            holder.imgLoaisp = (ImageView) convertView.findViewById(R.id.img_loaisp);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        Loaisp loaisp = loaispList.get(position);
        holder.txtLoaisp.setText(loaisp.getTenloaisp());
        Picasso.with(context).load(loaisp.getHinhanhloaisp())
                .placeholder(R.drawable.camera)
                .error(R.drawable.error)
                .into(holder.imgLoaisp);
        //holder.imgLoaisp.setScaleType(ImageView.ScaleType.FIT_XY);

        return convertView;
    }
}
