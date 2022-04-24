package cn.edu.neusoft.ypq.gowuu.customer.me.bean;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author yanpeiqi
 * @describe 订单商品实体类
 * @create 2022/3/31 - 0:15
 */
public class OrderGoods implements Serializable {
    private Integer id;
    private Integer oid;
    private Integer gid;
    private String name;
    private String pic;
    private Double price;
    private Integer count;
    private Integer state;
    private String category;
    private Integer position;

    public OrderGoods() {
    }

    public OrderGoods(Integer id, Integer oid, Integer gid, String name, String pic, Double price, Integer count, Integer state) {
        this.id = id;
        this.oid = oid;
        this.gid = gid;
        this.name = name;
        this.pic = pic;
        this.price = price;
        this.count = count;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
        OrderGoods goods = (OrderGoods) o;
        return Objects.equals(id, goods.id) && Objects.equals(oid, goods.oid) && Objects.equals(gid, goods.gid) && Objects.equals(name, goods.name) && Objects.equals(pic, goods.pic) && Objects.equals(price, goods.price) && Objects.equals(count, goods.count) && Objects.equals(state, goods.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, oid, gid, name, pic, price, count, state);
    }

    @Override
    public String toString() {
        return "OrderGoods{" +
                "id=" + id +
                ", oid=" + oid +
                ", gid=" + gid +
                ", name='" + name + '\'' +
                ", pic='" + pic + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", state=" + state +
                '}';
    }
}
