package cn.edu.neusoft.ypq.gowuu.customer.me.fragment;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

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
import cn.edu.neusoft.ypq.gowuu.admin.bean.Admin;
import cn.edu.neusoft.ypq.gowuu.admin.fragment.AdminFragment;
import cn.edu.neusoft.ypq.gowuu.app.MainActivity;
import cn.edu.neusoft.ypq.gowuu.base.BaseFragment;
import cn.edu.neusoft.ypq.gowuu.business.bean.Business;
import cn.edu.neusoft.ypq.gowuu.business.fragment.BusinessFragment;
import cn.edu.neusoft.ypq.gowuu.customer.home.extra.favorite.FavoriteFragment;
import cn.edu.neusoft.ypq.gowuu.customer.me.extra.address.AddressFragment;
import cn.edu.neusoft.ypq.gowuu.customer.me.extra.change_info.ChangeUsrFragment;
import cn.edu.neusoft.ypq.gowuu.customer.me.extra.message.MessageFragment;
import cn.edu.neusoft.ypq.gowuu.customer.me.extra.order.UsrOrderFragment;
import cn.edu.neusoft.ypq.gowuu.customer.me.extra.request.RequestFragment;
import cn.edu.neusoft.ypq.gowuu.user.fragment.LoginFragment;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.FragmentUtils;
import cn.edu.neusoft.ypq.gowuu.utils.GlideUtils;
import cn.edu.neusoft.ypq.gowuu.utils.PostMessage;
import cz.msebera.android.httpclient.Header;


/**
 * 作者:颜培琦
 * 时间:2022/2/14
 * 功能:个人信息页面Fragment的实现，主要有以下几个功能
 * 1.用户管理
 * 2.用户信息管理
 * 3.进入店铺
 * 4.进入
 */

public class MeFragment extends BaseFragment {

    @BindView(R.id.me_tv_user_name)
    TextView tvName;
    @BindView(R.id.me_img_avatar)
    ImageView ivAvatar;
    @BindView(R.id.me_bt_not_paid)
    LinearLayout btNotPaid;
    @BindView(R.id.me_bt_paid)
    LinearLayout btPaid;
    @BindView(R.id.me_bt_not_appraised)
    LinearLayout btNotAppraised;
    @BindView(R.id.me_bt_appraised)
    LinearLayout btAppraised;
    @BindView(R.id.me_bt_my_favorite)
    TextView btMyFavorite;
    @BindView(R.id.me_bt_address_management)
    TextView btAddress;
    @BindView(R.id.me_bt_my_shop)
    TextView btMyShop;
    @BindView(R.id.me_bt_switch_admin)
    TextView btSwitchAdmin;
    @BindView(R.id.me_bt_quit_account)
    TextView btQuitAccount;
    @BindView(R.id.me_view1)
    View view;
    @BindView(R.id.me_cl_usr)
    ConstraintLayout clUser;
    @BindView(R.id.me_bt_message)
    TextView btMessage;

    @Override
    public View initView() {
        Log.e(TAG, "个人信息Fragment的UI被初始化了");
        View view = View.inflate(mContext, R.layout.fragment_cstm_me, null);
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    /**
     * 检查MainActivity的user中是否有数据，有数据装填数据，没有数据隐藏内容
     */
    @Override
    public void initData() {
        super.initData();
        if (MainActivity.user.getUid()==null) {
            tvName.setText("注册/登录");
            ivAvatar.setImageResource(R.drawable.ic_launcher_foreground);
            clUser.setVisibility(View.GONE);
        } else {
            tvName.setText(MainActivity.user.getUsername());
            if (MainActivity.user.getPermission() >= 1){
                btMyShop.setText("我的店铺");
            }else {
                btMyShop.setText("申请店铺");
            }
            if (MainActivity.user.getPermission() == 2){
                btSwitchAdmin.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);
            }else {
                btSwitchAdmin.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
            }
            if (MainActivity.user.getAvatar()==null){
                ivAvatar.setImageResource(R.drawable.ic_launcher_foreground);
            } else {
                setAvatar();
            }
            btQuitAccount.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置头像，网络请求头像资源，设置到本地
     */
    private void setAvatar(){
        String url = Constants.RES_URL+MainActivity.user.getAvatar();
        GlideUtils.setImage(mContext, url, ivAvatar);
    }

    /**
     * 检查登录状态，如果没登陆，进入登陆界面
     */
    private void checkLogin(Fragment fragment) {
        if (MainActivity.user.getUid()==null) {
            Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
        }else {
            FragmentUtils.changeFragment(getActivity(), R.id.main_frameLayout, fragment);
        }
    }

    @OnClick({R.id.me_tv_user_name, R.id.me_img_avatar, R.id.me_tv_order_all,
            R.id.me_bt_not_paid, R.id.me_bt_paid, R.id.me_bt_not_appraised,
            R.id.me_bt_appraised, R.id.me_bt_my_favorite, R.id.me_bt_address_management,
            R.id.me_bt_my_shop, R.id.me_bt_message, R.id.me_bt_switch_admin, R.id.me_bt_quit_account})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.me_tv_user_name:
                if (MainActivity.user.getUid() == null){
                    FragmentUtils.changeFragment(getActivity(), R.id.main_frameLayout, new LoginFragment());
                } else {
                    FragmentUtils.changeFragment(getActivity(), R.id.main_frameLayout, new ChangeUsrFragment(0, MainActivity.user, false));
                }
                break;
            case R.id.me_img_avatar:
                if (MainActivity.user.getUid() == null){
                    FragmentUtils.changeFragment(getActivity(), R.id.main_frameLayout, new LoginFragment());
                } else {
                    FragmentUtils.changeFragment(getActivity(), R.id.main_frameLayout, new ChangeUsrFragment(2, MainActivity.user, false));
                }
                break;
            case R.id.me_tv_order_all:
                checkLogin(new UsrOrderFragment());
                break;
            case R.id.me_bt_not_paid:
                checkLogin(new UsrOrderFragment(1));
                break;
            case R.id.me_bt_paid:
                checkLogin(new UsrOrderFragment(2));
                break;
            case R.id.me_bt_not_appraised:
                checkLogin(new UsrOrderFragment(3));
                break;
            case R.id.me_bt_appraised:
                checkLogin(new UsrOrderFragment(4));
                break;
            case R.id.me_bt_my_favorite:
                checkLogin(new FavoriteFragment());
                break;
            case R.id.me_bt_address_management:
                checkLogin(new AddressFragment());
                break;
            case R.id.me_bt_my_shop:
                shop();
                break;
            case R.id.me_bt_message:
                checkLogin(new MessageFragment());
                break;
            case R.id.me_bt_switch_admin:
                admin();
                break;
            case R.id.me_bt_quit_account:
                quitAccount();
                break;
        }
    }

    private void shop(){
        if (MainActivity.user.getUid() != null){
            if (MainActivity.user.getPermission()>=1){
                String url = Constants.BUSINESS_URL+"/get_business";
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("uid", MainActivity.user.getUid());
                client.get(url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String response = new String(responseBody, StandardCharsets.UTF_8);
                        Type type = new TypeToken<PostMessage<Business>>(){}.getType();
                        PostMessage<Business> postMessage = new Gson().fromJson(response, type);
                        if (postMessage.getMessage() == null){
                            if (postMessage.getData() != null){
                                FragmentUtils.changeFragment(getActivity(), R.id.main_frameLayout, new BusinessFragment());
                                BusinessFragment.business = postMessage.getData();
                            }
                        }else {
                            Toast.makeText(mContext, postMessage.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(mContext, "MeFragment(232):请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                String url = Constants.REQUEST_URL+"/is_applied";
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("uid", MainActivity.user.getUid());
                client.get(url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String response = new String(responseBody, StandardCharsets.UTF_8);
                        Type type = new TypeToken<PostMessage<Void>>(){}.getType();
                        PostMessage<Void> postMessage = new Gson().fromJson(response, type);
                        if (postMessage.getMessage() == null){
                            FragmentUtils.changeFragment(getActivity(), R.id.main_frameLayout, new RequestFragment(true, null));
                        } else {
                            Toast.makeText(mContext, postMessage.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(mContext, "MeFragment(254):请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
        }
    }

    private void admin() {
        String url = Constants.ADMIN_URL+"/get_admin_data";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody, StandardCharsets.UTF_8);
                Type type = new TypeToken<PostMessage<Admin>>(){}.getType();
                PostMessage<Admin> postMessage = new Gson().fromJson(response, type);
                if (postMessage.getMessage() == null){
                    Admin admin = postMessage.getData();
                    FragmentUtils.changeFragment(getActivity(), R.id.main_frameLayout, new AdminFragment(admin));
                } else {
                    Toast.makeText(mContext, postMessage.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(mContext, "MeFragment(279):请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void quitAccount(){
        MainActivity.user.clearUser();
        //清除本地用户信息存储文件
        SharedPreferences preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();

        initData();
        Toast.makeText(mContext,"退出成功",Toast.LENGTH_SHORT).show();
    }

}