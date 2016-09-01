
package com.sx.yufs.sxapp.common.utils;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.sx.yufs.sxapp.common.Constant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public final class ImageTools {

    /**
     * 剪切图片是压缩图片，防止内存溢出
     * 
     * @param path
     * @return
     */
    public static Bitmap getClipImageByPath(String path) {
        int size;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        Float outWidth = (float) options.outWidth;
        Float outHeight = (float) options.outHeight;
        float scaleW = outWidth / 1080;
        float scaleH = outHeight / 1920;
        Log.i("init", "outWidth:" + outWidth + " outHeight:" + outHeight);
        boolean is_scale_width = true;
        if (scaleW >= scaleH) {
            is_scale_width = true;
        } else {
            is_scale_width = false;
        }
        if (is_scale_width) {// 横比放大
            if (scaleW >= 1) {
                size = 1080;
            } else {
                size = (int) (outWidth * 1);
            }
        } else {// 纵比放大
            if (scaleH >= 1) {
                size = 1920;
            } else {
                size = (int) (outHeight * 1);
            }
        }
        Log.i("init", "size:" + size);
        options.inJustDecodeBounds = false;
        if (is_scale_width) {
            options.inSampleSize = (int) (outWidth / size + 0.5);
        } else {
            options.inSampleSize = (int) (outHeight / size + 0.5);
        }
        bitmap = BitmapFactory.decodeFile(path, options);
        if (bitmap==null) {
            return null;
        }
        float scale = 0.0f;
        if (is_scale_width) {
            scale = (float) size / bitmap.getWidth();
        } else {
            scale = (float) size / bitmap.getHeight();
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        matrix.postRotate(ImageTools.getBitmapDegree(path));
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        return bitmap;
    }

    /**
     * 读取图片的旋转的角度
     * 
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static String getImageByIntent(Context context, Intent data, boolean isUserHead) {
        String img_path = null;
        // 取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意
        Uri mImageCaptureUri = data.getData();
        // 返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
        if (mImageCaptureUri != null) {
            img_path = getImageAbsolutePath(context, mImageCaptureUri);
        } else {
            Bundle extras = data.getExtras();
            if (extras != null) {
                // 这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片
                Bitmap image = extras.getParcelable("data");
                File file = new File(Constant.PICTURE_DIR);
                if (!file.exists()) {
                    file.mkdirs();
                }
                if (isUserHead) {
                    img_path = Constant.PICTURE_DIR + "update_clip_pic.jpg";
                } else {
                    img_path = Constant.PICTURE_DIR + System.currentTimeMillis() + ".jpg";
                }
                FileOutputStream oStream = null;
                try {
                    oStream = new FileOutputStream(img_path);
                    image.compress(CompressFormat.JPEG, 100, oStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (oStream != null) {
                            oStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    image.recycle();
                }
            }
        }
        Log.i("getImageByIntent", "img_path:" + img_path);
        return img_path;
    }

    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     * 
     * @param context
     * @param imageUri
     */
    @TargetApi(19)
    public static String getImageAbsolutePath(Context context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT
                && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[] {
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
            String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * 将图片最小边压缩到320并保存到sd卡中,返回缩略图路径
     * 
     * @param path
     * @param type: 0:头像 1：聊天 2：话题
     * @return
     */
    public static String getSmallImageByPath(String path, int type) {
        String outPath = null;
        int size = 320;
        int saveQuality = 100;
        String dirPath = null;
        switch (type) {
            case 0:
                size = 250;
                saveQuality = 25;
                dirPath = Constant.PICTURE_DIR;
                outPath = Constant.PICTURE_DIR + System.currentTimeMillis() + ".jpg";
                break;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        Float outWidth = (float) options.outWidth;
        Float outHeight = (float) options.outHeight;
        options.inJustDecodeBounds = false;
        options.inSampleSize = (int) (Math.min(outWidth, outHeight) / size + 0.5);
        bitmap = BitmapFactory.decodeFile(path, options);
        if (bitmap == null) {
            return path;
        }

        int angle = ImageTools.getBitmapDegree(path);
        Log.i("getSmallImageByPath", "angle:" + angle);
        bitmap = ImageTools.rotateBitmapByDegree(bitmap, angle);

        if (options.inSampleSize > 1) {
            float scale = 0.0f;
            scale = (float) size / Math.min(bitmap.getWidth(), bitmap.getHeight());
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
        }
        // bitmap = ImageTools.rotateBitmapByDegree(bitmap,
        // getBitmapDegree(path));
        FileOutputStream oStream = null;
        File file = new File(dirPath);
        try {
            if (!file.exists()) {
                file.mkdirs();
            }
            oStream = new FileOutputStream(outPath);
            bitmap.compress(CompressFormat.JPEG, saveQuality, oStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oStream != null) {
                    oStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return outPath;
    }

    /**
     * 将图片按照某个角度进行旋转
     * 
     * @param bm 需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

}
