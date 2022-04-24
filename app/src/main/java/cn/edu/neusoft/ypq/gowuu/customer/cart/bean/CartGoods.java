package cn.edu.neusoft.ypq.gowuu.customer.cart.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import cn.edu.neusoft.ypq.gowuu.business.bean.Goods;

/**
 * @author yanpeiqi
 * @describe 购物车的实体类
 * @create 2022/3/8 - 15:37
 */
public class CartGoods implements Serializable {
    private Integer id;
    private Integer cid;
    private Integer gid;
    private Integer count;
    private Goods goods;
    private boolean isSelect;
    private Integer position;

    public CartGoods() {
    }

    public CartGoods(Integer id, Integer cid, Integer gid, Integer count, Goods goods) {
        this.id = id;
        this.cid = cid;
        this.gid = gid;
        this.count = count;
        this.goods = goods;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getCount() {
        return count;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public boolean getSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartGoods cartGoods = (CartGoods) o;
        return Objects.equals(id, cartGoods.id) && Objects.equals(cid, cartGoods.cid) && Objects.equals(gid, cartGoods.gid) && Objects.equals(count, cartGoods.count) && Objects.equals(goods, cartGoods.goods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cid, gid, count, goods);
    }

    @Override
    public String toString() {
        return "CartGoods{" +
                "id=" + id +
                ", cid=" + cid +
                ", gid=" + gid +
                ", count=" + count +
                ", goods=" + goods +
                '}';
    }
}
