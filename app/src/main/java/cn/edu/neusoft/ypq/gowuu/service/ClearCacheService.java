package cn.edu.neusoft.ypq.gowuu.service;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

/**
 * 作者:颜培琦
 * 时间:2022/6/12
 * 功能:ClearCacheService
 */
public class ClearCacheService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: bind");
        return new ClearController();
    }

    public static class ClearController extends Binder {
        public void clearGlide(Context context) {
            Glide.get(context).clearMemory();
            new Thread(() -> {
                Glide.get(context).clearDiskCache();
            }).start();
        }
    }
}
