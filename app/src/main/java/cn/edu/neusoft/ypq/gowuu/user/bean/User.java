package cn.edu.neusoft.ypq.gowuu.user.bean;

/**
 * 作者:颜培琦
 * 时间:2022/3/4
 * 功能:User
 */
public class User {
    private Integer uid;
    private String username;
    private String password;
    private String phone;
    private String email;
    private Integer gender;
    private String avatar;
    private Integer permission;

    public User() {
    }

    public User(Integer uid, String username, String password, String phone, String email, Integer gender, String avatar, Integer permission) {
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.avatar = avatar;
        this.permission = permission;
    }

    public void setUser(User user) {
        this.uid = user.getUid();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.phone = user.getPhone();
        this.email = user.getEmail();
        this.gender = user.getGender();
        this.avatar = user.getAvatar();
        this.permission = user.getPermission();
    }

    public void clearUser(){
        this.uid = null;
        this.username = null;
        this.phone = null;
        this.email = null;
        this.gender = null;
        this.avatar = null;
        this.permission = null;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getGender() {
        return gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getPermission() {
        return permission;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public void setPermission(Integer permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", username='" + username + '\'' +
                ", phone=" + phone +
                ", email='" + email + '\'' +
                ", gender=" + gender +
                ", avatar='" + avatar + '\'' +
                ", permission=" + permission +
                '}';
    }
}
