package cn.edu.neusoft.ypq.gowuu.customer.home.extra.search;

import android.util.Log;
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
import cn.edu.neusoft.ypq.gowuu.business.adapter.GoodsAdapter;
import cn.edu.neusoft.ypq.gowuu.business.bean.Goods;
import cn.edu.neusoft.ypq.gowuu.customer.home.extra.goods.GoodsFrameFragment;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.FragmentUtils;
import cn.edu.neusoft.ypq.gowuu.utils.PostMessage;
import cn.edu.neusoft.ypq.gowuu.utils.RecyclerViewUtils;
import cz.msebera.android.httpclient.Header;

/**
 * 作者:颜培琦
 * 时间:2022/3/26
 * 功能:SearchResultFragment
 */
public class SearchResultFragment extends BaseFragment<Goods> {

    public static String searchName;
    private int position = 0;
    private GoodsAdapter adapter;

    @BindView(R.id.search_result_rv)
    RecyclerView recyclerView;
    @BindView(R.id.search_result_tv_title)
    TextView tvTitle;
    @BindView(R.id.search_spinner)
    Spinner spinner;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_cstm_search_result, null);
        ButterKnife.bind(this, view);
        Log.e("TAG", "SearchResultFragment的UI被初始化了");

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
                    getSearchPage();
                }
            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        //商品数据初始化
        setClickListener();
        if (searchName != null) {
            getSearchPage();
        }
        tvTitle.setHint(searchName);
    }

    public void getSearchPage(){
        String url = Constants.GOODS_URL+"/search";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("name", searchName);
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
                Toast.makeText(mContext,"SearchResultFragment(115):请求失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setClickListener(){
        adapter.setOnItemClickListener((holder, data, position) -> FragmentUtils.changeFragment(requireActivity(), R.id.main_frameLayout, new GoodsFrameFragment(data)));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (SearchResultFragment.this.position != position) {
                    SearchResultFragment.this.position = position;
                    adapter.reset();
                    page = 0;
                    pageEnd = false;
                    getSearchPage();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @OnClick(R.id.search_result_ib_back)
    public void back(){
        searchName = null;
        FragmentUtils.removeFragment(requireActivity(), this);
        FragmentUtils.popBack(requireActivity());
    }

    @OnClick(R.id.search_result_tv_title)
    public void search(){
        searchName = null;
        SearchFragment.searchString = tvTitle.getHint().toString();
        FragmentUtils.removeFragment(requireActivity(), this);
        FragmentUtils.changeRbFragment(requireActivity(), R.id.main_frameLayout, new SearchFragment());
    }
}
