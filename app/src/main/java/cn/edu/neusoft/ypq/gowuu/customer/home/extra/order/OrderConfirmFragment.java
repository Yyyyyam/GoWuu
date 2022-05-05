package cn.edu.neusoft.ypq.gowuu.customer.home.extra.order;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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
import cn.edu.neusoft.ypq.gowuu.business.bean.Business;
import cn.edu.neusoft.ypq.gowuu.customer.cart.bean.Cart;
import cn.edu.neusoft.ypq.gowuu.customer.cart.bean.CartGoods;
import cn.edu.neusoft.ypq.gowuu.customer.cart.bean.CartHeader;
import cn.edu.neusoft.ypq.gowuu.customer.home.adapter.OrderConfirmAdapter;
import cn.edu.neusoft.ypq.gowuu.customer.home.extra.dialog.BuyDialogFragment;
import cn.edu.neusoft.ypq.gowuu.customer.me.bean.Address;
import cn.edu.neusoft.ypq.gowuu.customer.me.bean.Order;
import cn.edu.neusoft.ypq.gowuu.customer.me.bean.OrderGoods;
import cn.edu.neusoft.ypq.gowuu.customer.me.extra.address.AddressFragment;
import cn.edu.neusoft.ypq.gowuu.utils.CheckUtils;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.FragmentUtils;
import cn.edu.neusoft.ypq.gowuu.utils.PostMessage;
import cz.msebera.android.httpclient.Header;

/**
 * 作者:颜培琦
 * 时间:2022/3/30
 * 功能:OrderConfirmFragment
 */
public class OrderConfirmFragment extends BaseFragment<Cart> {

    public static Cart cart;
    public static Address address;
    public static List<Cart> cartList;
    public static boolean selectAddress = false;

    @BindView(R.id.order_cfm_tv_addressee_name)
    TextView tvName;
    @BindView(R.id.order_cfm_tv_addressee_phone)
    TextView tvPhone;
    @BindView(R.id.order_cfm_tv_address_area)
    TextView tvArea;
    @BindView(R.id.order_cfm_tv_address_detail)
    TextView tvDetail;
    @BindView(R.id.order_cfm_rv)
    RecyclerView recyclerView;


    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_order_confirm, null);
        ButterKnife.bind(this, view);

        dataList = new ArrayList<>();
        adapter = new OrderConfirmAdapter(mContext, dataList);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        buyListener();

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        if (BuyDialogFragment.state){
            getBusinessName();
        } else {
            adapter.addMoreData(cartList);
            updatePrice(dataList);
        }
        setAddress();
    }

    public void getBusinessName(){
        String url = Constants.BUSINESS_URL+"/find_business";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("bid", cart.getGoodsList().get(0).getGoods().getBid());
        client.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody, StandardCharsets.UTF_8);
                Type type = new TypeToken<PostMessage<Business>>() {
                }.getType();
                PostMessage<Business> postMessage = new Gson().fromJson(response, type);
                if (postMessage.getMessage() == null){
                    CartHeader header = new CartHeader();
                    header.setBznsName(postMessage.getData().getName());
                    cart.setBid(postMessage.getData().getBid());
                    header.setBznsName(postMessage.getData().getName());
                    cart.setHeader(header);
                    cart.setUid(MainActivity.user.getUid());
                    adapter.insert(cart);
                    updatePrice(dataList);
                } else {
                    Toast.makeText(mContext, postMessage.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(mContext, "OrderConfirmFragment():请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setAddress() {
        if (address == null){
            String url = Constants.ADDRESS_URL+"/get_address";
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("uid", MainActivity.user.getUid());
            params.put("page",1);
            params.put("pageSize",1);
            client.get(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody, StandardCharsets.UTF_8);
                    Type type = new TypeToken<PostMessage<List<Address>>>() {
                    }.getType();
                    PostMessage<List<Address>> postMessage = new Gson().fromJson(response, type);
                    if (postMessage.getMessage() == null) {
                        if (postMessage.getData().size() == 0) {
                            Toast.makeText(mContext, "暂无地址信息，请添加地址数据", Toast.LENGTH_SHORT).show();
                            selectAddress = true;
                            FragmentUtils.changeFragment(getActivity(), R.id.main_frameLayout, new AddressFragment());
                        } else {
                            address = postMessage.getData().get(0);
                            tvName.setText("姓名："+address.getName());
                            tvPhone.setText("电话："+address.getPhone());
                            tvArea.setText("区域："+address.getProvinceName()+"-"+address.getCityName()+"-"+address.getAreaName());
                            tvDetail.setText("地址："+address.getDetail());
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(mContext, "OrderConfirmFragment():请求失败", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            tvName.setText("姓名："+address.getName());
            tvPhone.setText("电话："+address.getPhone());
            tvArea.setText("区域："+address.getProvinceName()+"-"+address.getCityName()+"-"+address.getAreaName());
            tvDetail.setText("地址："+address.getDetail());
        }

    }

    @OnClick(R.id.order_cfm_constraint_address)
    public void selectAddress(){
        selectAddress = true;
        FragmentUtils.changeFragment(getActivity(), R.id.main_frameLayout, new AddressFragment());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        address = null;
        setAddress();
    }

    public void updatePrice(List<Cart> cartList){
        TextView tvPrice = getActivity().findViewById(R.id.order_cfm_frame_tv_price);
        TextView tvCount = getActivity().findViewById(R.id.order_cfm_frame_tv_count);
        Double price = 0.0;
        Integer count = 0;
        for (int i=0; i<cartList.size(); i++){
            List<CartGoods> goodsList = cartList.get(i).getGoodsList();
            for (int j = 0; j< goodsList.size(); j++){
                count += goodsList.get(j).getCount();
                Double goodsPrice = goodsList.get(j).getGoods().getPrice();
                price = CheckUtils.doubleTrim(price
                                +CheckUtils.doubleTrim(goodsList.get(j).getCount()
                                *goodsPrice*goodsList.get(j).getGoods().getDiscount()));
            }
        }
        tvCount.setText("共"+count+"件");
        tvPrice.setText(String.valueOf(price));
    }

    public void buyListener(){
        Button btBuy = getActivity().findViewById(R.id.order_cfm_frame_bt_commit);
        btBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Order> orderList = getOrderList();
                String url = Constants.ORDER_URL+"/create_order";
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("orderJson", new Gson().toJson(orderList));
                client.post(url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String response = new String(responseBody, StandardCharsets.UTF_8);
                        Type type = new TypeToken<PostMessage<Void>>() {
                        }.getType();
                        PostMessage<Void> postMessage = new Gson().fromJson(response, type);
                        if (postMessage.getMessage() == null) {
                            Toast.makeText(mContext, "提交成功", Toast.LENGTH_SHORT).show();
                            BuyDialogFragment.state = false;
                            //后续弹出是否付款，付款直接提交改变订单状态为1
                            FragmentUtils.popBack(getActivity());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(mContext, "OrderConfirmFragment():请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public List<Order> getOrderList(){
        List<Cart> dataList = adapter.getDataList();
        List<Order> orderList = new ArrayList<>();
        for (Cart cart : dataList) {
            Order order = new Order();
            List<OrderGoods> goodsList = new ArrayList<>();
            double price = 0;
            order.setUid(cart.getUid());
            order.setBid(cart.getBid());
            order.setBznsName(cart.getHeader().getBznsName());
            order.setRecvName(address.getName());
            order.setRecvPhone(address.getPhone());
            order.setRecvProvince(address.getProvinceName());
            order.setRecvCity(address.getCityName());
            order.setRecvArea(address.getAreaName());
            order.setRecvAddress(address.getDetail());
            for (CartGoods goods : cart.getGoodsList()){
                OrderGoods orderGoods = new OrderGoods();
                orderGoods.setGid(goods.getGoods().getGid());
                orderGoods.setName(goods.getGoods().getName());
                orderGoods.setPic(goods.getGoods().getPathList().get(0));
                orderGoods.setPrice(CheckUtils.doubleTrim(goods.getGoods().getPrice()
                                        * goods.getGoods().getDiscount()));
                orderGoods.setCategory(goods.getGoods().getCategory());
                orderGoods.setCount(goods.getCount());
                price = CheckUtils.doubleTrim(price +
                            CheckUtils.doubleTrim(goods.getGoods().getDiscount()
                                    * goods.getGoods().getPrice() * goods.getCount()));
                goodsList.add(orderGoods);
            }
            order.setPrice(price);
            order.setGoodsList(goodsList);
            orderList.add(order);
        }
        return orderList;
    }
}
