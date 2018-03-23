package com.gnway.bangwoba.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;

/**
 * Created by luzhan on 2017/6/19.
 */

public class FileUtil {
    private static void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFile(f);
            }
            file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete();
        }
    }
    public static File uri2File(Activity aty, Uri uri) {

            // 在API11以上：要转为使用CursorLoader,并使用loadInBackground来返回
            String[] projection = { MediaStore.Images.Media.DATA };
            CursorLoader loader = new CursorLoader(aty, uri, projection, null,
                    null, null);
            Cursor cursor = loader.loadInBackground();
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return new File(cursor.getString(column_index));

    }
    public static String getRealPathFromURI(Activity activity,Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = activity.getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public static String getPath(Context context, Uri uri) {

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection,null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }

        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String FormetFileSize(long fileS)
    {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize="0B";
        if(fileS==0){
            return wrongSize;
        }
        if (fileS < 1024){
            fileSizeString = df.format((double) fileS) + "B";
        }
        else if (fileS < 1048576){
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        }
        else if (fileS < 1073741824){
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        }
        else{
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }
    @TargetApi(19) //Build.VERSION_CODES.KITKAT)
    public static String getImagePathFromUriAfter19(Activity activity, final Uri uri)
    {
        if(null != uri)
        {
            try
            {
                // use reflect
                Class<?> docClass = Class.forName("android.provider.DocumentsContract");
                Method checkUriMethod = docClass.getMethod("isDocumentUri", Context.class, Uri.class);
                Method getIDMethod = docClass.getMethod("getDocumentId", Uri.class);
                Boolean result = (Boolean)checkUriMethod.invoke(null, activity, uri);
                if (result.booleanValue() ) // DocumentProvider
                {
                    final String docId = (String)getIDMethod.invoke(null, uri);
                    if(uri.getAuthority().equals("com.android.externalstorage.documents") ) // ExternalStorageProvider
                    {
                        final String[] split = docId.split(":");
                        final String type = split[0];

                        if (type.equals("primary") )
                        {
                            return Environment.getExternalStorageDirectory() + "/" + split[1];
                        }
                    }
                    else if (uri.getAuthority().equals("com.android.providers.downloads.documents") ) // DownloadsProvider
                    {
                        final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                        return getImagePathFromUri(activity, contentUri, null, null);
                    }
                    else if (uri.getAuthority().equals("com.android.providers.media.documents") )  // MediaProvider
                    {
                        final String[] split = docId.split(":");
                        final String type = split[0];

                        Uri contentUri = null;
                        if ("image".equals(type))
                        {
                            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        }
                        else if ("video".equals(type))
                        {
                            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        }
                        else if ("audio".equals(type))
                        {
                            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        }
                        final String selection = "_id=?";
                        final String[] selectionArgs = new String[] { split[1] };
                        return getImagePathFromUri(activity, contentUri, selection, selectionArgs);
                    }
                }
                if (ContentResolver.SCHEME_FILE.equalsIgnoreCase(uri.getScheme() ) )  // File
                {
                    return uri.getPath();
                }
                else if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme() ) )  // MediaStore (and general)
                {
                    // Return the Google Photos remote address
                    if (uri.getAuthority().equals("com.google.android.apps.photos.content") )
                    {
                        return uri.getLastPathSegment();
                    }
                    return getImagePathFromUri(activity, uri, null, null);
                }
            }
            catch (Exception e)
            {
            }
        }

        return null;
    }
    public static String getImagePathFromUri(Activity activity, final Uri uri, String selection, String[] selectionArgs)
    {
        String sPath = "";
        if (null == uri)
        {
            return sPath;
        }
        final String scheme = uri.getScheme();
        if ( scheme == null )
        {
            sPath = uri.getPath();
        }
        else if (ContentResolver.SCHEME_FILE.equals(scheme) )
        {
            sPath = uri.getPath();
        }
        else if (ContentResolver.SCHEME_CONTENT.equals(scheme) )
        {
            Cursor cursor = activity.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, selection, selectionArgs, null);
            if (null != cursor )
            {
                if (cursor.moveToFirst() )
                {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1)
                    {
                        sPath = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return sPath;
    }

    public static String byte2File(byte[] buf, String filePath, String fileName)
    {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try
        {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory())
            {
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (bos != null)
            {
                try
                {
                    bos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (fos != null)
            {
                try
                {
                    fos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return filePath + File.separator + fileName;
    }

}
