package cn.edu.neusoft.ypq.gowuu.app.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.app.MainActivity;
import cn.edu.neusoft.ypq.gowuu.base.BaseFragment;
import cn.edu.neusoft.ypq.gowuu.business.bean.Goods;
import cn.edu.neusoft.ypq.gowuu.user.bean.User;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.PostMessage;
import cz.msebera.android.httpclient.Header;

/**
 * 作者:颜培琦
 * 时间:2022/3/4
 * 功能:WelcomeFragment
 */
public class WelcomeFragment extends BaseFragment<Void> {

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_welcome, null);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        initUser();
    }

    private void initUser(){
        SharedPreferences preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String userJson = preferences.getString("userJson",null);
        if (userJson != null){
            Gson gson = new Gson();
            User localUser = gson.fromJson(userJson, User.class);
            String url = Constants.USR_URL+"/check_user";
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("uid", localUser.getUid());
            params.put("password", localUser.getPassword());
            client.get(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody, StandardCharsets.UTF_8);
                    Type type = new TypeToken<PostMessage<User>>() {
                    }.getType();
                    PostMessage<User> postMessage = new Gson().fromJson(response, type);
                    if (postMessage.getMessage() == null){
                        User serviceUser = postMessage.getData();
                        if (localUser.getPermission() != serviceUser.getPermission()){
                            Toast.makeText(mContext,"用户权限已发生改变，请前往“我的”界面查看",Toast.LENGTH_SHORT).show();
                        }
                        if (serviceUser == null) {
                            Toast.makeText(mContext,"登录信息发生改变，请重新登录",Toast.LENGTH_SHORT).show();
                        } else {
                            MainActivity.user.setUser(serviceUser);
                        }
                    } else {
                        Toast.makeText(mContext,postMessage.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(mContext,"WelcomeFragment(78):请求失败",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
