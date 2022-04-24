package cn.edu.neusoft.ypq.gowuu.user.fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.nio.charset.StandardCharsets;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.base.BaseFragment;
import cn.edu.neusoft.ypq.gowuu.utils.CheckUtils;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.FragmentUtils;
import cn.edu.neusoft.ypq.gowuu.utils.PostMessage;
import cz.msebera.android.httpclient.Header;

/**
 * 作者:颜培琦
 * 时间:2022/3/3
 * 功能:RegisterFragment
 */
public class RegisterFragment extends BaseFragment {

    @BindView(R.id.register_et_name)
    EditText etName;
    @BindView(R.id.register_et_phone)
    EditText etPhone;
    @BindView(R.id.register_et_email)
    EditText etEmail;
    @BindView(R.id.register_et_password)
    EditText etPassword;
    @BindView(R.id.register_et_cfmpassword)
    EditText etCfmPassword;
    @BindView(R.id.register_bt_confirm)
    Button loginBtConfirm;
    @BindView(R.id.register_tv_phone_check)
    TextView tvPhoneCheck;
    @BindView(R.id.register_tv_email_check)
    TextView tvEmailCheck;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_usr_reg, null);
        ButterKnife.bind(this, view);
        CheckUtils.startPhoneCheck(etPhone, tvPhoneCheck);
        CheckUtils.startEmailCheck(etEmail, tvEmailCheck);
        return view;
    }

    @OnClick({R.id.register_ib_back,R.id.register_bt_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_ib_back:
                FragmentUtils.popBack(getActivity());
                break;
            case R.id.register_bt_confirm:
                reg();
                break;
        }
    }

    private void reg(){
        String url = Constants.USR_URL+"/reg";

        String username = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String cfm_password = etCfmPassword.getText().toString().trim();

        if (username.isEmpty()||phone.isEmpty()||email.isEmpty()||password.isEmpty()||cfm_password.isEmpty()){
            Toast.makeText(mContext,"请输入全部注册信息",Toast.LENGTH_SHORT).show();
        }else if (!cfm_password.equals(password)){
            Toast.makeText(mContext,"两次密码输入不一致",Toast.LENGTH_SHORT).show();
        }else if (!CheckUtils.isPhone(phone)){
            Toast.makeText(mContext,"手机格式有问题",Toast.LENGTH_SHORT).show();
        }else if (!CheckUtils.isEmail(email)){
            Toast.makeText(mContext,"邮箱格式有问题",Toast.LENGTH_SHORT).show();
        }else{
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params =new RequestParams();
            params.put("username",username);
            params.put("phone",phone);
            params.put("email",email);
            params.put("password",password);
            params.put("gender",0);
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody, StandardCharsets.UTF_8);
                    PostMessage postMessage = new Gson().fromJson(response, PostMessage.class);
                    if (postMessage.getMessage()==null){
                        Toast.makeText(mContext,"注册成功，请登录",Toast.LENGTH_SHORT).show();
                        FragmentUtils.popBack(getActivity());
                    }else {
                        Toast.makeText(mContext, postMessage.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(mContext,"请求失败",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
