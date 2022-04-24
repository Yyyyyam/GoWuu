package cn.edu.neusoft.ypq.gowuu.customer.home.bean;

import java.io.Serializable;
import java.util.Objects;

import cn.edu.neusoft.ypq.gowuu.business.bean.Goods;

/**
 * @author yanpeiqi
 * @describe
 * @create 2022/3/26 - 22:07
 */
public class Favorite implements Serializable {
    private Integer fid;
    private Integer uid;
    private Integer gid;
    private Double price;
    private Goods goods;
    private String time;
    private boolean isChecked;
    private Integer position;

    public Favorite() {
    }

    public Favorite(Integer fid, Integer uid, Integer gid, Double price, Goods goods, String time, boolean isChecked, Integer position) {
        this.fid = fid;
        this.uid = uid;
        this.gid = gid;
        this.price = price;
        this.goods = goods;
        this.time = time;
        this.isChecked = isChecked;
        this.position = position;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public boolean getChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favorite favorite = (Favorite) o;
        return Objects.equals(fid, favorite.fid) && Objects.equals(uid, favorite.uid) && Objects.equals(gid, favorite.gid) && Objects.equals(price, favorite.price) && Objects.equals(goods, favorite.goods) && Objects.equals(time, favorite.time) && Objects.equals(isChecked, favorite.isChecked) && Objects.equals(position, favorite.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fid, uid, gid, price, goods, time, isChecked, position);
    }

    @Override
    public String toString() {
        return "Favorite{" +
                "fid=" + fid +
                ", uid=" + uid +
                ", gid=" + gid +
                ", price=" + price +
                ", goods=" + goods +
                ", time='" + time + '\'' +
                ", isChecked=" + isChecked +
                ", position=" + position +
                '}';
    }
}
