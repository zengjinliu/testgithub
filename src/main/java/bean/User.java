package bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName User
 * @Description TODO
 * @Author Administrator
 * @Date 2018/12/11 14:26
 */

public class User implements Serializable {
	private static final long serialVersionUID = -3892776122354947578L;

	private int id;

	private String userName;

	private int password;

	private String sex;

	public User() {
	}

	public User(int id, String userName, int password, String sex) {
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.sex = sex;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", userName='" + userName + '\'' +
				", password=" + password +
				", sex='" + sex + '\'' +
				'}';
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getPassword() {
		return password;
	}

	public void setPassword(int password) {
		this.password = password;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
}
