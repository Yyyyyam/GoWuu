package cn.edu.neusoft.ypq.gowuu.customer.me.extra.change_info;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import cn.edu.neusoft.ypq.gowuu.base.BaseFragment;
import cn.edu.neusoft.ypq.gowuu.user.bean.User;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.FragmentUtils;
import cn.edu.neusoft.ypq.gowuu.utils.PostMessage;
import cz.msebera.android.httpclient.Header;

/**
 * 作者:颜培琦
 * 时间:2022/3/4
 * 功能:ChangePasFragment
 */
public class ChangePasFragment extends BaseFragment<Void> {
    private final User user;
    private boolean isManage;

    @BindView(R.id.usr_et_old_pas)
    EditText etOldPas;
    @BindView(R.id.usr_et_new_pas)
    EditText etNewPas;
    @BindView(R.id.usr_et_cfm_pas)
    EditText etCfmPas;
    @BindView(R.id.usr_bt_pas_confirm)
    Button btConfirm;

    public ChangePasFragment(User user, boolean isManage){
        this.user = user;
        this.isManage = isManage;
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_cstm_change_pas, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.usr_bt_pas_confirm)
    public void onClick() {
        String url = Constants.USR_URL+"/change_pas";
        String oldPas = etOldPas.getText().toString().trim();
        String newPas = etNewPas.getText().toString().trim();
        String cfmPas = etCfmPas.getText().toString().trim();
        if (oldPas.isEmpty()||newPas.isEmpty()||cfmPas.isEmpty()){
            Toast.makeText(mContext,"请输入全部信息",Toast.LENGTH_SHORT).show();
        } else if (!newPas.equals(cfmPas)){
            Toast.makeText(mContext,"两次输入新密码不一致",Toast.LENGTH_SHORT).show();
        } else if (oldPas.equals(newPas)){
            Toast.makeText(mContext,"旧密码与新密码不能一致",Toast.LENGTH_SHORT).show();
        } else {
            //提交数据
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params =new RequestParams();
            params.put("uid", user.getUid());
            params.put("oldPas", oldPas);
            params.put("newPas", newPas);
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody, StandardCharsets.UTF_8);
                    //修正泛型无法正常解析问题
                    Type type = new TypeToken<PostMessage<Void>>() {
                    }.getType();
                    PostMessage<Void> postMessage = new Gson().fromJson(response, type);
                    if (postMessage.getMessage()==null){
                        Toast.makeText(mContext,"修改成功",Toast.LENGTH_SHORT).show();
                        FragmentUtils.popBack(requireActivity());
                    }else {
                        Toast.makeText(mContext, postMessage.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(mContext,"ChangePasFragment(87):请求失败",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
