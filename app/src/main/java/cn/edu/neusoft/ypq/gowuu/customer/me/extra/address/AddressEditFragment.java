package cn.edu.neusoft.ypq.gowuu.customer.me.extra.address;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;

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
import cn.edu.neusoft.ypq.gowuu.customer.me.bean.Address;
import cn.edu.neusoft.ypq.gowuu.customer.me.bean.District;
import cn.edu.neusoft.ypq.gowuu.utils.CheckUtils;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.FragmentUtils;
import cn.edu.neusoft.ypq.gowuu.utils.PostMessage;
import cz.msebera.android.httpclient.Header;

/**
 * 作者:颜培琦
 * 时间:2022/3/8
 * 功能:AddressEditFragment
 */
public class AddressEditFragment extends BaseFragment<Void> {

    private List<District> pDistrict = new ArrayList<>();
    private List<District> cDistrict = new ArrayList<>();
    private List<District> aDistrict = new ArrayList<>();
    private final List<String> pList = new ArrayList<>();
    private final List<String> cList = new ArrayList<>();
    private final List<String> aList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    String url = Constants.DISTRICT_URL;

    Address address = new Address();

    @BindView(R.id.address_edit_sp_province)
    Spinner spProvince;
    @BindView(R.id.address_edit_sp_city)
    Spinner spCity;
    @BindView(R.id.address_edit_sp_area)
    Spinner spArea;
    @BindView(R.id.address_edit_et_name)
    EditText etName;
    @BindView(R.id.address_edit_et_phone)
    EditText etPhone;
    @BindView(R.id.address_edit_et_etDetail)
    EditText etDetail;
    @BindView((R.id.address_edit_et_tag))
    EditText etTag;
    @BindView(R.id.address_edit_sw_default)
    SwitchCompat isDefault;
    @BindView(R.id.address_edit_tv_phone_check)
    TextView tvPhoneCheck;
    @BindView(R.id.address_edit_tv_delete)
    TextView tvDelete;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_cstm_address_edit, null);
        ButterKnife.bind(this, view);
        CheckUtils.startPhoneCheck(etPhone, tvPhoneCheck);
        init();
        return view;
    }

    private void init(){
        if (AddressFragment.isModify){
            address.setAddress(AddressFragment.address);
            tvDelete.setVisibility(View.VISIBLE);
            etName.setText(address.getName());
            etPhone.setText(address.getPhone());
            etDetail.setText(address.getDetail());
            etTag.setText(address.getTag());
            isDefault.setChecked(address.getIsDefault() == 1);
        }
        initLocation();
    }

     //地址的获取，通过网络请求数据库，三段式获取地址信息
    private void initLocation(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params =new RequestParams();
        params.put("parent", "86");
        client.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody, StandardCharsets.UTF_8);
                Type type = new TypeToken<PostMessage<List<District>>>() {}.getType();
                PostMessage<List<District>> postMessage = new Gson().fromJson(response, type);
                if (postMessage.getMessage() == null){
                    pDistrict = postMessage.getData();
                    pList.clear();
                    for (District d : pDistrict) {
                        pList.add(d.getName());
                    }
                    adapter = new ArrayAdapter<>(mContext , android.R.layout.simple_spinner_item, pList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spProvince.setAdapter(adapter);
                    if (address.getProvinceName() != null){
                        spProvince.setSelection(pList.indexOf(address.getProvinceName()));
                    }
                    spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            address.setProvinceCode(pDistrict.get(position).getCode());
                            address.setProvinceName(pDistrict.get(position).getName());
                            AsyncHttpClient client = new AsyncHttpClient();
                            RequestParams params =new RequestParams();
                            params.put("parent", pDistrict.get(position).getCode());
                            client.get(url, params, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    String response = new String(responseBody, StandardCharsets.UTF_8);
                                    Type type = new TypeToken<PostMessage<List<District>>>() {
                                    }.getType();
                                    PostMessage<List<District>> postMessage = new Gson().fromJson(response, type);
                                    if (postMessage.getMessage() == null){
                                        cDistrict = postMessage.getData();
                                        cList.clear();
                                        for (District d : cDistrict) {
                                            cList.add(d.getName());
                                        }
                                        adapter = new ArrayAdapter<>(mContext , android.R.layout.simple_spinner_item, cList);
                                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spCity.setAdapter(adapter);
                                        if (address.getCityName() != null){
                                            spCity.setSelection(cList.indexOf(address.getCityName()));
                                        }
                                        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                address.setCityCode(cDistrict.get(position).getCode());
                                                address.setCityName(cDistrict.get(position).getName());
                                                AsyncHttpClient client = new AsyncHttpClient();
                                                RequestParams params =new RequestParams();
                                                params.put("parent", cDistrict.get(position).getCode());
                                                client.get(url, params, new AsyncHttpResponseHandler() {
                                                    @Override
                                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                        String response = new String(responseBody, StandardCharsets.UTF_8);
                                                        Type type = new TypeToken<PostMessage<List<District>>>() {
                                                        }.getType();
                                                        PostMessage<List<District>> postMessage = new Gson().fromJson(response, type);
                                                        if (postMessage.getMessage() == null){
                                                            aDistrict = postMessage.getData();
                                                            aList.clear();
                                                            for (District d : aDistrict) {
                                                                aList.add(d.getName());
                                                            }
                                                            adapter = new ArrayAdapter<>(mContext , android.R.layout.simple_spinner_item, aList);
                                                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                            spArea.setAdapter(adapter);
                                                            if (address.getAreaName() != null){
                                                                spArea.setSelection(aList.indexOf(address.getAreaName()));
                                                            }
                                                            spArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                @Override
                                                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                                    address.setAreaCode(aDistrict.get(position).getCode());
                                                                    address.setAreaName(aDistrict.get(position).getName());
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
                                                        Toast.makeText(mContext,"AddressEditFragment(196):请求失败",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
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
                                    Toast.makeText(mContext,"AddressEditFragment(209):请求失败",Toast.LENGTH_SHORT).show();
                                }
                            });
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
                Toast.makeText(mContext,"AddressEditFragment(223):请求失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.address_edit_bt_confirm)
    public void commit(){
        String url;
        AsyncHttpClient client = new AsyncHttpClient();
        address.setName(etName.getText().toString().trim());
        address.setPhone(etPhone.getText().toString().trim());
        address.setDetail(etDetail.getText().toString().trim());
        address.setTag(etTag.getText().toString().trim());
        if (isDefault.isChecked()){
            address.setIsDefault(1);
        }else {
            address.setIsDefault(0);
        }
        if (address.getName().isEmpty()||address.getPhone().isEmpty()||address.getDetail().isEmpty()||address.getTag().isEmpty()){
            Toast.makeText(mContext,"请补全信息后提交",Toast.LENGTH_SHORT).show();
        } else if (!CheckUtils.isPhone(address.getPhone())){
            Toast.makeText(mContext,"手机格式有问题，请重新输入",Toast.LENGTH_SHORT).show();
        } else{
            //提交数据
            RequestParams params = new RequestParams();
            if (AddressFragment.isModify){
                url = Constants.ADDRESS_URL+"/update";
                params.put("aid",address.getAid());
                params.put("uid",address.getUid());
            } else {
                url = Constants.ADDRESS_URL+"/add_address";
                params.put("id", MainActivity.user.getUid());
            }
            params.put("name",address.getName());
            params.put("phone",address.getPhone());
            params.put("provinceName",address.getProvinceName());
            params.put("provinceCode",address.getProvinceCode());
            params.put("cityName",address.getCityName());
            params.put("cityCode",address.getCityCode());
            params.put("areaName",address.getAreaName());
            params.put("areaCode",address.getAreaCode());
            params.put("detail",address.getDetail());
            params.put("tag",address.getTag());
            params.put("isDefault",address.getIsDefault());
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody, StandardCharsets.UTF_8);
                    Type type = new TypeToken<PostMessage<Void>>() {}.getType();
                    PostMessage<Void> postMessage = new Gson().fromJson(response, type);
                    if (postMessage.getMessage()==null){
                        Toast.makeText(mContext,"提交成功",Toast.LENGTH_SHORT).show();
                        FragmentUtils.popBack(requireActivity());
                    }else {
                        Toast.makeText(mContext, postMessage.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(mContext,"AddressEditFragment(282):请求失败",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @OnClick(R.id.address_edit_ib_back)
    public void back(){
        FragmentUtils.popBack(requireActivity());
    }

    @OnClick(R.id.address_edit_tv_delete)
    public void delete(){
        String url = Constants.ADDRESS_URL+"/delete";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("uid", MainActivity.user.getUid());
        params.put("aid", address.getAid());
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody, StandardCharsets.UTF_8);
                Type type = new TypeToken<PostMessage<Void>>() {}.getType();
                PostMessage<Void> postMessage = new Gson().fromJson(response, type);
                if (postMessage.getMessage()==null){
                    Toast.makeText(mContext,"删除成功",Toast.LENGTH_SHORT).show();
                    FragmentUtils.popBack(requireActivity());
                }else {
                    Toast.makeText(mContext, postMessage.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(mContext,"AddressEditFragment(315):请求失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
