package cn.edu.neusoft.ypq.gowuu.customer.home.bean;

import java.io.Serializable;
import java.util.List;

import cn.edu.neusoft.ypq.gowuu.business.bean.Goods;

/**
 * @author yanpeiqi
 * @describe
 * @create 2022/4/29 - 20:31
 */
public class HomeData implements Serializable {
    private List<String> bannerList;
    private List<Goods> discountGoods;

    public List<String> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<String> bannerList) {
        this.bannerList = bannerList;
    }

    public List<Goods> getDiscountGoods() {
        return discountGoods;
    }

    public void setDiscountGoods(List<Goods> discountGoods) {
        this.discountGoods = discountGoods;
    }

    @Override
    public String toString() {
        return "HomeData{" +
                "bannerList=" + bannerList +
                ", discountGoods=" + discountGoods +
                '}';
    }
}
