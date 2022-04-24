package cn.edu.neusoft.ypq.gowuu.admin.fragment.examine;

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
import cn.edu.neusoft.ypq.gowuu.admin.adapter.ExamineAdapter;
import cn.edu.neusoft.ypq.gowuu.admin.bean.Examine;
import cn.edu.neusoft.ypq.gowuu.base.BaseFragment;
import cn.edu.neusoft.ypq.gowuu.base.OnItemClickListener;
import cn.edu.neusoft.ypq.gowuu.base.ViewHolder;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.PostMessage;
import cn.edu.neusoft.ypq.gowuu.utils.RecyclerViewUtils;
import cz.msebera.android.httpclient.Header;

/**
 * 作者:颜培琦
 * 时间:2022/3/10
 * 功能:ExamineApplyFragment
 */
public class ExamineApplyFragment extends BaseFragment<Examine> {

    @BindView(R.id.fragment_recycler_view)
    RecyclerView recyclerView;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_for_rv, null);
        ButterKnife.bind(this, view);

        page = 0;
        pageSize = 4;
        dataList = new ArrayList<>();
        adapter = new ExamineAdapter(mContext, dataList);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setBackgroundResource(R.drawable.item_shape_half_fillet_top);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        setClickListener();
        getExaminePage();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!pageEnd && ((recyclerView.computeVerticalScrollOffset() > 0)
                        ||(RecyclerViewUtils.isVisBottom(recyclerView)))){
                    getExaminePage();
                }
            }
        });
        return view;
    }

    public void getExaminePage(){
        String url = Constants.ADMIN_URL+"/examine/get_examine_page";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("type", 0);
        params.put("page", page+=1);
        params.put("pageSize", pageSize);
        client.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody, StandardCharsets.UTF_8);
                Type type = new TypeToken<PostMessage<List<Examine>>>() {
                }.getType();
                PostMessage<List<Examine>> postMessage = new Gson().fromJson(response, type);
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
                Toast.makeText(mContext,"ExamineApplyFragment(99):请求失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //点击事件的处理
    public void setClickListener(){
        adapter.setOnItemClickListener(new OnItemClickListener<Examine>() {
            @Override
            public void onItemClick(ViewHolder holder, Examine data, int position) {
                String stateUrl = Constants.ADMIN_URL+"/examine/response";
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("uid", data.getUid());
                params.put("rid", data.getRid());
                params.put("state", data.getState());
                params.put("type", data.getType());
                if (data.getState() == 1){
                    params.put("detail", "申请成功");
                } else {
                    params.put("detail", "申请失败，请详细填写资料");
                }
                client.post(stateUrl, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String response = new String(responseBody, StandardCharsets.UTF_8);
                        //修正泛型无法正常解析问题
                        Type type = new TypeToken<PostMessage<Void>>() {
                        }.getType();
                        PostMessage<Void> postMessage = new Gson().fromJson(response, type);
                        if (postMessage.getMessage()==null){
                            Toast.makeText(mContext,"操作成功",Toast.LENGTH_SHORT).show();
                            adapter.notifyItemChanged(position);
                        }else {
                            Toast.makeText(mContext, postMessage.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(mContext,"ExamineApplyFragment(138):请求失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
