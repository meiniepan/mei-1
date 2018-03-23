package com.gnway.bangwoba.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by luzhan on 2017/7/5.
 */

public class ImageUtil {
    public static Bitmap ratio(String imgPath, float pixelW, float pixelH) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true，即只读边不读内容
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        // Get bitmap info, but notice that bitmap is null now

        Bitmap bitmap = BitmapFactory.decodeFile(imgPath,newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 想要缩放的目标尺寸
        float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了
        float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        // 开始压缩图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
        // 压缩好比例大小后再进行质量压缩
//        return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
        return bitmap;
    }
    public static String saveBitmap2file(Bitmap bmp, String filename){
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(path + "/gnway/xmppIm/allfiles");
        if(!file.exists()){
            file.mkdirs();
        }
        Bitmap.CompressFormat format= Bitmap.CompressFormat.JPEG;
       int quality = 30;
        OutputStream stream = null;
        String resultPath = file.getAbsolutePath()+"/"+filename;
        try {
            stream = new FileOutputStream(resultPath);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        bmp.compress(format, quality, stream);
        return resultPath;
    }
    public static String saveBitmap2file(Bitmap bmp, String filename,int quality){
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(path + "/gnway/xmppIm/allfiles");
        if(!file.exists()){
            file.mkdirs();
        }
        Bitmap.CompressFormat format= Bitmap.CompressFormat.JPEG;
//        int quality = 30;
        OutputStream stream = null;
        String resultPath = file.getAbsolutePath()+"/"+filename;
        try {
            stream = new FileOutputStream(resultPath);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        bmp.compress(format, quality, stream);
        return resultPath;
    }

    public static String saveBitmap2fileForRotation(Bitmap bmp, String filename){
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(path + "/gnway/xmppIm/allfiles");
        if(!file.exists()){
            file.mkdirs();
        }
        Bitmap.CompressFormat format= Bitmap.CompressFormat.JPEG;
        int quality = 30;
        OutputStream stream = null;
        String resultPath = file.getAbsolutePath()+"/"+filename;
        try {
            stream = new FileOutputStream(resultPath);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Matrix matrix = new Matrix();
        matrix.setRotate(90);
        //通过待旋转的图片和角度生成新的图片
        bmp=Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getHeight(),matrix,true);
        bmp.compress(format, quality, stream);
        return resultPath;
    }
}
