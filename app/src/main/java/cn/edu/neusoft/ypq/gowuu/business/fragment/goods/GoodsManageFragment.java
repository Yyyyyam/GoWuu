package cn.edu.neusoft.ypq.gowuu.business.fragment.goods;

import android.view.View;
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
import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.base.BaseFragment;
import cn.edu.neusoft.ypq.gowuu.base.OnItemClickListener;
import cn.edu.neusoft.ypq.gowuu.base.ViewHolder;
import cn.edu.neusoft.ypq.gowuu.business.bean.Goods;
import cn.edu.neusoft.ypq.gowuu.business.fragment.BusinessFragment;
import cn.edu.neusoft.ypq.gowuu.business.adapter.GoodsAdapter;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.FragmentUtils;
import cn.edu.neusoft.ypq.gowuu.utils.PostMessage;
import cn.edu.neusoft.ypq.gowuu.utils.RecyclerViewUtils;
import cz.msebera.android.httpclient.Header;

/**
 * 作者:颜培琦
 * 时间:2022/3/10
 * 功能:GoodsManageFragment
 */
public class GoodsManageFragment  extends BaseFragment<Goods> {
    @BindView(R.id.fragment_recycler_view)
    RecyclerView recyclerView;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_for_rv, null);
        ButterKnife.bind(this, view);
        page = 0;
        pageSize = 6;
        dataList = new ArrayList<>();
        adapter = new GoodsAdapter(mContext, dataList);
        GridLayoutManager manager = new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        setClickListener();
        getGoodsPage();

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

    public void getGoodsPage(){
        String url = Constants.GOODS_URL+"/get_bzns_goods";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("bid", BusinessFragment.business.getBid());
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
                BusinessGoodsFragment.isEdit = true;
                GoodsEditFragment.goods.setGoods(data);
                FragmentUtils.changeFragment(getActivity(), R.id.main_frameLayout, new GoodsEditFragment());
            }
        });
    }
}