package cn.edu.neusoft.ypq.gowuu.customer.me.extra.address;

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
import cn.edu.neusoft.ypq.gowuu.app.MainActivity;
import cn.edu.neusoft.ypq.gowuu.base.BaseFragment;
import cn.edu.neusoft.ypq.gowuu.customer.home.extra.order.OrderConfirmFragment;
import cn.edu.neusoft.ypq.gowuu.customer.me.adapter.AddressAdapter;
import cn.edu.neusoft.ypq.gowuu.customer.me.bean.Address;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.FragmentUtils;
import cn.edu.neusoft.ypq.gowuu.utils.PostMessage;
import cn.edu.neusoft.ypq.gowuu.utils.RecyclerViewUtils;
import cz.msebera.android.httpclient.Header;

/**
 * 作者:颜培琦
 * 时间:2022/3/8
 * 功能:AddressFragment
 */
public class AddressFragment extends BaseFragment<Address> {

    public static Address address  = new Address();
    public static boolean isModify = false;
    private AddressAdapter adapter;

    @BindView(R.id.me_address_rv)
    RecyclerView recyclerView;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_cstm_address, null);
        ButterKnife.bind(this, view);

        page = 0;
        pageSize = 4;
        pageEnd = false;
        dataList = new ArrayList<>();
        adapter = new AddressAdapter(mContext, dataList);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        setClickListener();
        getAddressPage();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!pageEnd && ((recyclerView.computeVerticalScrollOffset() > 0)
                        ||(RecyclerViewUtils.isVisBottom(recyclerView)))){
                    getAddressPage();
                }
            }
        });
        return view;
    }

    @OnClick(R.id.me_address_bt_add)
    public void addAddress(){
        isModify = false;
        AddressEditFragment editFragment = new AddressEditFragment();
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frameLayout, editFragment)
                .addToBackStack(null)
                .commit();
    }

    private void getAddressPage(){
        String url = Constants.ADDRESS_URL+"/get_address";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("uid", MainActivity.user.getUid());
        params.put("page", page+=1);
        params.put("pageSize", pageSize);
        client.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody, StandardCharsets.UTF_8);
                Type type = new TypeToken<PostMessage<List<Address>>>() {
                }.getType();
                PostMessage<List<Address>> postMessage = new Gson().fromJson(response, type);
                if (postMessage.getMessage() == null){
                    dataList.clear();
                    if (postMessage.getData().size() < pageSize) pageEnd = true;
                    dataList = postMessage.getData();
                    adapter.addMoreData(dataList);
                } else {
                    Toast.makeText(mContext, postMessage.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(mContext,"AddressFragment(117):请求失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setClickListener(){
        adapter.setOnItemClickListener((holder, data, position) -> {
            address.setAddress(data);
            isModify = true;
            FragmentUtils.changeFragment(requireActivity(), R.id.main_frameLayout, new AddressEditFragment());
        });

        adapter.select((viewHolder, address, position) -> {
            if (OrderConfirmFragment.selectAddress) {
                OrderConfirmFragment.selectAddress = false;
                OrderConfirmFragment.address = address;
                FragmentUtils.popBack(requireActivity());
            }
        });
    }

    @OnClick(R.id.me_address_ib_back)
    public void back(){
        FragmentUtils.popBack(requireActivity());
    }
}
