package cn.edu.neusoft.ypq.gowuu.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

/**
 * 作者:颜培琦
 * 时间:2022/1/28
 * 功能:BaseFragment,基类Fragment，其他Fragment类都要继承此类
 */

public abstract class BaseFragment<T> extends Fragment {

    protected Integer fragmentId;

    protected View view;
    protected Context mContext;
    protected List<T> dataList;

    protected BaseAdapter<T> adapter;
    protected Integer page;
    protected Integer pageSize;
    public Boolean pageEnd = false;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView();
    }

    //
    public abstract View initView();


    /**
     * UI的创建：当Activity被创建时回调回调此方法
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    //初始化内容当子类需要联网请求数据时，可以重写该方法
    public void initData(){

    }
}