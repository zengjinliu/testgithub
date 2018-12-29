package bean;

import java.io.Serializable;

/**
 * @ClassName Dog
 * @Description TODO
 * @Author Administrator
 * @Date 2018/12/29 9:00
 */
public class Dog implements Serializable {

	private static final long serialVersionUID = 2536570914214785960L;
	private String name;

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	private Integer age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
