package com.example.jh.picturedownload;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

/**
 * 此前没有整理过mvp图片下载，保存本地的demo，
 *
 * 这次写下，整合资源，后续会整合到androiddemo中
 */
public class MainActivity extends AppCompatActivity {

    final int REQUEST_WRITE = 1;//申请权限的请求码
    String url = "http://pic.ffpic.com/files/2014/0702/0702qbnnwazgqbz2.jpg";
    String fileName = "bg.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView imageView = (ImageView) findViewById(R.id.image);
        Button button = (Button) findViewById(R.id.bt_download);

        final SDFileHelper helper = new SDFileHelper(MainActivity.this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= 23) {
                    //判断是否有这个权限
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //2、申请权限: 参数二：权限的数组；参数三：请求码
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE);
                    } else {
                        helper.savePicture(fileName, url);
                        Glide.with(MainActivity.this).load(url).into(imageView);
                    }
                } else {
                    helper.savePicture(fileName, url);
                }


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Intent mediaScanIntent = new Intent(
                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri contentUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/image", "" + fileName)); //out is your output file
                    mediaScanIntent.setData(contentUri);
                    MainActivity.this.sendBroadcast(mediaScanIntent);
                } else {
                    sendBroadcast(new Intent(
                            Intent.ACTION_MEDIA_MOUNTED,
                            Uri.parse("file://"
                                    + Environment.getExternalStorageDirectory())));
                }




            }
        });





    }
}
