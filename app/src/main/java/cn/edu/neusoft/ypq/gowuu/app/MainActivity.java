package cn.edu.neusoft.ypq.gowuu.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.loopj.android.http.AsyncHttpClient;

import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.app.fragment.MainFragment;
import cn.edu.neusoft.ypq.gowuu.app.fragment.WelcomeFragment;
import cn.edu.neusoft.ypq.gowuu.user.bean.User;

public class MainActivity extends AppCompatActivity {
    public static User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (checkPermission()){
            welcome();
        }

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
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限被用户同意。
                    welcome();
                } else {
                    // 权限被用户拒绝了。
                    finish();
                }
                return;
            }
        }
    }
}