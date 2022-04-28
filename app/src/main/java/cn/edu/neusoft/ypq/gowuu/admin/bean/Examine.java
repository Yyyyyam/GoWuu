package cn.edu.neusoft.ypq.gowuu.admin.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author yanpeiqi
 * @describe 审核需要的实体类
 * @create 2022/3/12 - 13:30
 */
public class Examine implements Serializable {
    private int rid;
    private int uid;
    private Integer gid;
    private String avatar;
    private String name;
    private String time;
    private int type;
    private String detail;
    private int state;
    private List<String> requestPic;
    private String businessName;

    public Examine() {
    }

    public Examine(int rid, int uid, Integer gid, String avatar, String name, String time, int type, String detail, int state, List<String> requestPic, String businessName) {
        this.rid = rid;
        this.uid = uid;
        this.gid = gid;
        this.avatar = avatar;
        this.name = name;
        this.time = time;
        this.type = type;
        this.detail = detail;
        this.state = state;
        this.requestPic = requestPic;
        this.businessName = businessName;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<String> getRequestPic() {
        return requestPic;
    }

    public void setRequestPic(List<String> requestPic) {
        this.requestPic = requestPic;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Examine examine = (Examine) o;
        return uid == examine.uid && rid == examine.rid && type == examine.type && state == examine.state && Objects.equals(avatar, examine.avatar) && Objects.equals(name, examine.name) && Objects.equals(time, examine.time) && Objects.equals(detail, examine.detail) && Objects.equals(requestPic, examine.requestPic) && Objects.equals(businessName, examine.businessName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, avatar, name, rid, time, type, detail, state, requestPic, businessName);
    }

    @Override
    public String toString() {
        return "Examine{" +
                "uid=" + uid +
                ", avatar='" + avatar + '\'' +
                ", name='" + name + '\'' +
                ", rid=" + rid +
                ", time='" + time + '\'' +
                ", type=" + type +
                ", detail='" + detail + '\'' +
                ", state=" + state +
                ", requestPic=" + requestPic +
                ", businessName='" + businessName + '\'' +
                '}';
    }
}
