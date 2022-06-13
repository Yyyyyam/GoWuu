package cn.edu.neusoft.ypq.gowuu.customer.me.extra.change_info;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.base.BaseFragment;
import cn.edu.neusoft.ypq.gowuu.user.bean.User;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.FileUtils;
import cn.edu.neusoft.ypq.gowuu.utils.FragmentUtils;
import cn.edu.neusoft.ypq.gowuu.utils.GlideUtils;
import cn.edu.neusoft.ypq.gowuu.utils.PostMessage;
import cz.msebera.android.httpclient.Header;

/**
 * 作者:颜培琦
 * 时间:2022/3/4
 * 功能:ChangeAvatarFragment
 */
public class ChangeAvatarFragment extends BaseFragment<Void> {

    private File imgFile = null;
    private final User user;
    private final boolean isManage;

    @BindView(R.id.usr_iv_change_avatar)
    ImageView imgAvatar;
    @BindView(R.id.usr_bt_change_avatar)
    Button btChange;

    public ChangeAvatarFragment(User user, boolean isManage){
        this.user = user;
        this.isManage = isManage;
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_cstm_change_arvatar, null);
        ButterKnife.bind(this, view);

        initImg();
        return view;
    }

    public void initImg(){
        String avatar = user.getAvatar();
        if (avatar == null){
            imgAvatar.setImageResource(R.drawable.ic_launcher_foreground);
        } else {
            String url = Constants.RES_URL+user.getAvatar();
            GlideUtils.setImage(mContext, url, imgAvatar);
        }
    }

    @OnClick({R.id.usr_iv_change_avatar, R.id.usr_bt_change_avatar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.usr_iv_change_avatar:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
                break;

            case R.id.usr_bt_change_avatar:
                uploadImg();
                break;
        }
    }

    public void uploadImg() {
        String url = Constants.USR_URL+"/change_avatar";
        //提交数据
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params =new RequestParams();
        try {
            params.put("avatar", imgFile);
        } catch (FileNotFoundException e) {
            Toast.makeText(mContext,"请选择新的头像",Toast.LENGTH_SHORT).show();
            return;
        }
        params.put("uid", user.getUid());
        params.put("path",user.getAvatar());
        client.post(url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String response = new String(responseBody, StandardCharsets.UTF_8);
                        //修正泛型无法正常解析问题
                        Type type = new TypeToken<PostMessage<String>>() {
                        }.getType();
                        PostMessage<String> postMessage = new Gson().fromJson(response, type);
                        if (postMessage.getMessage()==null){
                            String src = postMessage.getData();
                            user.setAvatar(src);
                            Toast.makeText(mContext,"修改成功",Toast.LENGTH_SHORT).show();
                            //保存数据到SharedPreferences
                            if (!isManage) FileUtils.saveUserInfo(mContext);
                            FragmentUtils.popBack(requireActivity());
                        }else {
                            Toast.makeText(mContext, postMessage.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(mContext,"AddressEditFragment(124):请求失败"+error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri uri = data.getData();
                    String picPath = FileUtils.getPath(requireActivity(), uri);
                    imgFile = new File(picPath);
                    imgAvatar.setImageURI(uri);
                    Log.d("result", "uri:" + uri + "filePathForN" + picPath);
                }
            }
        }
    }
}
