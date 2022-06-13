package cn.edu.neusoft.ypq.gowuu.business.fragment.order;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.base.BaseFragment;
import cn.edu.neusoft.ypq.gowuu.business.adapter.BusinessOrderAdapter;
import cn.edu.neusoft.ypq.gowuu.business.fragment.BusinessFragment;
import cn.edu.neusoft.ypq.gowuu.customer.me.bean.Order;
import cn.edu.neusoft.ypq.gowuu.receiver.OrderChangeReceiver;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.PostMessage;
import cn.edu.neusoft.ypq.gowuu.utils.RecyclerViewUtils;
import cz.msebera.android.httpclient.Header;

/**
 * 作者:颜培琦
 * 时间:2022/3/10
 * 功能:OrderNotPayFragment
 */
public class OrderNotPayFragment extends BaseFragment<Order> {
    private BusinessOrderAdapter adapter;
    private OrderChangeReceiver receiver;
    private LocalBroadcastManager broadcastManager;

    @BindView(R.id.fragment_recycler_view)
    RecyclerView recyclerView;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_for_rv, null);
        ButterKnife.bind(this, view);

        page = 0;
        pageSize = 4;
        pageEnd = false;
        dataList = new ArrayList<>();
        adapter = new BusinessOrderAdapter(mContext, dataList);
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

    public void getOrderPage(){
        String url = Constants.ORDER_URL+"/get_bzns_order_state";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("bid", BusinessFragment.business.getBid());
        params.put("page", page+=1);
        params.put("pageSize", pageSize);
        params.put("state",0);
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

    public void setClickListener(){
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
                        Intent intent = new Intent();
                        intent.setAction(OrderChangeReceiver.ORDER_STATE_CANCELED);
                        intent.putExtra("order", orderData);
                        broadcastManager.sendBroadcast(intent);
                    } else {
                        Toast.makeText(mContext, postMessage.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(mContext, "OrderAllFragment(cancel):请求失败", Toast.LENGTH_SHORT).show();
                }
            });
        });

        adapter.changePrice((holder, order, position) -> {
            EditText etPrice = new EditText(mContext);
            etPrice.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
            new AlertDialog.Builder(mContext).setTitle("更改价格")
                    .setMessage("请输入修改的价格")
                    .setView(etPrice)
                    .setPositiveButton("确定",
                            (dialogInterface, i) -> {
                                int p = adapter.getDataList().indexOf(order);
                                changePriceCommit(order, p, Double.parseDouble(etPrice.getText().toString()));
                            })
                    .setNegativeButton("取消",null).show();
        });
    }

    private void changePriceCommit(Order order, int position, double price){
        String url = Constants.ORDER_URL+"/modify_price";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("oid", order.getOid());
        params.put("price", price);
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody, StandardCharsets.UTF_8);
                Type type = new TypeToken<PostMessage<Void>>() {
                }.getType();
                PostMessage<Void> postMessage = new Gson().fromJson(response, type);
                if (postMessage.getMessage() == null){
                    Order orderData = new Order(order);
                    Intent intent = new Intent();
                    intent.setAction(OrderChangeReceiver.ORDER_PRICE_MODIFIED);
                    intent.putExtra("order", orderData);
                    intent.putExtra("price", price);
                    LocalBroadcastManager.getInstance(requireActivity()).sendBroadcast(intent);
                } else {
                    Toast.makeText(mContext, postMessage.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(mContext, "OrderAllFragment(cancel):请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(OrderChangeReceiver.ORDER_STATE_CANCELED);
        intentFilter.addAction(OrderChangeReceiver.ORDER_PRICE_MODIFIED);
        receiver = new OrderChangeReceiver();

        broadcastManager = LocalBroadcastManager.getInstance(requireActivity());
        broadcastManager.registerReceiver(receiver, intentFilter);

        receiver.canceled(this::cancel);
        receiver.modify(this::modify);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        broadcastManager.unregisterReceiver(receiver);
    }

    private void cancel(Intent intent) {
        Order order = (Order) intent.getSerializableExtra("order");
        int position = adapter.getDataList().indexOf(order);
        if (position != -1) {
            adapter.getDataList().remove(order);
            adapter.notifyItemRemoved(position);
        }
    }

    private void modify(Intent intent) {
        Order order = (Order) intent.getSerializableExtra("order");
        Double price = intent.getDoubleExtra("price", 0);
        if (order != null) {
            int position = adapter.getDataList().indexOf(order);
            if (position != -1) {
                adapter.getDataList().get(position).setPrice(price);
                adapter.notifyItemChanged(position);
            }
        }
    }
}
