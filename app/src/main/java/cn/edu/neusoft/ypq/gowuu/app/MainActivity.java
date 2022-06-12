package cn.edu.neusoft.ypq.gowuu.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;

import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.app.fragment.MainFragment;
import cn.edu.neusoft.ypq.gowuu.app.fragment.WelcomeFragment;
import cn.edu.neusoft.ypq.gowuu.receiver.ClearCacheReceiver;
import cn.edu.neusoft.ypq.gowuu.service.ClearCacheService;
import cn.edu.neusoft.ypq.gowuu.user.bean.User;

public class MainActivity extends AppCompatActivity {
    public static User user = new User();
    private ClearCacheService.ClearController controller;
    private ClearCacheReceiver clearCacheReceiver;

    private final ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            controller = (ClearCacheService.ClearController) service;
            Intent i = new Intent();
            i.setAction(ClearCacheReceiver.GLIDE_CLEAR_CACHE);
            sendBroadcast(i);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) { }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (checkPermission()){
            welcome();
            init();
        }
    }

    private void init() {
        Intent service = new Intent(MainActivity.this, ClearCacheService.class);
        bindService(service, conn, BIND_AUTO_CREATE);

        regReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
        unregisterReceiver(clearCacheReceiver);
    }

    private void regReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ClearCacheReceiver.GLIDE_CLEAR_CACHE);
        clearCacheReceiver = new ClearCacheReceiver();
        // 实现广播的接口功能
        clearCacheReceiver.clear(() -> controller.clearGlide(MainActivity.this));
        // 注册广播接收器
        registerReceiver(clearCacheReceiver , intentFilter);
    }

    private void welcome(){
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_frameLayout, new WelcomeFragment())
                .commit();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_frameLayout, new MainFragment())
                            .commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },1000);
    }

    public boolean checkPermission() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            //请求权限
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限被用户同意。
                welcome();
            } else {
                // 权限被用户拒绝了。
                finish();
            }
        }
    }
}