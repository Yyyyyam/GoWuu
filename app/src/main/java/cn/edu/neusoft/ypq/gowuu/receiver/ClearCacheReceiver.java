package cn.edu.neusoft.ypq.gowuu.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 作者:颜培琦
 * 时间:2022/6/12
 * 功能:ClearCacheReceiver
 */
public class ClearCacheReceiver extends BroadcastReceiver {
    public static final String GLIDE_CLEAR_CACHE = "cn.edu.neusoft.ypq.CLEAR_CACHE";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (GLIDE_CLEAR_CACHE .equals(intent.getAction())) {
            clearCacheListener.clear();
        }
    }

    // 刷新用户数据的接口
    private ClearCacheListener clearCacheListener;
    public interface ClearCacheListener{
        void clear();
    }
    public void clear(ClearCacheListener clearCacheListener) {
        this.clearCacheListener = clearCacheListener;
    }
}
