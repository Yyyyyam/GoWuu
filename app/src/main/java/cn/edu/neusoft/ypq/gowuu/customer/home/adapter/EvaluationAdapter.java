package cn.edu.neusoft.ypq.gowuu.customer.home.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.base.BaseAdapter;
import cn.edu.neusoft.ypq.gowuu.base.ViewHolder;
import cn.edu.neusoft.ypq.gowuu.customer.me.bean.Evaluation;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;

/**
 * 作者:颜培琦
 * 时间:2022/4/4
 * 功能:EvaluationAdapter
 */
public class EvaluationAdapter extends BaseAdapter<Evaluation> {
    private Boolean isGoodsDetail;

    public EvaluationAdapter(Context context, List<Evaluation> dataList, Boolean isGoodsDetail) {
        super(context, dataList);
        this.isGoodsDetail = isGoodsDetail;
    }

    @Override
    protected void convert(ViewHolder holder, Evaluation data, int position) {
        if (!isGoodsDetail) {
            holder.setVisibility(R.id.itm_goods_evlt_banner, View.VISIBLE);
            holder.setBanner(R.id.itm_goods_evlt_banner, data.getPathList(), 60000);
        }
        holder.setText(R.id.itm_goods_evlt_tv_name, data.getUserName());
        holder.setImageView(R.id.itm_goods_evlt_iv_avatar, Constants.RES_URL+data.getUserAvatar());
        holder.setText(R.id.itm_goods_evlt_tv_time, data.getDate());
        holder.setText(R.id.itm_goods_evlt_tv_detail, data.getDetail());
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_goods_evaluation;
    }
}
