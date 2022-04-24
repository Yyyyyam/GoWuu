package cn.edu.neusoft.ypq.gowuu.customer.home.extra.favorite;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import cn.edu.neusoft.ypq.gowuu.app.MainActivity;
import cn.edu.neusoft.ypq.gowuu.base.BaseFragment;
import cn.edu.neusoft.ypq.gowuu.base.OnItemClickListener;
import cn.edu.neusoft.ypq.gowuu.base.ViewHolder;
import cn.edu.neusoft.ypq.gowuu.customer.home.adapter.FavoriteAdapter;
import cn.edu.neusoft.ypq.gowuu.customer.home.bean.Favorite;
import cn.edu.neusoft.ypq.gowuu.customer.home.extra.goods.GoodsDetailFragment;
import cn.edu.neusoft.ypq.gowuu.customer.home.extra.goods.GoodsFrameFragment;
import cn.edu.neusoft.ypq.gowuu.customer.me.adapter.AddressAdapter;
import cn.edu.neusoft.ypq.gowuu.customer.me.bean.Address;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.FragmentUtils;
import cn.edu.neusoft.ypq.gowuu.utils.PostMessage;
import cn.edu.neusoft.ypq.gowuu.utils.RecyclerViewUtils;
import cz.msebera.android.httpclient.Header;

/**
 * 作者:颜培琦
 * 时间:2022/3/26
 * 功能:FavoriteFragment
 */
public class FavoriteFragment extends BaseFragment<Favorite> {

    private boolean edit = false;
    private FavoriteAdapter adapter;

    @BindView(R.id.cstm_favorite_rv)
    RecyclerView recyclerView;
    @BindView(R.id.cstm_favorite_bottom_bar)
    ConstraintLayout bottomBar;
    @BindView(R.id.cstm_favorite_tv_edit)
    TextView tvEdit;
    @BindView(R.id.cstm_favorite_cb_select_all)
    CheckBox cbSelectAll;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_cstm_favorite, null);
        ButterKnife.bind(this, view);

        dataList = new ArrayList<>();
        adapter = new FavoriteAdapter(mContext, dataList);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        setClickListener();
        getFavoritePage();
        return view;
    }

    private void getFavoritePage(){
        String url = Constants.USR_URL+"/get_favorite";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("uid", MainActivity.user.getUid());
        client.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody, StandardCharsets.UTF_8);
                Type type = new TypeToken<PostMessage<List<Favorite>>>() {
                }.getType();
                PostMessage<List<Favorite>> postMessage = new Gson().fromJson(response, type);
                if (postMessage.getMessage() == null){
                    dataList.clear();
                    dataList = postMessage.getData();
                    adapter.addMoreData(dataList);
                    if (cbSelectAll.isChecked()) updateSelect(cbSelectAll.isChecked());
                } else {
                    Toast.makeText(mContext, postMessage.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(mContext,"FavoriteFragment(116):请求失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setClickListener(){
        adapter.setOnItemClickListener(new OnItemClickListener<Favorite>() {
            @Override
            public void onItemClick(ViewHolder holder, Favorite data, int position) {
                if (adapter.getEdit()){
                    data.setIsChecked(!data.getIsChecked());
                    holder.setChecked(R.id.itm_favorite_goods_cb, data.getIsChecked());
                } else {
                    FragmentUtils.changeFragment(getActivity(), R.id.main_frameLayout, new GoodsFrameFragment(data.getGoods()));
                }
            }
        });

        adapter.setOnSelectListener(new FavoriteAdapter.OnSelectListener() {
            @Override
            public void setOnSelectListener(boolean isChecked, Favorite favorite, int position) {
                List<Favorite> dataList = adapter.getDataList();
                dataList.get(position).setChecked(isChecked);
                int count = 0;
                for (int i=0; i<dataList.size(); i++){
                    if (dataList.get(i).getChecked()) count+=1;
                }
                cbSelectAll.setChecked(count == dataList.size());
            }
        });

        cbSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSelect(cbSelectAll.isChecked());
            }
        });
    }

    @OnClick(R.id.cstm_favorite_ib_back)
    public void back(){
        FragmentUtils.popBack(getActivity());
    }

    private void updateSelect(boolean isSelect){
        List<Favorite> dataList = adapter.getDataList();
        for (int i=0; i< dataList.size(); i++){
            dataList.get(i).setChecked(isSelect);
        }
        adapter.notifyItemRangeChanged(0, adapter.getItemCount());
    }

    @OnClick(R.id.cstm_favorite_bt_delete)
    public void delete(){
        String url = Constants.GOODS_URL+"/remove_favorite_list";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        List<Integer> fids = new ArrayList<>();
        List<Favorite> favorites = new ArrayList<>();
        for (int i=0; i< adapter.getDataList().size(); i++){
            Favorite favorite = adapter.getDataList().get(i);
            if (favorite.getIsChecked()){
                favorites.add(favorite);
                fids.add(favorite.getFid());
            }
        }
        params.put("fidJson", new Gson().toJson(fids));
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody, StandardCharsets.UTF_8);
                Type type = new TypeToken<PostMessage<Void>>() {
                }.getType();
                PostMessage<Void> postMessage = new Gson().fromJson(response, type);
                if (postMessage.getMessage()==null){
                    if (cbSelectAll.isChecked()) {
                        adapter.reset();
                    } else {
                        int times = 0;
                        for (Favorite f : favorites){
                            adapter.remove(f.getPosition()-times++);
                        }
                    }

                    edit();
                }else {
                    Toast.makeText(mContext, postMessage.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(mContext, "FavoriteFragment(186):请求失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.cstm_favorite_tv_edit)
    public void edit(){
        if (edit) {
            edit = false;
            tvEdit.setText("管理");
            bottomBar.setVisibility(View.GONE);
            updateSelect(false);
            cbSelectAll.setChecked(false);
        } else {
            edit = true;
            tvEdit.setText("完成");
            bottomBar.setVisibility(View.VISIBLE);
        }
        adapter.setEdit(edit);
        adapter.notifyItemRangeChanged(0, adapter.getItemCount());
    }
}
