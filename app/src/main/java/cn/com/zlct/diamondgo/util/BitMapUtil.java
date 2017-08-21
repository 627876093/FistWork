package cn.com.zlct.diamondgo.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Bitmap处理工具类
 */
public class BitMapUtil {

    public static final String CACHE_DIR = Environment.getExternalStorageDirectory() +
            "/Android/data/cn.com.zlct.diamondgo/cache";//缓存路径

    /**
     * 图片二次采样 -- 图片路径
     */
    public static Bitmap decodeBitmap(String bitmapFile, int scaleWidth, int scaleHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//表示只解码图片的边缘信息，用来得到图片的宽高
        BitmapFactory.decodeFile(bitmapFile, options);
        int oldWidth = options.outWidth;
        int oldHeight = options.outHeight;
        int bWidth = oldWidth / scaleWidth;
        int bHeight = oldHeight / scaleHeight;
        if (oldWidth > scaleWidth || oldHeight > scaleHeight) {
            int size = bWidth < bHeight ? bWidth : bHeight;
            if (size == 0) {
                size = (bWidth + bHeight) / 2;
            }
            options.inSampleSize = size;
        }
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(bitmapFile, options);
    }

    /**
     * 图片二次采样 -- Uri
     */
    public static Bitmap decodeBitmap(Context context, Uri uri, int scaleWidth, int scaleHeight) throws Exception{
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//表示只解码图片的边缘信息，用来得到图片的宽高
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
        int oldWidth = options.outWidth;
        int oldHeight = options.outHeight;
        int bWidth = oldWidth / scaleWidth;
        int bHeight = oldHeight / scaleHeight;
        if (oldWidth > scaleWidth || oldHeight > scaleHeight) {
            int size = bWidth < bHeight ? bWidth : bHeight;
            if (size == 0) {
                size = (bWidth + bHeight) / 2;
            }
            options.inSampleSize = size;
        }
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
    }

    /**
     * 图片二次采样并存为图片
     */
    public static File saveBitmap2File(String bitmapFile, int scaleWidth, int scaleHeight){
        Bitmap bitmap = decodeBitmap(bitmapFile, scaleWidth, scaleHeight);
        String filePath = CACHE_DIR + "/" + bitmapFile.hashCode() + ".jpg";
        return saveBitmap2File(bitmap, filePath);
    }

    /**
     * 图片二次采样并存为图片
     */
    public static File saveBitmap2File(Bitmap bitmap, String filePath){
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 图片二次采样 -- 裁剪成方形
     */
    public static Bitmap decodeBitmap(Bitmap bitmap, int scaleWidth, int scaleHeight){
        int min = scaleWidth < scaleHeight ? scaleWidth : scaleHeight;
        return ThumbnailUtils.extractThumbnail(bitmap, min, min);
    }

    /**
     * 图片二次采样 -- 裁剪成方形
     */
    public static Bitmap decodeBitmap(Bitmap bitmap){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//表示只解码图片的边缘信息，用来得到图片的宽高
        BitmapFactory.decodeStream(Bitmap2IS(bitmap), null, options);
        return decodeBitmap(bitmap, options.outWidth, options.outWidth);
    }


    /**
     * Uri转Bitmap 用于评论
     */
    public static Bitmap Uri2Bitmap4Review(Context context, Uri uri){
        try {
            Bitmap bitmap = decodeBitmap(context, uri, PhoneUtil.getPhoneWidth(context) / 3,
                    PhoneUtil.getPhoneHeight(context) / 3);
            if (getBitmapSize(bitmap) > 768 * 1024L) {
                return decodeBitmap4Review(bitmap, PhoneUtil.getPhoneWidth(context) / 3,
                        PhoneUtil.getPhoneHeight(context) / 3);
            } else {
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
            String path = getPath(context, uri);
            File file = new File(path);
            if (file.exists() && file.isFile() && file.length() > 196 * 1024L) {
                return getBitmap(path, (int) (file.length() / (196 * 1024L) + 1));
            }
            return getBitmap(path);
        }
    }

    /**
     * 图片二次采样
     */
    public static Bitmap decodeBitmap4Review(Bitmap bitmap, int scaleWidth, int scaleHeight){
        int oldWidth = bitmap.getWidth();
        int oldHeight = bitmap.getHeight();
        int bWidth = oldWidth / scaleWidth;
        int bHeight = oldHeight / scaleHeight;
        int inSampleSize = bWidth < bHeight ? bWidth : bHeight;
        if (inSampleSize < 2) {
            inSampleSize = 2;
        }
        return ThumbnailUtils.extractThumbnail(bitmap, oldWidth / inSampleSize, oldHeight / inSampleSize);
    }

    /**
     * 图片转Bitmap
     */
    public static Bitmap getBitmap(String bitmapFile){
        return BitmapFactory.decodeFile(bitmapFile);
    }

    /**
     * 图片转Bitmap
     */
    public static Bitmap getBitmap(String bitmapFile, int inSampleSize){
        return BitmapFactory.decodeFile(bitmapFile, getBitmapOption(inSampleSize));
    }

    /**
     * 获取bitmap大小
     */
    public static int getBitmapSize(Bitmap bitmap){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1){//API 12
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    /**
     * Bitmap转byte[]
     */
    public static byte[] Bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * byte[]转Bitmap
     */
    public static Bitmap Bytes2Bitmap(byte[] b){
        if (b.length != 0){
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    public static InputStream Bitmap2IS(Bitmap bm){
        ByteArrayOutputStream baos = Bitmap2BAOS(bm);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    public static ByteArrayOutputStream Bitmap2BAOS(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos;
    }

    /**
     * Uri转Bitmap
     */
    public static Bitmap Uri2Bitmap(Context context, Uri uri){
        try {
            Bitmap bitmap = decodeBitmap(context, uri, Util.getPhoneWidth(context) / 4,
                    Util.getPhoneHeight(context) / 4);
            if (getBitmapSize(bitmap) > 512 * 1024L) {
                return decodeBitmap(bitmap, Util.getPhoneWidth(context) / 4,
                        Util.getPhoneHeight(context) / 4);
            } else {
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
            String path = getPath(context, uri);
            File file = new File(path);
            if (file.exists() && file.isFile() && file.length() > 128 * 1024L) {
                return getBitmap(path, (int) (file.length() / (128 * 1024L) + 1));
            }
            return getBitmap(path);
        }
    }

    /**
     * Uri转Bitmap 用于评论
     */
    public static Bitmap Uri2Bitmap4Big(Context context, Uri uri){
        try {
            Bitmap bitmap = decodeBitmap(context, uri, Util.getPhoneWidth(context) / 3,
                    Util.getPhoneHeight(context) / 3);
            if (getBitmapSize(bitmap) > 768 * 1024L) {
                return decodeBitmap4Review(bitmap, Util.getPhoneWidth(context) / 3,
                        Util.getPhoneHeight(context) / 3);
            } else {
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
            String path = getPath(context, uri);
            File file = new File(path);
            if (file.exists() && file.isFile() && file.length() > 196 * 1024L) {
                return getBitmap(path, (int) (file.length() / (196 * 1024L) + 1));
            }
            return getBitmap(path);
        }
    }

    public static String getPath(Context context, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private static BitmapFactory.Options getBitmapOption(int inSampleSize){
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }
}
