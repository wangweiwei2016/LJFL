package com.aimowei.model;

import java.io.Serializable;

public class User   implements Serializable {
	private String uid;
	private String login;

	private String login_salt;

	private String uname;

	private String email;

	private String sex;
	private String birthday;

	private String birthdaym;

	private String birthdayd;

	private String location;

	private String is_audit;

	private String is_active;

	private String is_init;

	private String ctime;

	private String city;
	private String area;
	private String identity;
	private String feed_email_time;
	private String send_email_time;
	private String avatar_original;
	private String avatar_big;
	private String avatar_middle;

	private String avatar_small;
	private String avatar_tiny;

	private String avatar_url;
	private String user_intro_status_name;

	private String space_url;
	private String space_link_no;
	private String group_icon;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getLogin_salt() {
		return login_salt;
	}

	public void setLogin_salt(String login_salt) {
		this.login_salt = login_salt;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getBirthdaym() {
		return birthdaym;
	}

	public void setBirthdaym(String birthdaym) {
		this.birthdaym = birthdaym;
	}

	public String getBirthdayd() {
		return birthdayd;
	}

	public void setBirthdayd(String birthdayd) {
		this.birthdayd = birthdayd;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getIs_audit() {
		return is_audit;
	}

	public void setIs_audit(String is_audit) {
		this.is_audit = is_audit;
	}

	public String getIs_active() {
		return is_active;
	}

	public void setIs_active(String is_active) {
		this.is_active = is_active;
	}

	public String getIs_init() {
		return is_init;
	}

	public void setIs_init(String is_init) {
		this.is_init = is_init;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getFeed_email_time() {
		return feed_email_time;
	}

	public void setFeed_email_time(String feed_email_time) {
		this.feed_email_time = feed_email_time;
	}

	public String getSend_email_time() {
		return send_email_time;
	}

	public void setSend_email_time(String send_email_time) {
		this.send_email_time = send_email_time;
	}

	public String getAvatar_original() {
		return avatar_original;
	}

	public void setAvatar_original(String avatar_original) {
		this.avatar_original = avatar_original;
	}

	public String getAvatar_big() {
		return avatar_big;
	}

	public void setAvatar_big(String avatar_big) {
		this.avatar_big = avatar_big;
	}

	public String getAvatar_middle() {
		return avatar_middle;
	}

	public void setAvatar_middle(String avatar_middle) {
		this.avatar_middle = avatar_middle;
	}

	public String getAvatar_small() {
		return avatar_small;
	}

	public void setAvatar_small(String avatar_small) {
		this.avatar_small = avatar_small;
	}

	public String getAvatar_tiny() {
		return avatar_tiny;
	}

	public void setAvatar_tiny(String avatar_tiny) {
		this.avatar_tiny = avatar_tiny;
	}

	public String getAvatar_url() {
		return avatar_url;
	}

	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}

	public String getUser_intro_status_name() {
		return user_intro_status_name;
	}

	public void setUser_intro_status_name(String user_intro_status_name) {
		this.user_intro_status_name = user_intro_status_name;
	}

	public String getSpace_url() {
		return space_url;
	}

	public void setSpace_url(String space_url) {
		this.space_url = space_url;
	}

	public String getSpace_link_no() {
		return space_link_no;
	}

	public void setSpace_link_no(String space_link_no) {
		this.space_link_no = space_link_no;
	}

	public String getGroup_icon() {
		return group_icon;
	}

	public void setGroup_icon(String group_icon) {
		this.group_icon = group_icon;
	}

	public User() {

	}

	public User(String uid, String login, String login_salt, String uname, String email, String sex, String birthday, String birthdaym, String birthdayd, String location,
			String is_audit, String is_active, String is_init, String ctime, String city, String area, String identity, String feed_email_time, String send_email_time,
			String avatar_original, String avatar_big, String avatar_middle, String avatar_small, String avatar_tiny, String avatar_url, String user_intro_status_name,
			String space_url, String space_link_no, String group_icon) {
		super();
		this.uid = uid;
		this.login = login;
		this.login_salt = login_salt;
		this.uname = uname;
		this.email = email;
		this.sex = sex;
		this.birthday = birthday;
		this.birthdaym = birthdaym;
		this.birthdayd = birthdayd;
		this.location = location;
		this.is_audit = is_audit;
		this.is_active = is_active;
		this.is_init = is_init;
		this.ctime = ctime;
		this.city = city;
		this.area = area;
		this.identity = identity;
		this.feed_email_time = feed_email_time;
		this.send_email_time = send_email_time;
		this.avatar_original = avatar_original;
		this.avatar_big = avatar_big;
		this.avatar_middle = avatar_middle;
		this.avatar_small = avatar_small;
		this.avatar_tiny = avatar_tiny;
		this.avatar_url = avatar_url;
		this.user_intro_status_name = user_intro_status_name;
		this.space_url = space_url;
		this.space_link_no = space_link_no;
		this.group_icon = group_icon;
	}

}