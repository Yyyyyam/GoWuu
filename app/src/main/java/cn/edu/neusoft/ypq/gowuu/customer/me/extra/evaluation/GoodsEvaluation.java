package cn.edu.neusoft.ypq.gowuu.customer.me.extra.evaluation;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRatingBar;
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
import cn.edu.neusoft.ypq.gowuu.admin.adapter.ImageAdapter;
import cn.edu.neusoft.ypq.gowuu.app.MainActivity;
import cn.edu.neusoft.ypq.gowuu.base.BaseFragment;
import cn.edu.neusoft.ypq.gowuu.customer.cart.bean.CartGoods;
import cn.edu.neusoft.ypq.gowuu.customer.me.adapter.RequestImgAdapter;
import cn.edu.neusoft.ypq.gowuu.customer.me.bean.Order;
import cn.edu.neusoft.ypq.gowuu.customer.me.bean.OrderGoods;
import cn.edu.neusoft.ypq.gowuu.customer.me.extra.order.OrderAll;
import cn.edu.neusoft.ypq.gowuu.customer.me.extra.order.OrderReceived;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.FileUtils;
import cn.edu.neusoft.ypq.gowuu.utils.FragmentUtils;
import cn.edu.neusoft.ypq.gowuu.utils.GlideUtils;
import cn.edu.neusoft.ypq.gowuu.utils.PostMessage;
import cz.msebera.android.httpclient.Header;

/**
 * 作者:颜培琦
 * 时间:2022/4/3
 * 功能:GoodsEvaluation
 */
public class GoodsEvaluation extends BaseFragment<Void> {
    public static Order order;
    private List<Uri> uriList;
    private RequestImgAdapter imageAdapter;

    @BindView(R.id.goods_evlt_iv_goods)
    ImageView ivGoods;
    @BindView(R.id.goods_evlt_tv_goods_name)
    TextView tvName;
    @BindView(R.id.goods_evlt_tv_goods_type)
    TextView tvCategory;
    @BindView(R.id.goods_evlt_tv_bzns_name)
    TextView tvBznsName;
    @BindView(R.id.goods_evlt_rb_1)
    RadioButton rb1;
    @BindView(R.id.goods_evlt_rb_2)
    RadioButton rb2;
    @BindView(R.id.goods_evlt_radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.goods_evlt_et_detail)
    EditText etDetail;
    @BindView(R.id.goods_evlt_rv)
    RecyclerView recyclerView;
    @BindView(R.id.goods_evlt_rb_3)
    RadioButton rb3;
    @BindView(R.id.goods_evlt_rating_bar1)
    AppCompatRatingBar ratingBar1;
    @BindView(R.id.goods_evlt_rating_bar2)
    AppCompatRatingBar ratingBar2;
    @BindView(R.id.goods_evlt_rating_bar3)
    AppCompatRatingBar ratingBar3;


    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_goods_evaluation, null);
        ButterKnife.bind(this, view);

        uriList = new ArrayList<>();
        imageAdapter = new RequestImgAdapter(mContext, uriList);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext,3));
        recyclerView.setAdapter(imageAdapter);

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        setData();
    }

    private void setData(){
        if (order != null) {
            OrderGoods goods = order.getGoodsList().get(0);
            GlideUtils.setImage(mContext, Constants.RES_URL+goods.getPic(), ivGoods);
            tvName.setText(goods.getName());
            tvCategory.setText(goods.getCategory());
            tvBznsName.setText(order.getBznsName());
        }
    }

    @OnClick(R.id.cstm_request_ib_add)
    public void add(){
        if (uriList.size() < 5){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        } else {
            Toast.makeText(mContext,"最多添加5张图片",Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.goods_evlt_bt_commit)
    public void commit(){
        int evltState = -1;
        int bznsScore1 = ratingBar1.getProgress();
        int bznsScore2 = ratingBar2.getProgress();
        int bznsScore3 = ratingBar3.getProgress();
        String detail = etDetail.getText().toString().trim();
        switch (radioGroup.getCheckedRadioButtonId()){
            case R.id.goods_evlt_rb_1:
                evltState = 0;
                break;
            case R.id.goods_evlt_rb_2:
                evltState = 1;
                break;
            case R.id.goods_evlt_rb_3:
                evltState = 2;
                break;
            default:
                evltState = -1;
                break;
        }
        if (evltState == -1){
            Toast.makeText(mContext,"请选择好评/中评/差评",Toast.LENGTH_SHORT).show();
        } else if (detail.isEmpty()){
            Toast.makeText(mContext,"请输入详细评价内容",Toast.LENGTH_SHORT).show();
        } else if (uriList.size() == 0){
            Toast.makeText(mContext,"至少选择一张图片",Toast.LENGTH_SHORT).show();
        } else if (bznsScore1==0 || bznsScore2==0 || bznsScore3==0){
            Toast.makeText(mContext,"请选择评分",Toast.LENGTH_SHORT).show();
        } else {
            //提交数据
            List<String> paths = FileUtils.getPathList(requireActivity(), uriList);
            File[] files = new File[paths.size()];
            for (int i=0; i<paths.size(); i++){
                files[i] = new File(paths.get(i));
            }
            String url = Constants.EVALUATION_URL+"/add";
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            OrderGoods goods = order.getGoodsList().get(0);
            params.put("gid", goods.getGid());
            params.put("uid", MainActivity.user.getUid());
            params.put("id", goods.getId());
            params.put("state", evltState);
            params.put("detail",detail);
            params.put("score1", bznsScore1);
            params.put("score2", bznsScore2);
            params.put("score3", bznsScore3);
            try {
                params.put("files", files);
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
                        if (OrderAll.adapter != null) {
                            OrderAll.adapter.getDataList().get(order.getPosition()).getGoodsList().get(goods.getPosition()).setState(2);
                            OrderAll.adapter.notifyItemChanged(order.getPosition());
                        }
                        if (OrderReceived.adapter != null) {
                            OrderReceived.adapter.getDataList().get(order.getPosition()).getGoodsList().get(goods.getPosition()).setState(2);
                            OrderReceived.adapter.notifyItemChanged(order.getPosition());
                        }
                        FragmentUtils.popBack(requireActivity());
                    } else {
                        Toast.makeText(mContext,postMessage.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(mContext,"GoodsEvaluation(192):请求失败",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @OnClick(R.id.goods_evlt_iv_back)
    public void back(){
        FragmentUtils.popBack(requireActivity());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    //根据uri添加图片到UriList
                    imageAdapter.insert(uri);
                }
        }
    }
}
