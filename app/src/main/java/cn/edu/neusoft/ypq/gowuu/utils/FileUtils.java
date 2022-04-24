package cn.edu.neusoft.ypq.gowuu.utils;

import static cn.edu.neusoft.ypq.gowuu.app.MainActivity.user;

import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者:颜培琦
 * 时间:2022/3/6
 * 功能:FileUtils
 */
public class FileUtils {

    //content类型的uri获取图片路径的方法
    private static String getImagePath(FragmentActivity activity, Uri uri, String selection) {
        String path = null;
        Cursor cursor = activity.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    public static String getPath(FragmentActivity activity, Uri uri){
        String path = null;
        //根据不同的uri进行不同的解析
        if (DocumentsContract.isDocumentUri(activity, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                path = getImagePath(activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                path = getImagePath(activity, contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            path = getImagePath(activity, uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            path = uri.getPath();
        }
        return path;
    }

    public static List<String> getPathList(FragmentActivity activity, List<Uri> uriList){
        List<String> pathList = new ArrayList<>();
        for (Uri uri : uriList){
            String path = null;
            //根据不同的uri进行不同的解析
            if (DocumentsContract.isDocumentUri(activity, uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    String id = docId.split(":")[1];
                    String selection = MediaStore.Images.Media._ID + "=" + id;
                    path = getImagePath(activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                    path = getImagePath(activity, contentUri, null);
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                path = getImagePath(activity, uri, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                path = uri.getPath();
            }
            pathList.add(path);
        }
        return pathList;
    }

    public static void saveUserInfo(Context context){
        Gson gson = new Gson();
        //将获取到的User数据转换为Json格式存放到SharedPreferences中
        String userJson = gson.toJson(user);
        //初始化SharedPreferences
        SharedPreferences preferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        //调用edit方法用来处理数据
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userJson",userJson);
        //commit方法保存数据
        editor.commit();
    }
}
