package com.anime.rezero.appshop.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.anime.rezero.appshop.R;

public class ManHinhChao extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_hinh_chao);

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(5000);
                }catch (Exception e){

                }finally {
                    Intent intent = new Intent(ManHinhChao.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });
        thread.start();
    }
}
