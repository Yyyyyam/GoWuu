package cn.edu.neusoft.ypq.gowuu.customer.me.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author yanpeiqi
 * @describe
 * @create 2022/4/3 - 21:00
 */
public class Evaluation implements Serializable {
    private Integer eid;
    private Integer gid;
    private Integer uid;
    private Integer id;
    private Integer state;
    private String detail;
    private String date;
    private List<String> pathList;
    private String userAvatar;
    private String userName;

    public Evaluation() {
    }

    public Evaluation(Integer eid, Integer gid, Integer uid, Integer id, Integer state, String detail, String date, List<String> pathList, String userAvatar, String userName) {
        this.eid = eid;
        this.gid = gid;
        this.uid = uid;
        this.id = id;
        this.state = state;
        this.detail = detail;
        this.date = date;
        this.pathList = pathList;
        this.userAvatar = userAvatar;
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<String> getPathList() {
        return pathList;
    }

    public void setPathList(List<String> pathList) {
        this.pathList = pathList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Evaluation that = (Evaluation) o;
        return Objects.equals(eid, that.eid) && Objects.equals(gid, that.gid) && Objects.equals(uid, that.uid) && Objects.equals(id, that.id) && Objects.equals(state, that.state) && Objects.equals(detail, that.detail) && Objects.equals(date, that.date) && Objects.equals(pathList, that.pathList) && Objects.equals(userAvatar, that.userAvatar) && Objects.equals(userName, that.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eid, gid, uid, id, state, detail, date, pathList, userAvatar, userName);
    }

    @Override
    public String toString() {
        return "Evaluation{" +
                "eid=" + eid +
                ", gid=" + gid +
                ", uid=" + uid +
                ", id=" + id +
                ", state=" + state +
                ", detail='" + detail + '\'' +
                ", date='" + date + '\'' +
                ", pathList=" + pathList +
                ", userAvatar='" + userAvatar + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
