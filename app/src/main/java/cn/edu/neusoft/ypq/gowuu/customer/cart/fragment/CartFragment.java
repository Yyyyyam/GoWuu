package cn.edu.neusoft.ypq.gowuu.customer.cart.fragment;

/**
 * 作者:颜培琦
 * 时间:2022/3/4
 * 功能:CartFragment
 */

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.app.MainActivity;
import cn.edu.neusoft.ypq.gowuu.base.BaseFragment;
import cn.edu.neusoft.ypq.gowuu.business.bean.Goods;
import cn.edu.neusoft.ypq.gowuu.customer.cart.adapter.CartAdapter;
import cn.edu.neusoft.ypq.gowuu.customer.cart.bean.Cart;
import cn.edu.neusoft.ypq.gowuu.customer.cart.bean.CartGoods;
import cn.edu.neusoft.ypq.gowuu.customer.home.extra.goods.GoodsDetailFragment;
import cn.edu.neusoft.ypq.gowuu.customer.home.extra.goods.GoodsFrameFragment;
import cn.edu.neusoft.ypq.gowuu.customer.home.extra.order.OrderConfirmFragment;
import cn.edu.neusoft.ypq.gowuu.customer.home.extra.order.OrderConfirmFrameFragment;
import cn.edu.neusoft.ypq.gowuu.utils.CheckUtils;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.FragmentUtils;
import cn.edu.neusoft.ypq.gowuu.utils.PostMessage;
import cz.msebera.android.httpclient.Header;

/**
 * 作者:颜培琦
 * 时间:2022/1/28
 * 功能:购物车Fragment的实现，主要有以下几个功能
 *      1.
 *      2.
 *      3.
 *      4.
 */

public class CartFragment extends BaseFragment<Cart> {

    private CartAdapter adapter;
    private boolean isEdit = false;
    List<Cart> cartSelect = new ArrayList<>();
    List<CartGoods> goodsSelect = new ArrayList<>();

    @BindView(R.id.cart_rv)
    RecyclerView recyclerView;
    @BindView(R.id.cart_tv_price)
    TextView tvPrice;
    @BindView(R.id.cart_tv_edit)
    TextView tvEdit;
    @BindView(R.id.cart_cb_selcet_all)
    CheckBox cbSelectAll;
    @BindView(R.id.cart_bt_payment)
    Button btPay;
    @BindView(R.id.cart_tv_price_desc)
    TextView tv1;
    @BindView(R.id.cart_tv_price_flag)
    TextView tv2;

    @Override
    public View initView() {
        Log.e(TAG, "购物车Fragment的UI被初始化了");
        View view = View.inflate(mContext, R.layout.fragment_cart, null);
        ButterKnife.bind(this, view);

        dataList = new ArrayList<>();
        adapter = new CartAdapter(mContext, dataList);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        setClickListener();

        cbSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Cart> cartList = adapter.getCartList();
                for (int i=0; i<cartList.size(); i++){
                    cartList.get(i).setSelect(cbSelectAll.isChecked());
                    int length = cartList.get(i).getGoodsList().size();
                    for (int j = 0; j < length; j++) {
                        cartList.get(i).getGoodsList().get(j).setSelect(cbSelectAll.isChecked());
                    }
                }
                adapter.notifyItemRangeChanged(0, cartList.size());
                updatePrice();
            }
        });

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        Log.e(TAG, "购物车Fragment的数据被初始化了");
        getCart();
    }

    public void getCart(){
        String url = Constants.USR_URL+"/get_cart";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("uid", MainActivity.user.getUid());
        client.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody, StandardCharsets.UTF_8);
                Type type = new TypeToken<PostMessage<List<Cart>>>() {
                }.getType();
                PostMessage<List<Cart>> postMessage = new Gson().fromJson(response, type);
                if (postMessage.getMessage() == null){
                    dataList.clear();
                    dataList = postMessage.getData();
                    adapter.addMoreData(dataList);
                    updatePrice();
                } else {
                    Toast.makeText(mContext,postMessage.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(mContext,"CartFragment(130):请求失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setClickListener(){
        //设置全选监听
        adapter.setOnSelectAllListener(new CartAdapter.OnSelectAllItemListener() {
            @Override
            public void setOnSelectAllItemListener(boolean selected, View view, int position) {
                //商家全选监听
                List<Cart> cartList = adapter.getCartList();
                cartList.get(position).setSelect(selected);
                int length = cartList.get(position).getGoodsList().size();
                for (int i = 0; i < length; i++) {
                    cartList.get(position).getGoodsList().get(i).setSelect(selected);
                }
                //底部全选监听
                int count = 0;
                int size = 0;
                for (int i = 0; i< cartList.size(); i++){
                    List<CartGoods> goodsList = cartList.get(i).getGoodsList();
                    for (int j=0; j<goodsList.size(); j++){
                        if (goodsList.get(j).getSelect()) count+=1;
                    }
                    size += goodsList.size();
                }
                cbSelectAll.setChecked(count == size);
                updatePrice();
                adapter.notifyItemChanged(position);
            }
        });

        adapter.setOnMoneyChangeListener(new CartAdapter.OnMoneyChangeListener() {
            @Override
            public void setOnMoneyChangeListener(View view, int position) {
                updatePrice();
            }
        });

        adapter.setOnChildSelectListener(new CartAdapter.OnChildSelectListener() {
            @Override
            public void setOnChildSelectListener(List<Cart> cartList, View view, CartAdapter.MyHolder holder, int fPosition, int posiotn) {
                if (!isEdit) {
                    FragmentUtils.changeFragment(getActivity(), R.id.main_frameLayout, new GoodsFrameFragment(cartList.get(fPosition).getGoodsList().get(posiotn).getGoods()));
                } else {
                    //选中
                }
            }
        });

        adapter.setOnBznsSelectListener(new CartAdapter.OnBznsSelectListener() {
            @Override
            public void setOnBznsSelectListener(List<Cart> cartList, View view, int fPosition, int posiotn) {
                int count = 0;
                int size = 0;
                for (int i=0; i<cartList.size(); i++){
                    if (cartList.get(i).isSelect()) count+=1;
                }
                size = cartList.size();
                cbSelectAll.setChecked(count == size);
            }
        });
    }

    private void updatePrice(){
        List<Cart> cartList = adapter.getCartList();
        Double price = 0.0;
        for (int i=0; i<cartList.size(); i++){
            List<CartGoods> goodsList = cartList.get(i).getGoodsList();
            for (int j = 0; j< goodsList.size(); j++){
                if (goodsList.get(j).getSelect()){
                    Integer count = goodsList.get(j).getCount();
                    Double goodsPrice = goodsList.get(j).getGoods().getPrice()*goodsList.get(j).getGoods().getDiscount();
                    price = CheckUtils.doubleTrim(price+CheckUtils.doubleTrim(count*goodsPrice));
                }
            }
        }
        tvPrice.setText(String.valueOf(price));
    }

    @OnClick(R.id.cart_tv_edit)
    public void setEdit(){
        if (isEdit){
            isEdit = false;
            tvEdit.setText("管理");
            btPay.setText("结算");
            tv1.setVisibility(View.VISIBLE);
            tv2.setVisibility(View.VISIBLE);
            tvPrice.setVisibility(View.VISIBLE);
        } else {
            isEdit = true;
            tvEdit.setText("完成");
            btPay.setText("删除");
            tv1.setVisibility(View.GONE);
            tv2.setVisibility(View.GONE);
            tvPrice.setVisibility(View.GONE);
        }
//        cbSelectAll.setChecked(false);
    }

    @OnClick(R.id.cart_bt_payment)
    public void pay(){
        if (MainActivity.user.getUid() != null) {
            if (isEdit){
                removeCart();
            } else {
                List<Cart> cartList = adapter.getConfirmCart();
                if (cartList.size() == 0) {
                    Toast.makeText(mContext, "请选中商品后再进行结算！", Toast.LENGTH_SHORT).show();
                } else {
                    OrderConfirmFragment.cartList = cartList;
                    FragmentUtils.changeFragment(getActivity(), R.id.main_frameLayout, new OrderConfirmFrameFragment());
                }
            }
        } else {
            Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
        }
    }

    public void removeCart(){
        cartSelect = adapter.getSelectCart();
        goodsSelect = adapter.getSelectGoods();
        String url = Constants.USR_URL+"/delete_cart";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("cartSelect", new Gson().toJson(cartSelect));
        params.put("goodsSelect", new Gson().toJson(goodsSelect));
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody, StandardCharsets.UTF_8);
                //修正泛型无法正常解析问题
                Type type = new TypeToken<PostMessage<Void>>() {
                }.getType();
                PostMessage<Void> postMessage = new Gson().fromJson(response, type);
                if (postMessage.getMessage()==null){
                    Toast.makeText(mContext, "移除购物车成功",Toast.LENGTH_SHORT).show();
                    adapter.removeSelect();
                    updatePrice();
                    cbSelectAll.setChecked(false);
                } else {
                    Toast.makeText(mContext, postMessage.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(mContext,"CartFragment(286):请求失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

}