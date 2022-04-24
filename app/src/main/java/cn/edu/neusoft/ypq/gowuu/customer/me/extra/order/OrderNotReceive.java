package cn.edu.neusoft.ypq.gowuu.customer.me.extra.order;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.app.MainActivity;
import cn.edu.neusoft.ypq.gowuu.base.BaseFragment;
import cn.edu.neusoft.ypq.gowuu.base.ViewHolder;
import cn.edu.neusoft.ypq.gowuu.business.fragment.order.OrderSendFragment;
import cn.edu.neusoft.ypq.gowuu.customer.cart.bean.CartGoods;
import cn.edu.neusoft.ypq.gowuu.customer.me.adapter.OrderAdapter;
import cn.edu.neusoft.ypq.gowuu.customer.me.bean.Order;
import cn.edu.neusoft.ypq.gowuu.customer.me.bean.OrderGoods;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.PostMessage;
import cn.edu.neusoft.ypq.gowuu.utils.RecyclerViewUtils;
import cz.msebera.android.httpclient.Header;

/**
 * 作者:颜培琦
 * 时间:2022/3/8
 * 功能:OrderNotReceive
 */
public class OrderNotReceive extends BaseFragment<Order> {
    public static OrderAdapter adapter;

    @BindView(R.id.order_state_rv)
    RecyclerView recyclerView;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_bzns_order_state, null);
        ButterKnife.bind(this, view);
        page = 0;
        pageSize = 4;
        pageEnd = false;
        dataList = new ArrayList<>();
        adapter = new OrderAdapter(mContext, dataList);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!pageEnd && ((recyclerView.computeVerticalScrollOffset() > 0)
                        ||(RecyclerViewUtils.isVisBottom(recyclerView)))){
                    getOrderPage();
                }
            }
        });

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        setClickListener();
        getOrderPage();
    }

    private void getOrderPage(){
        String url = Constants.ORDER_URL+"/get_order_state";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("uid", MainActivity.user.getUid());
        params.put("page", page+=1);
        params.put("pageSize", pageSize);
        params.put("state", 2);
        client.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody, StandardCharsets.UTF_8);
                Type type = new TypeToken<PostMessage<List<Order>>>() {
                }.getType();
                PostMessage<List<Order>> postMessage = new Gson().fromJson(response, type);
                if (postMessage.getMessage() == null){
                    dataList.clear();
                    if (postMessage.getData().size() < pageSize) pageEnd = true;
                    dataList = postMessage.getData();
                    adapter.addMoreData(dataList);
                } else {
                    Toast.makeText(mContext, postMessage.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(mContext,"OrderALL(117):请求失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setClickListener(){
        adapter.receive(new OrderAdapter.OnOrderReceiveListener() {
            @Override
            public void receive(ViewHolder holder, Order order, int position) {
                String url = Constants.ORDER_URL+"/change_state";
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("oid", order.getOid());
                params.put("state", 3);
                client.post(url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String response = new String(responseBody, StandardCharsets.UTF_8);
                        Type type = new TypeToken<PostMessage<Void>>() {
                        }.getType();
                        PostMessage<Void> postMessage = new Gson().fromJson(response, type);
                        if (postMessage.getMessage() == null){
                            if (OrderAll.adapter != null){
                                int p = OrderAll.adapter.getDataList().indexOf(order);
                                if (p != -1){
                                    OrderAll.adapter.getDataList().get(p).setState(3);
                                    OrderAll.adapter.notifyItemChanged(p);
                                    List<OrderGoods> goodsList = OrderAll.adapter.goodsAdapterList.get(p).getDataList();
                                    for (int i=0; i<goodsList.size(); i++){
                                        OrderGoods goods = goodsList.get(i);
                                        goods.setState(2);
                                        OrderAll.adapter.goodsAdapterList.get(p).notifyItemChanged(i);
                                    }
                                }
                            }
                            if (OrderSendFragment.adapter != null && OrderSendFragment.pageEnd){
                                Order receivedData = adapter.getDataList().get(position);
                                receivedData.setState(3);
                                for (int i=0; i<receivedData.getGoodsList().size(); i++) {
                                    OrderGoods goods = receivedData.getGoodsList().get(i);
                                    goods.setState(2);
                                }
                                OrderSendFragment.adapter.insert(receivedData);
                            }
                            int p = adapter.getDataList().indexOf(order);
                            adapter.getDataList().remove(order);
                            adapter.notifyItemRemoved(p);
                        } else {
                            Toast.makeText(mContext, postMessage.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(mContext,"OrderALL(140):请求失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
