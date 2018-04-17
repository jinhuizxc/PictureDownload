package com.example.jh.picturedownload;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by jinhui on 2018/4/17.
 * email: 1004260403@qq.com
 */

public class SDFileHelper {

    private static final String TAG = "SDFileHelper";
    private Context context;

    public SDFileHelper() {
    }

    public SDFileHelper(Context context) {
        this.context = context;
    }

    // Glide保存图片
    public void savePicture(final String fileName, String url) {

        Glide.with(context).load(url).asBitmap().toBytes().into(new SimpleTarget<byte[]>() {
            @Override
            public void onResourceReady(byte[] bytes, GlideAnimation<? super byte[]> glideAnimation) {
                try {
                    // 保存到sd卡
                    savaFileToSD(fileName, bytes);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    // 往SD卡写入文件的方法
    private void savaFileToSD(String fileName, byte[] bytes) {

        //如果手机已插入sd卡,且app具有读写sd卡的权限
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String filePath = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/image";
            Log.e(TAG, "filePath = " + filePath);  // E/SDFileHelper: filePath = /storage/emulated/0/zs
            File dir1 = new File(filePath);
            if (!dir1.exists()) {
                dir1.mkdirs();
            }
            fileName = filePath + "/" + fileName;
            //这里就不要用openFileOutput了,那个是往手机内存中写数据的
            FileOutputStream output = null;
            try {
                output = new FileOutputStream(fileName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                output.write(bytes);
                //将bytes写入到输出流中
                output.close();
                //关闭输出流
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(context, "下载成功", Toast.LENGTH_SHORT).show();
//            Toast.makeText(context, "图片已成功保存到" + filePath, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "SD卡不存在或者不可读写", Toast.LENGTH_SHORT).show();
        }
        /**/
    }


}
