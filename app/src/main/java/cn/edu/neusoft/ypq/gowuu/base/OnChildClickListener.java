package cn.edu.neusoft.ypq.gowuu.base;

/**
 * 作者:颜培琦
 * 时间:2022/3/23
 * 功能:OnItemClickListener
 */
public interface OnChildClickListener<T> {
    /**
     * 普通点击事件
     * @param holder ViewHolder
     * @param data 数据
     * @param position 位置
     */
    void onChildClick(ViewHolder holder, T data, int position);
}
