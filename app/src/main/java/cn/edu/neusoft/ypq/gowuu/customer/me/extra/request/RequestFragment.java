package cn.edu.neusoft.ypq.gowuu.customer.me.extra.request;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import cn.edu.neusoft.ypq.gowuu.app.MainActivity;
import cn.edu.neusoft.ypq.gowuu.base.BaseFragment;
import cn.edu.neusoft.ypq.gowuu.customer.me.adapter.RequestImgAdapter;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.FileUtils;
import cn.edu.neusoft.ypq.gowuu.utils.FragmentUtils;
import cn.edu.neusoft.ypq.gowuu.utils.PostMessage;
import cz.msebera.android.httpclient.Header;

/**
 * 作者:颜培琦
 * 时间:2022/3/9
 * 功能:RequestFragment
 */
public class RequestFragment extends BaseFragment<Uri> {

    private boolean isRequest = false;
    private Integer gid;

    @BindView(R.id.cstm_request_rv)
    RecyclerView recyclerView;
    @BindView(R.id.cstm_request_et_detail)
    EditText etDetail;
    @BindView(R.id.cstm_request_et_name)
    EditText etName;

    public RequestFragment(boolean isRequest, Integer gid) {
        this.isRequest = isRequest;
        this.gid = gid;
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_cstm_request, null);
        ButterKnife.bind(this, view);

        if (!isRequest) {
            TextView textView = view.findViewById(R.id.cstm_request_tv1);
            textView.setText("举报理由");
            etName.setHint("请输入举报理由");
        }

        dataList = new ArrayList<>();
        //通过Adapter加载Uri到recyclerView
        adapter = new RequestImgAdapter(mContext, dataList);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext,3));
        recyclerView.setAdapter(adapter);
        return view;
    }

    @OnClick(R.id.cstm_request_ib_add)
    public void add(){
        if (dataList.size() < 5){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        } else {
            Toast.makeText(mContext,"最多添加5张照片",Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.cstm_request_ib_back)
    public void back(){
        FragmentUtils.popBack(requireActivity());
    }

    @OnClick(R.id.cstm_request_bt_confirm)
    public void confirm(){
        String url;
        if (isRequest) {
            url = Constants.REQUEST_URL+"/add_request";
        } else {
            url = Constants.REQUEST_URL+"/add_report";
        }
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        String name = etName.getText().toString().trim();
        String detail = etDetail.getText().toString().trim();
        List<String> paths = FileUtils.getPathList(requireActivity(), dataList);
        File[] files = new File[paths.size()];
        for (int i=0; i<paths.size(); i++){
            files[i] = new File(paths.get(i));
        }
        if (detail.isEmpty()||name.isEmpty()){
            Toast.makeText(mContext,"请将内容填写完整",Toast.LENGTH_SHORT).show();
        } else if (dataList.size() == 0){
            Toast.makeText(mContext,"至少选择一张图片",Toast.LENGTH_SHORT).show();
        } else {
            //提交数据
            params.put("uid", MainActivity.user.getUid());
            if (isRequest) {
                params.put("type",0);
            } else {
                params.put("type",1);
                params.put("gid", gid);
            }
            params.put("detail",detail);
            params.put("others", name);
            try {
                params.put("files", files);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody, StandardCharsets.UTF_8);
                    //修正泛型无法正常解析问题
                    Type type = new TypeToken<PostMessage<Void>>() {
                    }.getType();
                    PostMessage<Void> postMessage = new Gson().fromJson(response, type);
                    if (postMessage.getMessage()==null){
                        Toast.makeText(mContext,"提交成功",Toast.LENGTH_SHORT).show();
                        FragmentUtils.popBack(requireActivity());
                    }else {
                        Toast.makeText(mContext, postMessage.getMessage(),Toast.LENGTH_SHORT).show();
                        FragmentUtils.popBack(requireActivity());
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(mContext,"RequestFragment(127):请求失败",Toast.LENGTH_SHORT).show();
                }
            });
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
}
