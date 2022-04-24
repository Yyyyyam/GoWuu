package cn.edu.neusoft.ypq.gowuu.customer.home.extra.goods;

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
import butterknife.OnClick;
import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.base.BaseFragment;
import cn.edu.neusoft.ypq.gowuu.business.adapter.BusinessOrderAdapter;
import cn.edu.neusoft.ypq.gowuu.customer.home.adapter.EvaluationAdapter;
import cn.edu.neusoft.ypq.gowuu.customer.me.bean.Evaluation;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.FragmentUtils;
import cn.edu.neusoft.ypq.gowuu.utils.PostMessage;
import cn.edu.neusoft.ypq.gowuu.utils.RecyclerViewUtils;
import cz.msebera.android.httpclient.Header;

/**
 * 作者:颜培琦
 * 时间:2022/4/4
 * 功能:EvaluationFragment
 */
public class EvaluationFragment extends BaseFragment<Evaluation> {
    public static Integer gid;

    @BindView(R.id.evaluation_rv)
    RecyclerView recyclerView;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_evaluation, null);
        ButterKnife.bind(this, view);

        page = 0;
        pageSize = 4;
        pageEnd = false;
        dataList = new ArrayList<>();
        adapter = new EvaluationAdapter(mContext, dataList, false);
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
        getOrderPage();
    }

    private void getOrderPage(){
        String url = Constants.EVALUATION_URL+"/get_evaluation";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("gid", gid);
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
                    if (postMessage.getData().size() < pageSize) pageEnd = true;
                    dataList = postMessage.getData();
                    adapter.addMoreData(dataList);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(mContext,"GoodsDetailFragment(175):请求失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.evaluation_ib_back)
    public void back(){
        FragmentUtils.popBack(getActivity());
    }
}
