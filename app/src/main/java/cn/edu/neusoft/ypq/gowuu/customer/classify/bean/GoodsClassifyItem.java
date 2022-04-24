package cn.edu.neusoft.ypq.gowuu.customer.classify.bean;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author yanpeiqi
 * @describe 分类界面父分类
 * @create 2022/3/13 - 10:43
 */
public class GoodsClassifyItem implements Serializable {
    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "GoodsClassifyParent{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
