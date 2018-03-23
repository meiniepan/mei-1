package com.gnway.bangwoba.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.gnway.bangwoba.R;
import com.gnway.bangwoba.bean.Emoji;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EmojiUtil {
    private static ArrayList<Emoji> emojiList;

    public static ArrayList<Emoji> getEmojiList() {
        if (emojiList == null) {
            emojiList = generateEmojis();
        }
        return emojiList;
    }

    private static ArrayList<Emoji> generateEmojis() {
        ArrayList<Emoji> list = new ArrayList<>();
        for (int i = 0; i < EmojiResArray.length; i++) {
            Emoji emoji = new Emoji();
            emoji.setImageUri(EmojiResArray[i]);
            emoji.setContent(EmojiTextArray[i]);
            list.add(emoji);
        }
        return list;
    }


    public static final int[] EmojiResArray = {
            R.drawable.def_0,
            R.drawable.def_1,
            R.drawable.def_2,
            R.drawable.def_3,
            R.drawable.def_4,
            R.drawable.def_5,
            R.drawable.def_6,
            R.drawable.def_7,
            R.drawable.def_8,
            R.drawable.def_9,
            R.drawable.def_10,
            R.drawable.def_11,
            R.drawable.def_12,
            R.drawable.def_13,
            R.drawable.def_14,
            R.drawable.def_15,
            R.drawable.def_16,
            R.drawable.def_17,
            R.drawable.def_18,
            R.drawable.def_19,
            R.drawable.def_20,
            R.drawable.def_21,
            R.drawable.def_22,
            R.drawable.def_23,
            R.drawable.def_24,
            R.drawable.def_25,
            R.drawable.def_26,
            R.drawable.def_27,
            R.drawable.def_28,
            R.drawable.def_29,
            R.drawable.def_30,
            R.drawable.def_31,
            R.drawable.def_32,
            R.drawable.def_33,
            R.drawable.def_34,
            R.drawable.def_35,
            R.drawable.def_36,
            R.drawable.def_37,
            R.drawable.def_38,
            R.drawable.def_39,
            R.drawable.def_40,
            R.drawable.def_41,
            R.drawable.def_42,
            R.drawable.def_43,
            R.drawable.def_44,
            R.drawable.def_45,
            R.drawable.def_46,
            R.drawable.def_47,
            R.drawable.def_48,
            R.drawable.def_49,
            R.drawable.def_50,
            R.drawable.def_51,
            R.drawable.def_52,
            R.drawable.def_53,
            R.drawable.def_54,
            R.drawable.def_55,
            R.drawable.def_56,
            R.drawable.def_57,
            R.drawable.def_58,
            R.drawable.def_59,
            R.drawable.def_60,
            R.drawable.def_61,
            R.drawable.def_62,
            R.drawable.def_63,
            R.drawable.def_64,
            R.drawable.def_65,
            R.drawable.def_66,
            R.drawable.def_67,
            R.drawable.def_68,
            R.drawable.def_69,
            R.drawable.def_70,
            R.drawable.def_71,
            R.drawable.def_72,
            R.drawable.def_73,
            R.drawable.def_74,
            R.drawable.def_75,
            R.drawable.def_76,
            R.drawable.def_77,
            R.drawable.def_78,
            R.drawable.def_79,
            R.drawable.def_80,
            R.drawable.def_81,
            R.drawable.def_82,
            R.drawable.def_83,
            R.drawable.def_84,
            R.drawable.def_85,
            R.drawable.def_86,
            R.drawable.def_87,
            R.drawable.def_88,
            R.drawable.def_89,
            R.drawable.def_90,
            R.drawable.def_91,
            R.drawable.def_92,
            R.drawable.def_93,
            R.drawable.def_94,
            R.drawable.def_95,
            R.drawable.def_96,
            R.drawable.def_97,
            R.drawable.def_98,
            R.drawable.def_99,
            R.drawable.def_100,
            R.drawable.def_101,
            R.drawable.def_102,
            R.drawable.def_103,
            R.drawable.def_104,

    };

    public static final String[] EmojiTextArray = {
            "[def_0]",
            "[def_1]",
            "[def_2]",
            "[def_3]",
            "[def_4]",
            "[def_5]",
            "[def_6]",
            "[def_7]",
            "[def_8]",
            "[def_9]",
            "[def_10]",
            "[def_11]",
            "[def_12]",
            "[def_13]",
            "[def_14]",
            "[def_15]",
            "[def_16]",
            "[def_17]",
            "[def_18]",
            "[def_19]",
            "[def_20]",
            "[def_21]",
            "[def_22]",
            "[def_23]",
            "[def_24]",
            "[def_25]",
            "[def_26]",
            "[def_27]",
            "[def_28]",
            "[def_29]",
            "[def_30]",
            "[def_31]",
            "[def_32]",
            "[def_33]",
            "[def_34]",
            "[def_35]",
            "[def_36]",
            "[def_37]",
            "[def_38]",
            "[def_39]",
            "[def_40]",
            "[def_41]",
            "[def_42]",
            "[def_43]",
            "[def_44]",
            "[def_45]",
            "[def_46]",
            "[def_47]",
            "[def_48]",
            "[def_49]",
            "[def_50]",
            "[def_51]",
            "[def_52]",
            "[def_53]",
            "[def_54]",
            "[def_55]",
            "[def_56]",
            "[def_57]",
            "[def_58]",
            "[def_59]",
            "[def_60]",
            "[def_61]",
            "[def_62]",
            "[def_63]",
            "[def_64]",
            "[def_65]",
            "[def_66]",
            "[def_67]",
            "[def_68]",
            "[def_69]",
            "[def_70]",
            "[def_71]",
            "[def_72]",
            "[def_73]",
            "[def_74]",
            "[def_75]",
            "[def_76]",
            "[def_77]",
            "[def_78]",
            "[def_79]",
            "[def_80]",
            "[def_81]",
            "[def_82]",
            "[def_83]",
            "[def_84]",
            "[def_85]",
            "[def_86]",
            "[def_87]",
            "[def_88]",
            "[def_89]",
            "[def_90]",
            "[def_91]",
            "[def_92]",
            "[def_93]",
            "[def_94]",
            "[def_95]",
            "[def_96]",
            "[def_97]",
            "[def_98]",
            "[def_99]",
            "[def_100]",
            "[def_101]",
            "[def_102]",
            "[def_103]",
            "[def_104]",
    };

    static {
        emojiList = generateEmojis();
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static void handlerEmojiText(TextView comment, String content, Context context,int size){
        SpannableStringBuilder sb = new SpannableStringBuilder(content);
        String regex = "\\[(\\S+?)\\]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        Iterator<Emoji> iterator;
        Emoji emoji = null;
        while (m.find()) {
            iterator = emojiList.iterator();
            String tempText = m.group();
            while (iterator.hasNext()) {
                emoji = iterator.next();
                if (tempText.equals(emoji.getContent())) {
                    //转换为Span并设置Span的大小
                    sb.setSpan(new ImageSpan(context, decodeSampledBitmapFromResource(context.getResources(), emoji.getImageUri()
                                    , dip2px(context, size), dip2px(context, size))),
                            m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                }
            }
        }
        comment.setText(sb);
    }
}
