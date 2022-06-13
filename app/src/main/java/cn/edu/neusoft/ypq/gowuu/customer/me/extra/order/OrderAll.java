package cn.edu.neusoft.ypq.gowuu.customer.me.extra.order;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
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
import cn.edu.neusoft.ypq.gowuu.customer.me.adapter.OrderAdapter;
import cn.edu.neusoft.ypq.gowuu.customer.me.bean.Order;
import cn.edu.neusoft.ypq.gowuu.customer.me.bean.OrderGoods;
import cn.edu.neusoft.ypq.gowuu.customer.me.extra.evaluation.GoodsEvaluation;
import cn.edu.neusoft.ypq.gowuu.receiver.OrderChangeReceiver;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.FragmentUtils;
import cn.edu.neusoft.ypq.gowuu.utils.PostMessage;
import cn.edu.neusoft.ypq.gowuu.utils.RecyclerViewUtils;
import cz.msebera.android.httpclient.Header;

/**
 * 作者:颜培琦
 * 时间:2022/3/8
 * 功能:OrderAll
 */
public class OrderAll extends BaseFragment<Order> {
    private OrderAdapter adapter;
    private OrderChangeReceiver receiver;
    private LocalBroadcastManager broadcastManager;

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
        adapter.cancel((holder, order, position) -> {
            String url = Constants.ORDER_URL+"/remove_order";
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("oid", order.getOid());
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody, StandardCharsets.UTF_8);
                    Type type = new TypeToken<PostMessage<Void>>() {
                    }.getType();
                    PostMessage<Void> postMessage = new Gson().fromJson(response, type);
                    if (postMessage.getMessage() == null){
                        Order orderData = new Order(order);
                        Intent i = new Intent();
                        i.setAction(OrderChangeReceiver.ORDER_STATE_CANCELED);
                        i.putExtra("order", orderData);
                        broadcastManager.sendBroadcast(i);
                    } else {
                        Toast.makeText(mContext, postMessage.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(mContext,"OrderALL(140):请求失败",Toast.LENGTH_SHORT).show();
                }
            });
        });

        adapter.pay((holder, order, position) -> {
            String url = Constants.ORDER_URL+"/change_state";
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("oid", order.getOid());
            params.put("state", 1);
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody, StandardCharsets.UTF_8);
                    Type type = new TypeToken<PostMessage<Void>>() {}.getType();
                    PostMessage<Void> postMessage = new Gson().fromJson(response, type);
                    if (postMessage.getMessage() == null){
                        Order orderData = new Order(order);
                        Intent i = new Intent();
                        i.setAction(OrderChangeReceiver.ORDER_STATE_PAID);
                        i.putExtra("order", orderData);
                        broadcastManager.sendBroadcast(i);
                    } else {
                        Toast.makeText(mContext, postMessage.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(mContext,"OrderALL(140):请求失败",Toast.LENGTH_SHORT).show();
                }
            });
        });

        adapter.receive((holder, order, position) -> {
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
                        Order orderData = new Order(order);
                        Intent i = new Intent();
                        i.setAction(OrderChangeReceiver.ORDER_STATE_RECEIVED);
                        i.putExtra("order", orderData);
                        broadcastManager.sendBroadcast(i);
                    } else {
                        Toast.makeText(mContext, postMessage.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(mContext,"OrderALL(140):请求失败",Toast.LENGTH_SHORT).show();
                }
            });
        });

        adapter.evaluation((holder, goods, fPosition, position) -> {
            //设计评价界面，包括好评差评、评价内容和图片
            Order data = adapter.getDataList().get(fPosition);
            GoodsEvaluation.order = new Order(data);
            GoodsEvaluation.position = position;
            FragmentUtils.changeFragment(requireActivity(), R.id.main_frameLayout, new GoodsEvaluation());
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction(OrderChangeReceiver.ORDER_STATE_CANCELED);
        filter.addAction(OrderChangeReceiver.ORDER_STATE_PAID);
        filter.addAction(OrderChangeReceiver.ORDER_STATE_RECEIVED);
        filter.addAction(OrderChangeReceiver.ORDER_STATE_EVALUATION);
        receiver = new OrderChangeReceiver();
        broadcastManager = LocalBroadcastManager.getInstance(requireActivity());
        broadcastManager.registerReceiver(receiver, filter);

        receiver.canceled(this::cancel);
        receiver.paid(this::pay);
        receiver.received(this::receive);
        receiver.evaluation(this::evaluation);
    }

    private void cancel(Intent intent) {
        Order order = (Order) intent.getSerializableExtra("order");
        int position = adapter.getDataList().indexOf(order);
        if (position != -1) {
            adapter.getDataList().remove(order);
            adapter.notifyItemRemoved(position);
        }
    }

    private void pay(Intent intent) {
        Order order = (Order) intent.getSerializableExtra("order");
        if (order != null) {
            order.setState(0);
            int position = adapter.getDataList().indexOf(order);
            if (position != -1) {
                adapter.getDataList().get(position).setState(1);
                adapter.notifyItemChanged(position);
            }
        }

    }

    private void receive(Intent intent) {
        Order order = (Order) intent.getSerializableExtra("order");
        if (order != null) {
            order.setState(2);
            int position = adapter.getDataList().indexOf(order);
            if (position != -1) {
                Order data = adapter.getDataList().get(position);
                data.setState(3);
                for (int i = 0; i< data.getGoodsList().size(); i++){
                    OrderGoods goods = data.getGoodsList().get(i);
                    goods.setState(1);
                }
            }
            adapter.notifyItemChanged(position);
        }
    }

    private void evaluation(Intent intent) {
        Order order = (Order) intent.getSerializableExtra("order");
        if (order != null) {
            order.setState(3);
            int pOrder = adapter.getDataList().indexOf(order);
            if (pOrder != -1) {
                adapter.getDataList().get(pOrder).getGoodsList().get(GoodsEvaluation.position).setState(2);
                adapter.notifyItemChanged(pOrder);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        broadcastManager.unregisterReceiver(receiver);
    }
}
