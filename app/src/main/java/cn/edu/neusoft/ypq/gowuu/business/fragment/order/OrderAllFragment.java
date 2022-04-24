package cn.edu.neusoft.ypq.gowuu.business.fragment.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
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
import cn.edu.neusoft.ypq.gowuu.base.BaseFragment;
import cn.edu.neusoft.ypq.gowuu.base.ViewHolder;
import cn.edu.neusoft.ypq.gowuu.business.adapter.BusinessOrderAdapter;
import cn.edu.neusoft.ypq.gowuu.business.fragment.BusinessFragment;
import cn.edu.neusoft.ypq.gowuu.customer.me.bean.Order;
import cn.edu.neusoft.ypq.gowuu.customer.me.extra.order.OrderNotPay;
import cn.edu.neusoft.ypq.gowuu.customer.me.extra.order.OrderNotReceive;
import cn.edu.neusoft.ypq.gowuu.customer.me.extra.order.OrderNotSend;
import cn.edu.neusoft.ypq.gowuu.customer.me.extra.order.OrderReceived;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.PostMessage;
import cn.edu.neusoft.ypq.gowuu.utils.RecyclerViewUtils;
import cz.msebera.android.httpclient.Header;

/**
 * 作者:颜培琦
 * 时间:2022/3/10
 * 功能:OrderNotPayFragment
 */
public class OrderAllFragment extends BaseFragment<Order> {
    public static BusinessOrderAdapter adapter;

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
        adapter.cancel(new BusinessOrderAdapter.OnOrderCancelListener() {
            @Override
            public void cancel(ViewHolder holder, Order order, int position) {
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
                            if (OrderNotPayFragment.adapter != null){
                                int p = OrderNotPayFragment.adapter.getDataList().indexOf(order);
                                if (p != -1){
                                    OrderNotPayFragment.adapter.getDataList().remove(p);
                                    OrderNotPayFragment.adapter.notifyItemRemoved(p);
                                }
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
                        Toast.makeText(mContext, "OrderAllFragment(cancel):请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        adapter.changePrice(new BusinessOrderAdapter.OnOrderChangePriceListener() {
            @Override
            public void changePrice(ViewHolder holder, Order order, int position) {
                EditText etPrice = new EditText(mContext);
                etPrice.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                new AlertDialog.Builder(mContext).setTitle("更改价格")
                        .setMessage("请输入修改的价格")
                        .setView(etPrice)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        int p = adapter.getDataList().indexOf(order);
                                        changePriceCommit(order, p, Double.parseDouble(etPrice.getText().toString()));
                                    }
                                })
                        .setNegativeButton("取消",null).show();
            }
        });

        adapter.send(new BusinessOrderAdapter.OnOrderSendListener() {
            @Override
            public void send(ViewHolder holder, Order order, int position) {
                String url = Constants.ORDER_URL+"/change_state";
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("oid", order.getOid());
                params.put("state", 2);
                client.post(url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String response = new String(responseBody, StandardCharsets.UTF_8);
                        Type type = new TypeToken<PostMessage<Void>>() {
                        }.getType();
                        PostMessage<Void> postMessage = new Gson().fromJson(response, type);
                        if (postMessage.getMessage() == null){
                            if (OrderNotSendFragment.adapter != null){
                                int p = OrderNotSendFragment.adapter.getDataList().indexOf(order);
                                if (p != -1){
                                    OrderNotSendFragment.adapter.getDataList().remove(p);
                                    OrderNotSendFragment.adapter.notifyItemRemoved(p);
                                }
                            }
                            adapter.getDataList().get(position).setState(3);
                            adapter.notifyItemChanged(position);
                            if (OrderSendFragment.adapter != null && OrderSendFragment.pageEnd){
                                Order receivedData = adapter.getDataList().get(position);
                                OrderSendFragment.adapter.insert(receivedData);
                            }
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
                            if (OrderNotPayFragment.adapter != null){
                                int p = OrderNotPayFragment.adapter.getDataList().indexOf(order);
                                if (p != -1){
                                    OrderNotPayFragment.adapter.getDataList().get(p).setPrice(price);
                                    OrderNotPayFragment.adapter.notifyItemChanged(p);
                                }
                            }
                            adapter.getDataList().get(position).setPrice(price);
                            adapter.notifyItemChanged(position);
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
}
