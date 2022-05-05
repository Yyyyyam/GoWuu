package cn.edu.neusoft.ypq.gowuu.customer.home.fragment;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import cn.edu.neusoft.ypq.gowuu.business.bean.Goods;
import cn.edu.neusoft.ypq.gowuu.business.adapter.GoodsAdapter;
import cn.edu.neusoft.ypq.gowuu.customer.home.bean.HomeData;
import cn.edu.neusoft.ypq.gowuu.customer.home.extra.discount.DiscountFragment;
import cn.edu.neusoft.ypq.gowuu.customer.home.extra.goods.GoodsFrameFragment;
import cn.edu.neusoft.ypq.gowuu.customer.home.extra.search.SearchFragment;
import cn.edu.neusoft.ypq.gowuu.customer.home.extra.goods.GoodsDetailFragment;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.FragmentUtils;
import cn.edu.neusoft.ypq.gowuu.utils.GlideUtils;
import cn.edu.neusoft.ypq.gowuu.utils.PostMessage;
import cn.edu.neusoft.ypq.gowuu.utils.RecyclerViewUtils;
import cz.msebera.android.httpclient.Header;

/**
 * 作者:颜培琦
 * 时间:2022/1/28
 * 功能:主页面Fragment的实现，主要有以下几个功能
 * 1.
 * 2.
 * 3.
 * 4.
 */

public class HomeFragment extends BaseFragment<Goods> {
    private GoodsAdapter discountAdapter;
    private List<Goods> discountGoods;

    @BindView(R.id.tv_search_home)
    TextView tvSearchHome;
    @BindView(R.id.rv_home)
    RecyclerView rvHome;
    @BindView(R.id.ib_top)
    ImageButton ibTop;
    @BindView(R.id.banner_home)
    Banner banner;
    @BindView(R.id.rv_home_discount)
    RecyclerView rvDiscount;

    @Override
    public View initView() {
        view = View.inflate(mContext, R.layout.fragment_cstm_home, null);
        Log.e("TAG", "主页面Fragment的UI被初始化了");
        ButterKnife.bind(this, view);
        ibTop.setVisibility(View.VISIBLE);

        page = 0;
        pageSize = 6;

        dataList = new ArrayList<>();
        adapter = new GoodsAdapter(mContext, dataList);
        GridLayoutManager manager = new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false);
        rvHome.setLayoutManager(manager);
        rvHome.setAdapter(adapter);

        rvHome.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!pageEnd && ((recyclerView.computeVerticalScrollOffset() > 0)
                        ||(RecyclerViewUtils.isVisBottom(recyclerView)))){
                    getGoodsPage();
                }
            }
        });

        discountGoods = new ArrayList<>();
        discountAdapter = new GoodsAdapter(mContext, discountGoods);
        LinearLayoutManager discountManager = new LinearLayoutManager(mContext);
        discountManager.setOrientation(RecyclerView.HORIZONTAL);
        rvDiscount.setLayoutManager(discountManager);
        rvDiscount.setAdapter(discountAdapter);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        Log.e("TAG", "主页面Fragment的数据被初始化了");
        //商品数据初始化
        setClickListener();
        getGoodsPage();
        getHomeDate();
        SearchFragment.hideKeyBoard(getActivity());
    }

    @OnClick({R.id.tv_search_home, R.id.ib_top})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_search_home:
                FragmentUtils.changeFragment(getActivity(), R.id.main_frameLayout, new SearchFragment());
                break;
            case R.id.ib_top:
                rvHome.setVerticalScrollbarPosition(0);
                ibTop.setVisibility(View.GONE);
                break;
        }
    }

    private void getHomeDate() {
        String url = Constants.GOODS_URL + "/get_home_data";
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody, StandardCharsets.UTF_8);
                Type type = new TypeToken<PostMessage<HomeData>>() {}.getType();
                PostMessage<HomeData> postMessage = new Gson().fromJson(response, type);
                if (postMessage.getMessage() == null){
                    List<String> pathList = new ArrayList<>();
                    for (String path : postMessage.getData().getBannerList()){
                        pathList.add(Constants.RES_URL+path);
                    }
                    ImageAdapter imageAdapter = new ImageAdapter(pathList, true);
                    banner.setAdapter(imageAdapter);
                    banner.setLoopTime(5000);
                    imageAdapter.onClick(new ImageAdapter.OnClickListener() {
                        @Override
                        public void onClick(String data, int position) {
                            Toast.makeText(mContext, "点击位置为第"+position+"张", Toast.LENGTH_SHORT).show();
                        }
                    });
                    discountAdapter.addMoreData(postMessage.getData().getDiscountGoods());
                } else {
                    Toast.makeText(mContext,postMessage.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(mContext,"访问失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getGoodsPage(){
        String url = Constants.GOODS_URL+"/get_all_goods";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
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
                Toast.makeText(mContext,"HomeFragment(136):请求失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setClickListener(){
        adapter.setOnItemClickListener(new OnItemClickListener<Goods>() {
            @Override
            public void onItemClick(ViewHolder holder, Goods data, int position) {
                FragmentUtils.changeFragment(getActivity(), R.id.main_frameLayout, new GoodsFrameFragment(data));
            }
        });

        discountAdapter.setOnItemClickListener(new OnItemClickListener<Goods>() {
            @Override
            public void onItemClick(ViewHolder holder, Goods data, int position) {
                FragmentUtils.changeFragment(getActivity(), R.id.main_frameLayout, new GoodsFrameFragment(data));
            }
        });
    }

    @OnClick(R.id.tv_home_discount_all)
    public void discountAll() {
        FragmentUtils.changeFragment(getActivity(), R.id.main_frameLayout, new DiscountFragment());
    }
}