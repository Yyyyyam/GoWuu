package cn.edu.neusoft.ypq.gowuu.customer.me.bean;

import java.util.Objects;

/**
 * @author yanpeiqi
 * @describe 省市区的实体类
 * @create 2022/3/8 - 15:37
 */
public class District {
    private Integer id;
    private String parent;
    private String code;
    private String name;

    public District() {
    }

    public District(Integer id, String parent, String code, String name) {
        this.id = id;
        this.parent = parent;
        this.code = code;
        this.name = name;
    }

    public void setDistrict(District district){
        this.id = district.getId();
        this.parent = district.getParent();
        this.code = district.getCode();
        this.name = district.getName();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "District{" +
                "id=" + id +
                ", parent='" + parent + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        District district = (District) o;
        return Objects.equals(id, district.id) && Objects.equals(parent, district.parent) && Objects.equals(code, district.code) && Objects.equals(name, district.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parent, code, name);
    }
}
