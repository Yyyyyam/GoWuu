package cn.edu.neusoft.ypq.gowuu.customer.classify.fragment;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.base.BaseFragment;
import cn.edu.neusoft.ypq.gowuu.customer.classify.adapter.ChildAdapter;
import cn.edu.neusoft.ypq.gowuu.customer.classify.adapter.ParentAdapter;
import cn.edu.neusoft.ypq.gowuu.customer.classify.bean.GoodsClassify;
import cn.edu.neusoft.ypq.gowuu.customer.classify.bean.GoodsClassifyItem;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.FragmentUtils;
import cn.edu.neusoft.ypq.gowuu.utils.PostMessage;
import cz.msebera.android.httpclient.Header;

/**
 * 作者:颜培琦
 * 时间:2022/1/28
 * 功能:分类Fragment的实现，主要有以下几个功能
 *      1.
 *      2.
 *      3.
 *      4.
 */

public class ClassifyFragment extends BaseFragment<Void> {

    ParentAdapter parentAdapter = new ParentAdapter(mContext, null);
    ChildAdapter childAdapter = new ChildAdapter(mContext, null);

    @BindView(R.id.cstm_clsfy_rv1)
    RecyclerView rvParent;
    @BindView(R.id.cstm_clsfy_rv2)
    RecyclerView rvChild;

    @Override
    public View initView() {
        Log.e(TAG, "分类Fragment的UI被初始化了");
        View view = View.inflate(mContext, R.layout.fragment_cstm_classify, null);
        ButterKnife.bind(this, view);

        parentAdapter = new ParentAdapter(mContext, null);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvParent.setLayoutManager(manager);
        rvParent.setAdapter(parentAdapter);

        childAdapter = new ChildAdapter(mContext, null);
        LinearLayoutManager childManager = new LinearLayoutManager(mContext);
        childManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvChild.setLayoutManager(childManager);
        rvChild.setAdapter(childAdapter);

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        Log.e(TAG, "分类Fragment的数据被初始化了");
        getParent();
        getChild(1);
        setOnListener();
    }

    public void getParent() {
        String url = Constants.GOODS_CATEGORY_URL+"/get_classify_parent";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody, StandardCharsets.UTF_8);
                Type type = new TypeToken<PostMessage<List<GoodsClassifyItem>>>() {
                }.getType();
                PostMessage<List<GoodsClassifyItem>> postMessage = new Gson().fromJson(response, type);
                if (postMessage.getMessage() == null){
                    parentAdapter.addMoreData(postMessage.getData());
                } else {
                    Toast.makeText(mContext,postMessage.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(mContext,"ClassifyFragment(getParent):请求失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getChild(Integer parentId) {
        String url = Constants.GOODS_CATEGORY_URL+"/get_classify";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("parentId", parentId);
        client.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody, StandardCharsets.UTF_8);
                Type type = new TypeToken<PostMessage<List<GoodsClassify>>>() {
                }.getType();
                PostMessage<List<GoodsClassify>> postMessage = new Gson().fromJson(response, type);
                if (postMessage.getMessage() == null){
                    childAdapter.reset();
                    childAdapter.addMoreData(postMessage.getData());
                } else {
                    Toast.makeText(mContext,postMessage.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(mContext,"ClassifyFragment(getParent):请求失败",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setOnListener() {
        parentAdapter.setOnSelectListener((holder, data, position) -> getChild(data.getId()));

        childAdapter.setOnSelectListener((holder, data, position) ->
                FragmentUtils.changeFragment(requireActivity(),
                        R.id.main_frameLayout, new ClassifyGoodsFragment(data.getId(), data.getName())));
    }
}