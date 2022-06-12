package cn.edu.neusoft.ypq.gowuu.customer.home.extra.goods;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
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
import cn.edu.neusoft.ypq.gowuu.base.BaseFragment;
import cn.edu.neusoft.ypq.gowuu.base.OnItemClickListener;
import cn.edu.neusoft.ypq.gowuu.base.ViewHolder;
import cn.edu.neusoft.ypq.gowuu.business.adapter.GoodsAdapter;
import cn.edu.neusoft.ypq.gowuu.business.bean.Business;
import cn.edu.neusoft.ypq.gowuu.business.bean.Goods;
import cn.edu.neusoft.ypq.gowuu.customer.classify.fragment.ClassifyGoodsFragment;
import cn.edu.neusoft.ypq.gowuu.utils.CheckUtils;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.FragmentUtils;
import cn.edu.neusoft.ypq.gowuu.utils.PostMessage;
import cn.edu.neusoft.ypq.gowuu.utils.RecyclerViewUtils;
import cz.msebera.android.httpclient.Header;

/**
 * 作者:颜培琦
 * 时间:2022/4/15
 * 功能:BznsStoreFragment
 */
public class BznsStoreFragment extends BaseFragment<Goods> {
    private Business business;
    private int position = 0;

    @BindView(R.id.cstm_type_tv_title)
    TextView tvTitle;
    @BindView(R.id.cstm_bzns_goods_tv_score)
    TextView tvScore;
    @BindView(R.id.cstm_bzns_goods_rv)
    RecyclerView recyclerView;
    @BindView(R.id.cstm_bzns_goods_spinner)
    Spinner spinner;

    public BznsStoreFragment(Business business){
        this.business = business;
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_cstm_goods_type, null);
        ButterKnife.bind(this,view);
        tvTitle.setText(business.getName());
        String score = CheckUtils.doubleToString((business.getScore1() + business.getScore2() + business.getScore3())/3);
        tvScore.setText("该商家综合评分:"+score);

        page = 0;
        pageSize = 6;
        dataList = new ArrayList<>();
        adapter = new GoodsAdapter(mContext, dataList);
        GridLayoutManager manager = new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!pageEnd && ((recyclerView.computeVerticalScrollOffset() > 0)
                        ||(RecyclerViewUtils.isVisBottom(recyclerView)))){
                    getGoodsPage();
                }
            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        setClickListener();
        getGoodsPage();
    }

    public void getGoodsPage(){
        String url = Constants.GOODS_URL+"/get_bzns_goods";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("bid", business.getBid());
        params.put("sort", position);
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
                Toast.makeText(mContext,"GoodsManageFragment(171):请求失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setClickListener(){
        adapter.setOnItemClickListener(new OnItemClickListener<Goods>() {
            @Override
            public void onItemClick(ViewHolder holder, Goods data, int position) {
                pageEnd = false;
                FragmentUtils.changeFragment(requireActivity(), R.id.main_frameLayout, new GoodsFrameFragment(data));
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (BznsStoreFragment.this.position != position) {
                    BznsStoreFragment.this.position = position;
                    adapter.reset();
                    page = 0;
                    pageEnd = false;
                    getGoodsPage();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @OnClick(R.id.cstm_type_goods_ib_back)
    public void back(){
        FragmentUtils.popBack(requireActivity());
    }
}
