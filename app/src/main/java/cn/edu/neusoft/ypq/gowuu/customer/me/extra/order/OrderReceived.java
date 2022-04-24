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
import cn.edu.neusoft.ypq.gowuu.customer.me.adapter.OrderAdapter;
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
 * 功能:OrderReceived
 */
public class OrderReceived extends BaseFragment<Order> {

    public static boolean pageEnd = false;
    public static OrderAdapter adapter;
    public static List<Order> dataList;

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
        params.put("state", 3);
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
        adapter.evaluation(new OrderAdapter.OnGoodsEvaluationListener() {
            @Override
            public void evaluation(ViewHolder holder, OrderGoods goods, int fPosition, int position) {
                //评价
                Order data = adapter.getDataList().get(fPosition);
                GoodsEvaluation.order = new Order();
                GoodsEvaluation.order.setBznsName(data.getBznsName());
                GoodsEvaluation.order.setPosition(fPosition);
                List<OrderGoods> goodsList = new ArrayList<>();
                goods.setPosition(position);
                goodsList.add(goods);
                GoodsEvaluation.order.setGoodsList(goodsList);
                FragmentUtils.changeFragment(getActivity(), R.id.main_frameLayout, new GoodsEvaluation());
            }
        });
    }
}
