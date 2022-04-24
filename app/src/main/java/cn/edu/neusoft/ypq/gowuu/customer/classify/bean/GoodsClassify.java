package cn.edu.neusoft.ypq.gowuu.customer.classify.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author yanpeiqi
 * @describe
 * @create 2022/3/13 - 10:43
 */
public class GoodsClassify implements Serializable {
    private GoodsClassifyItem item;
    private List<GoodsClassifyItem> childItems;

    public GoodsClassify() {
    }

    public GoodsClassify(GoodsClassifyItem item, List<GoodsClassifyItem> childItems) {
        this.item = item;
        this.childItems = childItems;
    }

    public GoodsClassifyItem getItem() {
        return item;
    }

    public void setItem(GoodsClassifyItem item) {
        this.item = item;
    }

    public List<GoodsClassifyItem> getChildItems() {
        return childItems;
    }

    public void setChildItems(List<GoodsClassifyItem> childItems) {
        this.childItems = childItems;
    }

    @Override
    public String toString() {
        return "GoodsClassify{" +
                "item=" + item +
                ", childItems=" + childItems +
                '}';
    }
}
