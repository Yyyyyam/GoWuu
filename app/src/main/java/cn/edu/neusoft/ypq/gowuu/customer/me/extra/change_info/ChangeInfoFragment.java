package cn.edu.neusoft.ypq.gowuu.customer.me.extra.change_info;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
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
import cn.edu.neusoft.ypq.gowuu.utils.CheckUtils;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.FileUtils;
import cn.edu.neusoft.ypq.gowuu.utils.FragmentUtils;
import cn.edu.neusoft.ypq.gowuu.utils.PostMessage;
import cz.msebera.android.httpclient.Header;

/**
 * 作者:颜培琦
 * 时间:2022/3/4
 * 功能:ChangeInfoFragment
 */
public class ChangeInfoFragment extends BaseFragment {

    private int gender = -1;
    private User user;
    private boolean isManage = false;

    @BindView(R.id.usr_et_name)
    EditText etName;
    @BindView(R.id.usr_rg_gender)
    RadioGroup rgGender;
    @BindView(R.id.usr_et_phone)
    EditText etPhone;
    @BindView(R.id.usr_et_email)
    EditText etEmail;
    @BindView(R.id.usr_tv_chek_phone)
    TextView tvPhoneCheck;
    @BindView(R.id.usr_tv_chek_email)
    TextView tvEmailCheck;
    @BindView(R.id.usr_bt_info_confirm)
    Button btConfirm;
    @BindView(R.id.usr_rb_man)
    Button rbMan;
    @BindView(R.id.usr_rb_woman)
    Button rbWoman;
    @BindView(R.id.usr_rb_unknow)
    Button rbUnknow;

    public ChangeInfoFragment(User user, boolean isManage){
        this.user = user;
        if (user.getUid() == MainActivity.user.getUid()) {
            isManage = false;
        } else {
            this.isManage = isManage;
        }
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_cstm_change_info, null);
        ButterKnife.bind(this, view);

        Integer gender = user.getGender();
        etName.setText(user.getUsername());
        if (gender == 0){
            rgGender.check(R.id.usr_rb_unknow);
        } else if (gender == 1){
            rgGender.check(R.id.usr_rb_man);
        } else {
            rgGender.check(R.id.usr_rb_woman);
        }
        etPhone.setText(user.getPhone());
        etEmail.setText(user.getEmail());

        CheckUtils.startPhoneCheck(etPhone, tvPhoneCheck);
        CheckUtils.startEmailCheck(etEmail, tvEmailCheck);
        return view;
    }

    @OnClick(R.id.usr_bt_info_confirm)
    public void onClick() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        if (rgGender.getCheckedRadioButtonId() == R.id.usr_rb_unknow){
            gender = 0;
        } else if (rgGender.getCheckedRadioButtonId() == R.id.usr_rb_man){
            gender = 1;
        } else {
            gender = 2;
        }

        if (name.isEmpty()||phone.isEmpty()||email.isEmpty()){
            Toast.makeText(mContext,"请输入全部信息",Toast.LENGTH_SHORT).show();
        } else if (name.equals(user.getUsername())
                && gender == user.getGender()
                && phone.equals(user.getPhone())
                && email.equals(user.getEmail())){
            Toast.makeText(mContext,"修改信息不能与原信息全部一致",Toast.LENGTH_SHORT).show();
        } else {
            String url = Constants.SERVICE_URL+"users/change_info";
            //提交数据
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params =new RequestParams();
            params.put("uid", user.getUid());
            params.put("username", name);
            params.put("phone", phone);
            params.put("email", email);
            params.put("gender", gender);
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody, StandardCharsets.UTF_8);
                    //修正泛型无法正常解析问题
                    Type type = new TypeToken<PostMessage<Void>>() {
                    }.getType();
                    PostMessage<Void> postMessage = new Gson().fromJson(response, type);
                    if (postMessage.getMessage()==null){
                        if (!isManage) {
                            MainActivity.user.setUsername(name);
                            MainActivity.user.setGender(gender);
                            MainActivity.user.setPhone(phone);
                            MainActivity.user.setEmail(email);
                            //保存数据到SharedPreferences
                            FileUtils.saveUserInfo(mContext);
                        }
                        Toast.makeText(mContext,"修改成功",Toast.LENGTH_SHORT).show();
                        FragmentUtils.popBack(getActivity());
                    }else {
                        Toast.makeText(mContext, postMessage.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(mContext,"ChangeInfoFragment(139):请求失败"+error.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
