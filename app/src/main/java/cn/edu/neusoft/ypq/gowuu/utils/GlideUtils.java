package cn.edu.neusoft.ypq.gowuu.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * 作者:颜培琦
 * 时间:2022/3/22
 * 功能:GlideUtils
 */
public class GlideUtils {
    public static void setImage(Context context, String  url, ImageView imageView){
        Glide.with(context).load(url)
                .into(imageView);
    }

    public static void setImageNoCache(Context context, String  url, ImageView imageView){
        Glide.with(context).load(url)
                .skipMemoryCache(true)//跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)//不缓冲disk硬盘中
                .into(imageView);
    }
}
