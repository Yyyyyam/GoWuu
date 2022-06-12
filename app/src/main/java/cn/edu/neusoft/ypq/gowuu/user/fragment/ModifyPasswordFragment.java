package cn.edu.neusoft.ypq.gowuu.user.fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
 * 功能:ModifyPasswordFragment
 */
public class ModifyPasswordFragment extends BaseFragment {

    @BindView(R.id.mod_pas_ib_back)
    ImageButton btBcak;
    @BindView(R.id.mod_pas_et_phone)
    EditText etPhone;
    @BindView(R.id.mod_pas_tv_phone_check)
    TextView tvPhoneCheck;
    @BindView(R.id.mod_pas_et_email)
    EditText etEmail;
    @BindView(R.id.mod_pas_tv_email_check)
    TextView tvEmailCheck;
    @BindView(R.id.mod_pas_et_password)
    EditText etPassword;
    @BindView(R.id.mod_pas_et_cfmpassword)
    EditText etCfmPassword;
    @BindView(R.id.mod_pas_bt_confirm)
    Button btConfirm;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_usr_mod_pas, null);
        ButterKnife.bind(this, view);

        CheckUtils.startPhoneCheck(etPhone, tvPhoneCheck);
        CheckUtils.startEmailCheck(etEmail, tvEmailCheck);
        return view;
    }


    @OnClick({R.id.mod_pas_ib_back, R.id.mod_pas_bt_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mod_pas_ib_back:
                FragmentUtils.popBack(requireActivity());
                break;
            case R.id.mod_pas_bt_confirm:
                modPass();
                break;
        }
    }

    public void modPass(){
        String url = Constants.USR_URL+"/mod_pas";
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String cfmPassword = etCfmPassword.getText().toString().trim();

        if (phone.isEmpty()||email.isEmpty()||password.isEmpty()||cfmPassword.isEmpty()){
            Toast.makeText(mContext,"请补全信息",Toast.LENGTH_SHORT).show();
        } else if (!CheckUtils.isPhone(phone)){
            Toast.makeText(mContext,"请填写正确的手机号",Toast.LENGTH_SHORT).show();
        } else if (!CheckUtils.isEmail(email)){
            Toast.makeText(mContext,"请填写正确的邮箱",Toast.LENGTH_SHORT).show();
        } else if (!cfmPassword.equals(password)){
            Toast.makeText(mContext,"两次密码输入不一致",Toast.LENGTH_SHORT).show();
        } else {
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params =new RequestParams();
            params.put("phone",phone);
            params.put("email",email);
            params.put("password",password);
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody, StandardCharsets.UTF_8);
                    PostMessage postMessage = new Gson().fromJson(response, PostMessage.class);
                    if (postMessage.getMessage()==null){
                        Toast.makeText(mContext,"修改成功，请重新登录",Toast.LENGTH_SHORT).show();
                        FragmentUtils.popBack(requireActivity());
                    }else {
                        Toast.makeText(mContext, postMessage.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(mContext,"ModifyPasFragment(112):请求失败",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
