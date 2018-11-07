package com.wuyou.user.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.RequiresApi;
import android.support.v4.os.EnvironmentCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.gs.buluo.common.utils.DensityUtils;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.RecycleViewDivider;
import com.wuyou.user.CarefreeApplication;
import com.wuyou.user.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.os.Environment.DIRECTORY_DCIM;

/**
 * Created by hjn on 2016/11/10.
 */
public class CommonUtil {
    public static String stringToJSON(String strJson) {
        // 计数tab的个数
        int tabNum = 0;
        StringBuffer jsonFormat = new StringBuffer();
        int length = strJson.length();

        char last = 0;
        for (int i = 0; i < length; i++) {
            char c = strJson.charAt(i);
            if (c == '{') {
                tabNum++;
                jsonFormat.append(c + "\n");
                jsonFormat.append(getSpaceOrTab(tabNum));
            } else if (c == '}') {
                tabNum--;
                jsonFormat.append("\n");
                jsonFormat.append(getSpaceOrTab(tabNum));
                jsonFormat.append(c);
            } else if (c == ',') {
                jsonFormat.append(c + "\n");
                jsonFormat.append(getSpaceOrTab(tabNum));
            } else if (c == ':') {
                jsonFormat.append(c + " ");
            } else if (c == '[') {
                tabNum++;
                char next = strJson.charAt(i + 1);
                if (next == ']') {
                    jsonFormat.append(c);
                } else {
                    jsonFormat.append(c + "\n");
                    jsonFormat.append(getSpaceOrTab(tabNum));
                }
            } else if (c == ']') {
                tabNum--;
                if (last == '[') {
                    jsonFormat.append(c);
                } else {
                    jsonFormat.append("\n" + getSpaceOrTab(tabNum) + c);
                }
            } else {
                jsonFormat.append(c);
            }
            last = c;
        }
        return jsonFormat.toString();
    }

    // 是空格还是tab
    private static String getSpaceOrTab(int tabNum) {
        StringBuffer sbTab = new StringBuffer();
        for (int i = 0; i < tabNum; i++) {
            sbTab.append('\t');
        }
        return sbTab.toString();
    }


    public static void saveBitmap2file(Bitmap bmp, Context context) {
        String savePath;
        String fileName = UUID.randomUUID().toString() + ".JPEG";
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = Environment.getExternalStorageDirectory().getPath() + "/laidao/";
        } else {
            Toast.makeText(context, "保存失败！", Toast.LENGTH_SHORT).show();
            return;
        }
        File filePic = new File(savePath + fileName);
        try {
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            Toast.makeText(context, "保存成功,位置:" + filePic.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    filePic.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + savePath + fileName)));

    }

    public static boolean checkNetworkNoConnected(Context context) {
        if (!NetTool.isConnected(context)) {
            ToastUtils.ToastMessage(context, R.string.connect_fail);
            return true;
        } else {
            return false;
        }
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        if (pm == null) {
            return false;
        }
        List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
        for (PackageInfo info : packageInfos) {
            if (TextUtils.equals(info.packageName.toLowerCase(), packageName)) {
                return true;
            }
        }
        return false;
    }


    private static final int QR_WIDTH = DensityUtils.dip2px(CarefreeApplication.getInstance().getApplicationContext(), 200);
    private static final int QR_HEIGHT = DensityUtils.dip2px(CarefreeApplication.getInstance().getApplicationContext(), 200);

    public static boolean createQRImage(String url) {
        Bitmap bitmap;
        try {
            if (url == null || "".equals(url) || url.length() < 1) {
                return false;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.RGB_565);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            String s = MediaStore.Images.Media.insertImage(CarefreeApplication.getInstance().getApplicationContext().getContentResolver(), bitmap, "", "");
            Uri uri = Uri.parse(s);
            CarefreeApplication.getInstance().getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
//            saveBitmap2file(bitmap, "qr.jpg");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String formatPrice(float price) {
        NumberFormat nf = new DecimalFormat("0.00");
        return nf.format(price);
    }
    public static String formaEos(float price) {
        NumberFormat nf = new DecimalFormat("0.0000");
        return nf.format(price);
    }


    public static void setEdDecimal(EditText editText, int count) {
        if (count < 0) {
            count = 0;
        }
        count += 1;
        editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        //设置字符过滤
        final int finalCount = count;
        editText.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
//                Log.d("setEdDecimal", "source:" + source + " start:" + start + " end:" + end + " dest:" + dest + " dstart:" + dstart + " dstart:" + dend);
                if (source.equals(".") && dest.toString().length() == 0) {
                    return "0.";
                }
                if (dest.toString().contains(".")) {
                    int index = dest.toString().indexOf(".");
                    int mlength = dest.toString().substring(index).length();
                    if (mlength == finalCount) {
                        return "";
                    }
                }

                if (dest.toString().equals("0") && source.equals("0")) {
                    return "";
                }

                return null;
            }
        }});
    }

    public static ArrayMap<String, String> ConvertObjToMap(Object obj) {
        ArrayMap<String, String> reMap = new ArrayMap<>();
        if (obj == null)
            return null;
        Field[] fields = obj.getClass().getDeclaredFields();
        try {
            for (int i = 0; i < fields.length; i++) {
                try {
                    String fieldName = fields[i].getName();
                    Field f = obj.getClass().getDeclaredField(fieldName);
                    f.setAccessible(true);
                    Object o = f.get(obj);
                    if (o != null && !fieldName.contains("$") && !TextUtils.equals("CREATOR", fieldName) && !TextUtils.equals(fieldName, "serialVersionUID")) {
                        reMap.put(fieldName, o.toString());
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return reMap;
    }

    public static String getOrderStatusString(Context context, int status) {
        switch (status) {
            case 1:
                return context.getString(R.string.wait_pay);
            case 2:
                return context.getString(R.string.serving);
            case 3:
                return context.getString(R.string.wait_comment);
            case 4:
                return context.getString(R.string.finished);
            case 5:
                return context.getString(R.string.canceled);
        }
        return "";
    }

    public static boolean checkPhone(String area, String phone, Context context) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.ToastMessage(context, "手机号不能为空!");
            return false;
        }
        Pattern p;
        switch (area) {
            case "86":
                p = Pattern.compile("1[0-9]{10}");  //中国
                break;
            case "1":
                p = Pattern.compile("[0-9]{10}");   //美国
                break;
            case "886":
                p = Pattern.compile("09[0-9]{8}||9[0-9]{8}");  //台湾
                break;
            case "852":
                p = Pattern.compile("[5|6|9|][0-9]{7}");  //香港  目前8�?
                break;
            case "81":
                p = Pattern.compile("0[8|9|]0[0-9]{8}");  //日本
                break;
            default:
                p = Pattern.compile("\\d+"); //哪国都不? 纯数字就?
        }
        Matcher m = p.matcher(phone);

        if (!m.matches() || phone.length() != 11) {
            ToastUtils.ToastMessage(context, "请输入正确的手机号!");
            return false;
        }
        return true;
    }

    public static void backgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }

    public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException var5) {
        } catch (UnsupportedEncodingException var6) {
            var6.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; ++i) {
            if (Integer.toHexString(255 & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(255 & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(255 & byteArray[i]));
            }
        }

        return md5StrBuff.toString();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890";
        Random random = new Random();
        StringBuffer sbuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sbuffer.append(base.charAt(number));
        }
        return sbuffer.toString();
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap getFlur(Context context, Bitmap sentBitmap) {
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        final RenderScript rs = RenderScript.create(context);
        final Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(25 /* e.g. 3.f */);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(bitmap);
        return bitmap;
    }

    public static Bitmap getScreenshot(Context context, View v) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Drawable drawable1 = new BitmapDrawable();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private static final String SYSTEM_LIB_C_PATH = "/system/lib/libc.so";
    private static final String SYSTEM_LIB_C_PATH_64 = "/system/lib64/libc.so";
    /**
     * ELF文件�? e_indent[]数组文件类标识索�?
     */
    private static final int EI_CLASS = 4;
    /**
     * ELF文件�? e_indent[EI_CLASS]的取值：ELFCLASS32表示32位目�?
     */
    private static final int ELFCLASS32 = 1;
    /**
     * ELF文件�? e_indent[EI_CLASS]的取值：ELFCLASS64表示64位目�?
     */
    private static final int ELFCLASS64 = 2;

    /**
     * Check if system libc.so is 32 bit or 64 bit
     */
    public static boolean isLibc64() {
        boolean bb = false;
        File libcFile = new File(SYSTEM_LIB_C_PATH);
        if (libcFile != null && libcFile.exists()) {
            byte[] header = readELFHeadrIndentArray(libcFile);
            if (header != null && header[EI_CLASS] == ELFCLASS64) {
                return true;
            }
        }
        File libcFile64 = new File(SYSTEM_LIB_C_PATH_64);
        if (libcFile64 != null && libcFile64.exists()) {
            byte[] header = readELFHeadrIndentArray(libcFile64);
            byte b = header[EI_CLASS];
            if (String.valueOf(b).equals("2")) {
                bb = true;
            }
        }
        return bb;
    }

    private static byte[] readELFHeadrIndentArray(File libFile) {
        if (libFile != null && libFile.exists()) {
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(libFile);
                if (inputStream != null) {
                    byte[] tempBuffer = new byte[16];
                    int count = inputStream.read(tempBuffer, 0, 16);
                    if (count == 16) {
                        return tempBuffer;
                    } else {
                    }
                }
            } catch (Throwable t) {
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }


    public static String getDeviceInfo(Context context) {
        StringBuilder sb = new StringBuilder();
        String packageName = context.getPackageName();
        try {
            sb.append(packageName + "/")
                    .append(context.getPackageManager().getPackageInfo(packageName, 0).versionName)
                    .append("(").append(Build.MODEL).append(";").append("Android ").append(Build.VERSION.RELEASE).append(";")
                    .append("Scale/").append(context.getResources().getDisplayMetrics().density).append(")");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private static File createImageFile(String imageFileName) {
        // Create an image file name
        File storageDir;
        storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
//            storageDir = mContext.get().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // Avoid joining path components manually
        File tempFile = new File(storageDir, imageFileName);

        // Handle the situation that user's external storage is not ready
        if (!Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(tempFile))) {
            return null;
        }
        return tempFile;
    }

    public static File saveBitmap2file(Bitmap bmp, String desFileName) {
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        File fileDir = getFileDir(desFileName);
        try {
            stream = new FileOutputStream(fileDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bmp.compress(format, quality, stream);
        return fileDir;
    }

    public static File getFileDir(String desFileName) {
        try {
            File dir = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM).toString(), desFileName);
            if (!dir.exists()) {
                dir.createNewFile();
            }
            return dir;
        } catch (IOException e) {
            e.printStackTrace();
            return new File(CarefreeApplication.getInstance().getApplicationContext().getFilesDir(), desFileName);
        }
    }

    public static File createOrGetDir() {
        File mDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "carefree");
        if (!mDir.exists()) {
            mDir.mkdirs();
        }
        return mDir;
    }

    public static Bitmap compressBitmap(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //�?始读入图片，此时把options.inJustDecodeBounds 设回true�?
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 400f;
        float ww = 240f;
        //缩放比�?�由于是固定比例缩放，只用高或�?�宽其中�?个数据进行计算即�?
        int be = 1;//be=1表示不缩�?
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩�?
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩�?
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return bitmap;
    }

    public static byte[] bmpToByteArray(Bitmap thumbBmp, boolean b) {
        int bytes = thumbBmp.getByteCount();

        ByteBuffer buf = ByteBuffer.allocate(bytes);
        thumbBmp.copyPixelsToBuffer(buf);

        byte[] byteArray = buf.array();
        return byteArray;
    }

    public static boolean checkInstallation(String packageName) {
        try {
            CarefreeApplication.getInstance().getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * Check if there is any connectivity to a mobile network
     *
     * @param context
     * @return
     */
    public static boolean isConnectedMobile(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    public static boolean isConnected(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    public static boolean isConnectedWifi(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    public static RecycleViewDivider getRecyclerDivider(Context context) {
        return new RecycleViewDivider(context, LinearLayoutManager.HORIZONTAL, DensityUtils.dip2px(context, 0.5f), context.getResources().getColor(R.color.tint_bg));
    }

    public static RecycleViewDivider getRecyclerDivider(Context context, float dp) {
        return new RecycleViewDivider(context, LinearLayoutManager.HORIZONTAL, DensityUtils.dip2px(context, dp), context.getResources().getColor(R.color.tint_bg));
    }

    public static String getPhoneWithStar(String mobile) {
        if (mobile.length() == 11) {
            return mobile.substring(0, 3) + "****" + mobile.substring(7);
        }
        return mobile;
    }

    public static String prettyPrintJson(Object object) {
        return new GsonBuilder()
                .registerTypeAdapterFactory(new GsonEosTypeAdapterFactory())
                .setPrettyPrinting().create().toJson(object);
    }
}
