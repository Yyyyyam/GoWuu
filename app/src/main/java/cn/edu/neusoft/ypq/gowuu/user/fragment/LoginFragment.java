package cn.edu.neusoft.ypq.gowuu.user.fragment;


import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.app.MainActivity;
import cn.edu.neusoft.ypq.gowuu.base.BaseFragment;
import cn.edu.neusoft.ypq.gowuu.user.bean.User;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.FileUtils;
import cn.edu.neusoft.ypq.gowuu.utils.FragmentUtils;
import cn.edu.neusoft.ypq.gowuu.utils.PostMessage;
import cz.msebera.android.httpclient.Header;

/**
 * 作者:颜培琦
 * 时间:2022/3/3
 * 功能:LoginFragment
 */
public class LoginFragment extends BaseFragment {

    @BindView(R.id.login_to_register)
    TextView register;
    @BindView(R.id.login_et_name)
    EditText etName;
    @BindView(R.id.login_et_password)
    EditText etPassword;
    @BindView(R.id.login_tv_fgtpasword)
    TextView resetPass;
    @BindView(R.id.login_bt_confirm)
    Button confirm;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_usr_login, null);
        ButterKnife.bind(this, view);
        return view;
    }


    @OnClick({R.id.login_ib_back, R.id.login_to_register, R.id.login_tv_fgtpasword, R.id.login_bt_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_ib_back:
                FragmentUtils.popBack(getActivity());
                break;
            case R.id.login_to_register:
                RegisterFragment rFragment = new RegisterFragment();
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_frameLayout, rFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.login_tv_fgtpasword:
                ModifyPasswordFragment modFragment = new ModifyPasswordFragment();
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_frameLayout, modFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.login_bt_confirm:
                login();
                break;
        }
    }

    /**
     * 登录功能：
     * 1.获取登录地址
     * 2.通过通过联网请求将输入数据对比数据取
     * 3.获得请求结果，通过请求结果判断是否登陆成功
     */
    private void login(){
        String url = Constants.USR_URL+"/login";

        String sLogin = etName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (sLogin.isEmpty()||password.isEmpty()){
            Toast.makeText(mContext,"请输入登录信息！",Toast.LENGTH_SHORT).show();
        } else {
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params =new RequestParams();
            params.put("sLogin",sLogin);
            params.put("password",password);
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody, StandardCharsets.UTF_8);
                    //修正泛型无法正常解析问题
                    Type type = new TypeToken<PostMessage<User>>() {
                    }.getType();
                    PostMessage<User> postMessage = new Gson().fromJson(response, type);
                    if (postMessage.getMessage()==null){
                        User user = postMessage.getData();
                        MainActivity.user.setUser(user);
                        //保存数据到SharedPreferences
                        FileUtils.saveUserInfo(mContext);
                        Toast.makeText(mContext,"登陆成功",Toast.LENGTH_SHORT).show();
                        FragmentUtils.popBack(getActivity());
                    }else {
                        Toast.makeText(mContext, postMessage.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(mContext,"LoginFragment(254):请求失败",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
