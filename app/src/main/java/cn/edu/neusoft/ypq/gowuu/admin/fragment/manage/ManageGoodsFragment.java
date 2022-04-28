package cn.edu.neusoft.ypq.gowuu.admin.fragment.manage;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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
import cn.edu.neusoft.ypq.gowuu.business.bean.Goods;
import cn.edu.neusoft.ypq.gowuu.business.fragment.goods.BusinessGoodsFragment;
import cn.edu.neusoft.ypq.gowuu.business.fragment.goods.GoodsEditFragment;
import cn.edu.neusoft.ypq.gowuu.customer.home.extra.search.SearchFragment;
import cn.edu.neusoft.ypq.gowuu.customer.me.adapter.MessageAdapter;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.FragmentUtils;
import cn.edu.neusoft.ypq.gowuu.utils.PostMessage;
import cn.edu.neusoft.ypq.gowuu.utils.RecyclerViewUtils;
import cz.msebera.android.httpclient.Header;

/**
 * 作者:颜培琦
 * 时间:2022/3/10
 * 功能:ManageGoodsFragment
 */
public class ManageGoodsFragment extends BaseFragment<Goods> {
    String name;

    @BindView(R.id.admin_manage_rv)
    RecyclerView recyclerView;
    @BindView(R.id.admin_manage_et_name)
    EditText etName;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_admin_manage, null);
        ButterKnife.bind(this, view);

        page = 0;
        pageSize = 6;
        pageEnd = false;
        dataList = new ArrayList<>();
        adapter = new GoodsAdapter(mContext, dataList);
        GridLayoutManager manager = new GridLayoutManager(mContext,2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!pageEnd && ((recyclerView.computeVerticalScrollOffset() > 0)
                        ||(RecyclerViewUtils.isVisBottom(recyclerView)))){
                    getGoodsPage(name);
                }
            }
        });

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        getGoodsPage(name);
        initListener();
    }

    @OnClick(R.id.admin_manage_bt_search)
    public void search() {
        name = etName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(mContext, "请输入搜索内容", Toast.LENGTH_SHORT).show();
        } else {
            pageEnd = false;
            page = 0;
            adapter.reset();
            SearchFragment.hideKeyBoard(getActivity());
            getGoodsPage(name);
        }
    }

    private void getGoodsPage(String name) {
        if (name != null) {
            String url = Constants.GOODS_URL+"/search";
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("name", name);
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
    }

    private void initListener() {
        etName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                // 按下按钮，这里和xml文件中的EditText中属性imeOptions对应;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    return false;
                }
                return true;//返回true，保留软键盘;false，隐藏软键盘
            }
        });

        adapter.setOnItemClickListener(new OnItemClickListener<Goods>() {
            @Override
            public void onItemClick(ViewHolder holder, Goods data, int position) {
                FragmentUtils.changeFragment(getActivity(), R.id.main_frameLayout, new GoodsEditFragment(data, true));
            }
        });
    }
}
