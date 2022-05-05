package cn.edu.neusoft.ypq.gowuu.business.fragment.goods;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.base.BaseFragment;
import cn.edu.neusoft.ypq.gowuu.business.bean.Goods;
import cn.edu.neusoft.ypq.gowuu.business.bean.GoodsCategory;
import cn.edu.neusoft.ypq.gowuu.business.fragment.BusinessFragment;
import cn.edu.neusoft.ypq.gowuu.customer.me.adapter.RequestImgAdapter;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.FileUtils;
import cn.edu.neusoft.ypq.gowuu.utils.FragmentUtils;
import cn.edu.neusoft.ypq.gowuu.utils.PostMessage;
import cz.msebera.android.httpclient.Header;

/**
 * 作者:颜培琦
 * 时间:2022/3/13
 * 功能:GoodsEditFragment
 */
public class GoodsEditFragment extends BaseFragment<Uri> {
    private Goods goods;
    private boolean isEdit;
    String[] category = new String[3];

    private String url = Constants.GOODS_CATEGORY_URL;
    private String c1 = "";
    private String c2 = "";
    private String c3 = "";
    private List<GoodsCategory> typeList1 = new ArrayList<>();
    private List<GoodsCategory> typeList2 = new ArrayList<>();
    private List<GoodsCategory> typeList3 = new ArrayList<>();
    private List<String> sList1 = new ArrayList<>();
    private List<String> sList2 = new ArrayList<>();
    private List<String> sList3 = new ArrayList<>();
    private ArrayAdapter<String> ctgrAdapter;

    @BindView(R.id.bzns_goods_add_rv)
    RecyclerView recyclerView;
    @BindView(R.id.bzns_goods_add_sp1)
    Spinner spType1;
    @BindView(R.id.bzns_goods_add_sp2)
    Spinner spType2;
    @BindView(R.id.bzns_goods_add_sp3)
    Spinner spType3;
    @BindView(R.id.bzns_goods_add_switch_state)
    SwitchCompat swState;
    @BindView(R.id.bzns_goods_add_et_name)
    EditText etName;
    @BindView(R.id.bzns_goods_add_et_count)
    EditText etCount;
    @BindView(R.id.bzns_goods_add_et_price)
    EditText etPrice;
    @BindView(R.id.bzns_goods_add_tv_title)
    TextView tvTitle;
    @BindView(R.id.bzns_goods_add_tv8)
    TextView tvTag;
    @BindView(R.id.bzns_goods_add_et_discount)
    EditText etDiscount;
    @BindView(R.id.bzns_goods_add_ib_delete)
    ImageButton ibDelete;

    public GoodsEditFragment(Goods goods, boolean isEdit) {
        this.goods = goods;
        this.isEdit = isEdit;
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_bzns_goods_add, null);
        ButterKnife.bind(this, view);
        initCategory();
        dataList = new ArrayList<>();
        //通过Adapter加载Uri到recyclerView
        adapter = new RequestImgAdapter(mContext, dataList);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext,3));
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        if (isEdit) {
            tvTitle.setText("编辑商品");
            etName.setText(goods.getName());
            etPrice.setText(String.valueOf(goods.getPrice()));
            etCount.setText(String.valueOf(goods.getCount()));
            if (goods.getState() == 0) swState.setChecked(false);
            else swState.setChecked(true);
            tvTag.setVisibility(View.VISIBLE);
            etDiscount.setVisibility(View.VISIBLE);
            ibDelete.setVisibility(View.VISIBLE);
        }
    }

    //网络请求分类信息
    private void initCategory(){
        if (isEdit) category = goods.getCategory().split("-");
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("parentId", 0);
        client.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody, StandardCharsets.UTF_8);
                Type type = new TypeToken<PostMessage<List<GoodsCategory>>>() {}.getType();
                PostMessage<List<GoodsCategory>> postMessage = new Gson().fromJson(response, type);
                if (postMessage.getMessage() == null){
                    typeList1 = postMessage.getData();
                    sList1.clear();
                    for (GoodsCategory g : typeList1) {
                        sList1.add(g.getName());
                    }
                    ctgrAdapter = new ArrayAdapter<>(mContext , android.R.layout.simple_spinner_item, sList1);
                    ctgrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spType1.setAdapter(ctgrAdapter);
                    if (isEdit){
                        spType1.setSelection(sList1.indexOf(category[0]));
                    }
                    spType1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            goods.setCid1(typeList1.get(position).getId());
                            c1 = typeList1.get(position).getName();
                            AsyncHttpClient client = new AsyncHttpClient();
                            RequestParams params = new RequestParams();
                            params.put("parentId", typeList1.get(position).getId());
                            client.get(url, params, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    String response = new String(responseBody, StandardCharsets.UTF_8);
                                    Type type = new TypeToken<PostMessage<List<GoodsCategory>>>() {}.getType();
                                    PostMessage<List<GoodsCategory>> postMessage = new Gson().fromJson(response, type);
                                    if (postMessage.getMessage() == null){
                                        typeList2 = postMessage.getData();
                                        sList2.clear();
                                        for (GoodsCategory g : typeList2) {
                                            sList2.add(g.getName());
                                        }
                                        ctgrAdapter = new ArrayAdapter<>(mContext , android.R.layout.simple_spinner_item, sList2);
                                        ctgrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spType2.setAdapter(ctgrAdapter);
                                        if (isEdit){
                                            spType2.setSelection(sList2.indexOf(category[1]));
                                        }
                                        spType2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                goods.setCid2(typeList2.get(position).getId());
                                                c2 = "-"+typeList2.get(position).getName();
                                                AsyncHttpClient client = new AsyncHttpClient();
                                                RequestParams params = new RequestParams();
                                                params.put("parentId", typeList2.get(position).getId());
                                                client.get(url, params, new AsyncHttpResponseHandler() {
                                                    @Override
                                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                        String response = new String(responseBody, StandardCharsets.UTF_8);
                                                        Type type = new TypeToken<PostMessage<List<GoodsCategory>>>() {}.getType();
                                                        PostMessage<List<GoodsCategory>> postMessage = new Gson().fromJson(response, type);
                                                        if (postMessage.getMessage() == null){
                                                            typeList3 = postMessage.getData();
                                                            sList3.clear();
                                                            for (GoodsCategory g : typeList3) {
                                                                sList3.add(g.getName());
                                                            }
                                                            ctgrAdapter = new ArrayAdapter<>(mContext , android.R.layout.simple_spinner_item, sList3);
                                                            ctgrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                            spType3.setAdapter(ctgrAdapter);
                                                            if (isEdit){
                                                                spType3.setSelection(sList3.indexOf(category[0]));
                                                            }
                                                            spType3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                @Override
                                                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                                    goods.setCid3(typeList3.get(position).getId());
                                                                    c3 = "-"+typeList3.get(position).getName();
                                                                    goods.setCategory(c1+c2+c3);
                                                                }
                                                                @Override
                                                                public void onNothingSelected(AdapterView<?> parent) {}
                                                            });
                                                        } else {
                                                            Toast.makeText(mContext, postMessage.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                    @Override
                                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                                        Toast.makeText(mContext,"GoodsEditFragment(171):请求失败",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {}
                                        });
                                    } else {
                                        Toast.makeText(mContext,postMessage.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                    Toast.makeText(mContext,"GoodsEditFragment(184):请求失败",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });
                } else {
                    Toast.makeText(mContext,postMessage.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(mContext,"GoodsEditFragment(197):请求失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.bzns_goods_add_ib_add)
    public void addImg(){
        if (dataList.size() < 5){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        } else {
            Toast.makeText(mContext,"最多添加5张照片",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    //根据uri添加图片到UriList
                    adapter.insert(uri);
                }
        }
    }

    @OnClick(R.id.bzns_goods_add_bt_commit)
    public void commit(){
        String url;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        if (etName.getText().toString().trim().isEmpty()
                ||etCount.getText().toString().trim().isEmpty()
                ||etPrice.getText().toString().trim().isEmpty()){
            Toast.makeText(mContext,"请完整填写内容",Toast.LENGTH_SHORT).show();
        } else if (isEdit && etDiscount.getText().toString().isEmpty()) {
            Toast.makeText(mContext,"请设置折扣",Toast.LENGTH_SHORT).show();
        } else if (isEdit&&(Double.parseDouble(etDiscount.getText().toString().trim())>1||Double.parseDouble(etDiscount.getText().toString().trim())<0)) {
            Toast.makeText(mContext,"折扣在0-1之间",Toast.LENGTH_SHORT).show();
        } else if (dataList.size() == 0){
            Toast.makeText(mContext,"至少添加一张图片",Toast.LENGTH_SHORT).show();
        } else {
            List<String> paths = FileUtils.getPathList(getActivity(), dataList);
            File[] files = new File[paths.size()];
            for (int i=0; i<paths.size(); i++){
                files[i] = new File(paths.get(i));
            }
            if (swState.isChecked()){
                goods.setState(1);
            } else {
                goods.setState(2);
            }
            if (isEdit){
                url = Constants.GOODS_URL+"/edit";
            } else {
                url = Constants.GOODS_URL+"/add";
            }
            params.put("bid", BusinessFragment.business.getBid());
            params.put("name",etName.getText().toString().trim());
            params.put("price",Double.valueOf(etPrice.getText().toString().trim()));
            params.put("count",Integer.valueOf(etCount.getText().toString().trim()));
            params.put("cid1",goods.getCid1());
            params.put("cid2",goods.getCid2());
            params.put("cid3",goods.getCid3());
            params.put("category",goods.getCategory());
            if (swState.isChecked()){
                params.put("state", 1);
            } else {
                params.put("state", 2);
            }
            if (isEdit) {
                params.put("discount", Double.valueOf(etDiscount.getText().toString().trim()));
                params.put("gid", goods.getGid());
            }
            try {
                params.put("files",files);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody, StandardCharsets.UTF_8);
                    Type type = new TypeToken<PostMessage<Void>>(){}.getType();
                    PostMessage<Void> postMessage = new Gson().fromJson(response, type);
                    if (postMessage.getMessage() == null){
                        Toast.makeText(mContext,"提交成功",Toast.LENGTH_SHORT).show();
                        FragmentUtils.popBack(getActivity());
                    } else {
                        Toast.makeText(mContext,postMessage.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(mContext,"GoodsEditFragment(341):请求失败",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @OnClick(R.id.bzns_goods_add_ib_delete)
    public void deleteGoods() {
        String url = Constants.GOODS_URL+"/goods_delete";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("gid", goods.getGid());
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody, StandardCharsets.UTF_8);
                Type type = new TypeToken<PostMessage<Void>>(){}.getType();
                PostMessage<Void> postMessage = new Gson().fromJson(response, type);
                if (postMessage.getMessage() == null){
                    Toast.makeText(mContext,"提交成功",Toast.LENGTH_SHORT).show();
                    FragmentUtils.popBack(getActivity());
                } else {
                    Toast.makeText(mContext,postMessage.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(mContext,"GoodsEditFragment(369):请求失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.bzns_goods_add_ib_back)
    public void back(){FragmentUtils.popBack(getActivity());}
}
