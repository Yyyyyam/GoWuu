package cn.edu.neusoft.ypq.gowuu.customer.home.extra.goods;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

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
import cn.edu.neusoft.ypq.gowuu.business.bean.Business;
import cn.edu.neusoft.ypq.gowuu.business.bean.Goods;
import cn.edu.neusoft.ypq.gowuu.customer.home.bean.Favorite;
import cn.edu.neusoft.ypq.gowuu.customer.home.extra.dialog.BuyDialogFragment;
import cn.edu.neusoft.ypq.gowuu.customer.me.extra.request.RequestFragment;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.FragmentUtils;
import cn.edu.neusoft.ypq.gowuu.utils.PostMessage;
import cz.msebera.android.httpclient.Header;

/**
 * 作者:颜培琦
 * 时间:2022/3/25
 * 功能:GoodsFrameFragment
 */
public class GoodsFrameFragment extends BaseFragment<Void> {
    private final Goods goods;
    private Favorite favorite;
    @BindView(R.id.goods_frame_tv_favorite)
    TextView tvFavorite;

    Drawable[] drawables = new Drawable[2];

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_goods_frame, null);
        ButterKnife.bind(this, view);
        FragmentUtils.changeRbFragment(requireActivity(), R.id.goods_frame_layout, new GoodsDetailFragment(goods));
        return view;
    }

    public GoodsFrameFragment(Goods goods){
        this.goods = goods;
    }

    @Override
    public void initData() {
        super.initData();
        drawables[0] = ContextCompat.getDrawable(mContext, R.drawable.ic_favorite);
        drawables[1] = ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_select);
        String url = Constants.GOODS_URL+"/is_favorite";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("uid", MainActivity.user.getUid());
        params.put("gid", goods.getGid());
        client.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody, StandardCharsets.UTF_8);
                Type type = new TypeToken<PostMessage<Favorite>>() {
                }.getType();
                PostMessage<Favorite> postMessage = new Gson().fromJson(response, type);
                if (postMessage.getMessage() == null) {
                    favorite = postMessage.getData();
                    if (favorite == null){
                        tvFavorite.setCompoundDrawablesWithIntrinsicBounds(null, drawables[0], null, null);
                    } else {
                        tvFavorite.setCompoundDrawablesWithIntrinsicBounds(null, drawables[1], null, null);
                    }
                } else {
                    Toast.makeText(mContext,postMessage.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(mContext,"GoodsFrameFragment(87):请求失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.goods_frame_ib_back)
    public void back(){
        FragmentUtils.popBack(requireActivity());
    }

    @OnClick(R.id.goods_frame_tv_favorite)
    public void setFavorite(){
        if (MainActivity.user.getUid() != null) {
            String url;
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            if (favorite == null){
                url = Constants.GOODS_URL+"/add_favorite";
                params.put("uid", MainActivity.user.getUid());
                params.put("gid", goods.getGid());
                params.put("price", goods.getPrice());
            } else {
                url = Constants.GOODS_URL+"/remove_favorite";
                params.put("fid", favorite.getFid());
                params.put("gid", goods.getGid());
            }
            client.put(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody, StandardCharsets.UTF_8);
                    Type type = new TypeToken<PostMessage<Favorite>>() {
                    }.getType();
                    PostMessage<Favorite> postMessage = new Gson().fromJson(response, type);
                    if (postMessage.getMessage()==null){
                        favorite = postMessage.getData();
                        if (favorite  != null){
                            Toast.makeText(mContext,"收藏成功",Toast.LENGTH_SHORT).show();
                            tvFavorite.setCompoundDrawablesWithIntrinsicBounds(null, drawables[1], null, null);
                        } else {
                            Toast.makeText(mContext,"取消收藏成功",Toast.LENGTH_SHORT).show();
                            tvFavorite.setCompoundDrawablesWithIntrinsicBounds(null, drawables[0], null, null);
                        }
                    }else {
                        Toast.makeText(mContext, postMessage.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(mContext,"GoodsFrameFragment(136):请求失败",Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.goods_frame_bt_sp_cart)
    public void addToShoppingCart(){
        if (MainActivity.user.getUid() != null) {
            String url = Constants.USR_URL+"/add_cart";
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("uid", MainActivity.user.getUid());
            params.put("gid", goods.getGid());
            params.put("bid",goods.getBid());
            client.put(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody, StandardCharsets.UTF_8);
                    Type type = new TypeToken<PostMessage<Void>>() {
                    }.getType();
                    PostMessage<Void> postMessage = new Gson().fromJson(response, type);
                    if (postMessage.getMessage()==null){
                        Toast.makeText(mContext,"加入购物车成功",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(mContext, postMessage.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(mContext,"GoodsFrameFragment(166):请求失败",Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.goods_frame_bt_buy)
    public void buy(){
        if (MainActivity.user.getUid() != null) {
            BuyDialogFragment buyFragment = new BuyDialogFragment(goods);
            buyFragment.show(requireActivity().getSupportFragmentManager(), "buyFragment");
        } else {
            Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.goods_frame_tv_store)
    public void store() {
        String url = Constants.BUSINESS_URL+"/find_business";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("bid", goods.getBid());
        client.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody, StandardCharsets.UTF_8);
                Type type = new TypeToken<PostMessage<Business>>() {
                }.getType();
                PostMessage<Business> postMessage = new Gson().fromJson(response, type);
                if (postMessage.getMessage() == null){
                    FragmentUtils.changeFragment(requireActivity(), R.id.main_frameLayout, new BznsStoreFragment(postMessage.getData()));
                } else {
                    Toast.makeText(mContext, postMessage.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(mContext, "GoodsFrameFragment(store):请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.goods_frame_ib_report)
    public void report() {
        FragmentUtils.changeFragment(requireActivity(), R.id.main_frameLayout, new RequestFragment(false, goods.getGid()));
    }
}
