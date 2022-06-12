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
import cn.edu.neusoft.ypq.gowuu.business.fragment.goods.GoodsEditFragment;
import cn.edu.neusoft.ypq.gowuu.business.fragment.order.OrderNotPayFragment;
import cn.edu.neusoft.ypq.gowuu.customer.me.adapter.AddressAdapter;
import cn.edu.neusoft.ypq.gowuu.customer.me.adapter.OrderAdapter;
import cn.edu.neusoft.ypq.gowuu.customer.me.bean.Address;
import cn.edu.neusoft.ypq.gowuu.customer.me.bean.Order;
import cn.edu.neusoft.ypq.gowuu.customer.me.bean.OrderGoods;
import cn.edu.neusoft.ypq.gowuu.customer.me.extra.evaluation.GoodsEvaluation;
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
                        if (order.getState() == 0 && OrderNotPay.adapter != null){
                            int p = OrderNotPay.adapter.getDataList().indexOf(order);
                            if (p != -1){
                                OrderNotPay.adapter.getDataList().remove(p);
                                OrderNotPay.adapter.notifyItemRemoved(p);
                            }
                        } else if (order.getState() == 1 && OrderNotSend.adapter != null){
                            int p = OrderNotSend.adapter.getDataList().indexOf(order);
                            if (p != -1){
                                OrderNotSend.adapter.getDataList().remove(p);
                                OrderNotSend.adapter.notifyItemRemoved(p);
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
                    Toast.makeText(mContext,"OrderALL(140):请求失败",Toast.LENGTH_SHORT).show();
                }
            });
        });

        adapter.pay(new OrderAdapter.OnOrderPayListener() {
            @Override
            public void pay(ViewHolder holder, Order order, int position) {
                String url = Constants.ORDER_URL+"/change_state";
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("oid", order.getOid());
                params.put("state", 1);
                client.post(url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String response = new String(responseBody, StandardCharsets.UTF_8);
                        Type type = new TypeToken<PostMessage<Void>>() {
                        }.getType();
                        PostMessage<Void> postMessage = new Gson().fromJson(response, type);
                        if (postMessage.getMessage() == null){
                            if (OrderNotPay.adapter != null){
                                int p = OrderNotPay.adapter.getDataList().indexOf(order);
                                if (p != -1){
                                    OrderNotPay.adapter.getDataList().remove(p);
                                    OrderNotPay.adapter.notifyItemRemoved(p);
                                }
                            }
                            adapter.getDataList().get(position).setState(1);
                            adapter.notifyItemChanged(position);
                            if (OrderNotSend.adapter != null && OrderNotSend.pageEnd){
                                Order paidData = adapter.getDataList().get(position);
                                OrderNotSend.adapter.insert(paidData);
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
                            if (OrderNotReceive.adapter != null){
                                int p = OrderNotReceive.adapter.getDataList().indexOf(order);
                                if (p != -1){
                                    OrderNotReceive.adapter.getDataList().remove(p);
                                    OrderNotReceive.adapter.notifyItemRemoved(p);
                                }
                            }
                            order.setState(3);
                            for (int i=0; i<order.getGoodsList().size(); i++){
                                OrderGoods goods = order.getGoodsList().get(i);
                                goods.setState(2);
                            }
                            adapter.notifyItemChanged(position);
                            if (OrderReceived.adapter != null && OrderReceived.pageEnd){
                                Order receivedData = adapter.getDataList().get(position);
                                OrderReceived.adapter.insert(receivedData);
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

        adapter.evaluation(new OrderAdapter.OnGoodsEvaluationListener() {
            @Override
            public void evaluation(ViewHolder holder, OrderGoods goods, int fPosition, int position) {
                //设计评价界面，包括好评差评、评价内容和图片
                Order data = adapter.getDataList().get(fPosition);
                GoodsEvaluation.order = new Order();
                GoodsEvaluation.order.setBznsName(data.getBznsName());
                GoodsEvaluation.order.setPosition(fPosition);
                List<OrderGoods> goodsList = new ArrayList<>();
                goods.setPosition(position);
                goodsList.add(goods);
                GoodsEvaluation.order.setGoodsList(goodsList);
                FragmentUtils.changeFragment(requireActivity(), R.id.main_frameLayout, new GoodsEvaluation());
            }
        });
    }
}
