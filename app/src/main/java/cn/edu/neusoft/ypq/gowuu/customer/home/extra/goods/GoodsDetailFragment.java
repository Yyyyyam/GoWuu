package cn.edu.neusoft.ypq.gowuu.customer.home.extra.goods;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.youth.banner.Banner;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.admin.adapter.ImageAdapter;
import cn.edu.neusoft.ypq.gowuu.base.BaseFragment;
import cn.edu.neusoft.ypq.gowuu.base.OnItemClickListener;
import cn.edu.neusoft.ypq.gowuu.base.ViewHolder;
import cn.edu.neusoft.ypq.gowuu.business.adapter.GoodsAdapter;
import cn.edu.neusoft.ypq.gowuu.business.bean.Goods;
import cn.edu.neusoft.ypq.gowuu.customer.home.adapter.EvaluationAdapter;
import cn.edu.neusoft.ypq.gowuu.customer.me.bean.Evaluation;
import cn.edu.neusoft.ypq.gowuu.utils.CheckUtils;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.FragmentUtils;
import cn.edu.neusoft.ypq.gowuu.utils.PostMessage;
import cz.msebera.android.httpclient.Header;

/**
 * 作者:颜培琦
 * 时间:2022/3/23
 * 功能:GoodsDetailFragment
 */
public class GoodsDetailFragment extends BaseFragment<Goods> {
    private Goods goods;

    @BindView(R.id.goods_detail_scroll)
    ScrollView scrollView;
    @BindView(R.id.goods_detail_banner)
    Banner banner;
    @BindView(R.id.goods_detail_tv_price)
    TextView tvPrice;
    @BindView(R.id.goods_detail_tv_name)
    TextView tvName;
    @BindView(R.id.goods_detail_tv_count)
    TextView tvCount;
    @BindView(R.id.goods_detail_tv_sale)
    TextView tvSale;
    @BindView(R.id.goods_detail_tv_evaluation_count)
    TextView tvEvaluationCount;
    @BindView(R.id.goods_detail_rv_evaluation)
    RecyclerView rvEvaluation;
    @BindView(R.id.goods_detail_rv_goods)
    RecyclerView rvGoods;

    public GoodsDetailFragment(Goods goods) {
        this.goods = goods;
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_goods_detail, null);
        ButterKnife.bind(this, view);

        page = 0;
        pageSize = 6;
        pageEnd = false;
        dataList = new ArrayList<>();
        adapter = new GoodsAdapter(mContext, dataList);
        GridLayoutManager manager = new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false);
        rvGoods.setLayoutManager(manager);
        rvGoods.setNestedScrollingEnabled(false);
        rvGoods.setAdapter(adapter);

        scrollView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollView.isAttachedToWindow() && scrollY != 0) {
                onScrollToEnd();
            }
        });

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        List<String> pathList = new ArrayList<>();
        for (String path : goods.getPathList()){
            pathList.add(Constants.RES_URL+path);
        }
        banner.setAdapter(new ImageAdapter(pathList));
        banner.setLoopTime(6000);
        tvPrice.setText(String.valueOf(CheckUtils.doubleTrim(goods.getPrice()*goods.getDiscount())));
        tvName.setText(goods.getName());
        tvCount.setText("库存数量:"+goods.getCount());
        tvSale.setText("已售:"+goods.getSale());
        setEvaluation();
        onScrollToEnd();
        setClickListener();
    }

    private void getGoodsPage(){
        String url = Constants.GOODS_URL+"/goods_classify";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("cid", goods.getCid3());
        params.put("page",page+=1);
        params.put("pageSize",pageSize);
        client.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody, StandardCharsets.UTF_8);
                Type type = new TypeToken<PostMessage<List<Goods>>>() {
                }.getType();
                PostMessage<List<Goods>> postMessage = new Gson().fromJson(response, type);
                if (postMessage.getMessage() == null){
                    dataList.clear();
                    if (postMessage.getData().size() < pageSize) pageEnd = true;
                    dataList = postMessage.getData();
                    adapter.addMoreData(dataList);
                } else {
                    Toast.makeText(mContext,postMessage.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(mContext,"GoodsDetailFragment(125):请求失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onScrollToEnd() {
        if (!pageEnd&&page<3){
            getGoodsPage();
        } else {
            Toast.makeText(mContext, "到底啦", Toast.LENGTH_SHORT).show();
        }
    }

    private void setEvaluation(){
        String url = Constants.EVALUATION_URL+"/get_evaluation";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("gid", goods.getGid());
        params.put("page", 1);
        params.put("pageSize", 3);
        client.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody, StandardCharsets.UTF_8);
                Type type = new TypeToken<PostMessage<List<Evaluation>>>() {
                }.getType();
                PostMessage<List<Evaluation>> postMessage = new Gson().fromJson(response, type);
                if (postMessage.getMessage() == null){
                    List<Evaluation> evaluationList = postMessage.getData();
                    tvEvaluationCount.setText("商品评价("+evaluationList.size()+")");
                    EvaluationAdapter evaluationAdapter = new EvaluationAdapter(mContext, evaluationList, true);
                    LinearLayoutManager manager = new LinearLayoutManager(mContext);
                    manager.setOrientation(LinearLayoutManager.VERTICAL);
                    rvEvaluation.setLayoutManager(manager);
                    rvEvaluation.setAdapter(evaluationAdapter);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(mContext,"GoodsDetailFragment(175):请求失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setClickListener(){
        adapter.setOnItemClickListener(new OnItemClickListener<Goods>() {
            @Override
            public void onItemClick(ViewHolder holder, Goods data, int position) {
                FragmentUtils.changeFragment(requireActivity(), R.id.main_frameLayout, new GoodsFrameFragment(data));
            }
        });
    }

    @OnClick(R.id.goods_detail_tv_all)
    public void allEvaluation(){
        EvaluationFragment.gid = goods.getGid();
        FragmentUtils.changeFragment(requireActivity(), R.id.main_frameLayout, new EvaluationFragment());
    }
}
