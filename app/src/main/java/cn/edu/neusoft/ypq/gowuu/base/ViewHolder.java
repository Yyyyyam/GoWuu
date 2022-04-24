package cn.edu.neusoft.ypq.gowuu.base;

import android.content.Context;
import android.net.Uri;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import cn.edu.neusoft.ypq.gowuu.admin.adapter.ImageAdapter;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.GlideUtils;

/**
 * 作者:颜培琦
 * 时间:2022/3/23
 * 功能:ViewHolder
 */
public class ViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private View mConvertView;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        mConvertView = itemView;
        mViews = new SparseArray<>();
    }

    public static ViewHolder create(Context context, int layoutId, ViewGroup parent) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new ViewHolder(itemView);
    }

    public static ViewHolder create(View itemView) {
        return new ViewHolder(itemView);
    }

    /**
     * 通过id获得控件
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    public View getSwipeView() {
        ViewGroup itemLayout = ((ViewGroup) mConvertView);
        if (itemLayout.getChildCount() == 2) {
            return itemLayout.getChildAt(1);
        }
        return null;
    }

    public void setText(int viewId, String text) {
        TextView textView = getView(viewId);
        textView.setText(text);
    }

    public void setText(int viewId, int textId) {
        TextView textView = getView(viewId);
        textView.setText(textId);
    }

    public void setTextColor(int viewId, int colorId) {
        TextView textView = getView(viewId);
        textView.setTextColor(colorId);
    }

    public void setTextSelect(int viewId, int colorId, float textSize, boolean isSelect){
        TextView textView = getView(viewId);
        textView.setTextSize(textSize);
        textView.setTextColor(mConvertView.getResources().getColor(colorId,null));
        if (isSelect) textView.callOnClick();
    }

    public void setBackground(int viewId, int bgId){
        View view = getView(viewId);
        view.setBackgroundResource(bgId);
    }

    public void setOnClickListener(View view, View.OnClickListener clickListener){
        view.setOnClickListener(clickListener);
    }

    public void setOnClickListener(int viewId, View.OnClickListener clickListener) {
        View view = getView(viewId);
        view.setOnClickListener(clickListener);
    }

    public void setVisibility(int viewId, int visibility) {
        View view = getView(viewId);
        view.setVisibility(visibility);
    }

    public void setBanner(int viewId, List<String> paths, int loopTime){
        Banner banner = getView(viewId);

        List<String> pathList = new ArrayList<>();
        for (String path : paths){
            pathList.add(Constants.RES_URL+path);
        }
        banner.setAdapter(new ImageAdapter(pathList));
        banner.setLoopTime(loopTime);
    }

    public void setImageView(int viewId, Uri uri){
        ImageView imageView = getView(viewId);
        imageView.setImageURI(uri);
    }

    public void setImageView(int viewId, int resId){
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resId);
    }

    public void setImageView(int viewId, String url){
        ImageView imageView = getView(viewId);
        GlideUtils.setImage(mConvertView.getContext(), url, imageView);
    }

    public void setChecked(int viewId, boolean checked){
        CheckBox checkBox = getView(viewId);
        checkBox.setChecked(checked);
    }
}
