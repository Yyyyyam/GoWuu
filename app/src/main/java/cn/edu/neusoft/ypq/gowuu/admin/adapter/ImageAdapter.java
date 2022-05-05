package cn.edu.neusoft.ypq.gowuu.admin.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

import cn.edu.neusoft.ypq.gowuu.utils.GlideUtils;

/**
 * 作者:颜培琦
 * 时间:2022/3/12
 * 功能:ImageAdapter
 */
public class ImageAdapter extends BannerAdapter<String,ImageAdapter.BannerViewHolder> {

    boolean isHome = false;

    public ImageAdapter(List<String> paths) {
        super(paths);
        //设置数据，也可以调用banner提供的方法,或者自己在adapter中实现

    }

    public ImageAdapter(List<String> paths, boolean isHome) {
        super(paths);
        //设置数据，也可以调用banner提供的方法,或者自己在adapter中实现
        this.isHome = isHome;
    }

    //创建ViewHolder，可以用viewType这个字段来区分不同的ViewHolder
    @Override
    public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                parent.getLayoutParams()));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new BannerViewHolder(imageView);
    }

    @Override
    public void onBindView(BannerViewHolder holder, String data, int position, int size) {
        GlideUtils.setImage(holder.imageView.getContext(), data, holder.imageView);
//        Glide.with(holder.imageView.getContext()).load(data).into(holder.imageView);
        if (isHome) {
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClick(data, position);
                }
            });
        }
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public BannerViewHolder(@NonNull ImageView view) {
            super(view);
            this.imageView = view;
        }
    }

    private OnClickListener onClickListener;

    public interface OnClickListener{
        void onClick(String data, int position);
    }

    public void onClick(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
