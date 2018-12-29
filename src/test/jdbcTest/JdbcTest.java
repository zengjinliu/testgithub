package jdbcTest;

import Util.DBUtils;
import bean.User;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName JdbcTest
 * @Description TODO
 * @Author Administrator
 * @Date 2018/12/11 14:25
 */
public class JdbcTest {
	@Test
	public void insert(){
		User user = new User();
		user.setUserName("王五");
		user.setPassword(123456);
		user.setSex("1");
		String sql = "insert into t_user(user_name,password,sex) values(?,?,?)";
		Map<Integer,Object> map = new HashMap<>();
		map.put(1,"userName");
		map.put(2,"password");
		map.put(3,"sex");
		int result = DBUtils.executeUpdate(sql, user, map);
		System.out.println(result >=1?"添加成功":"添加失败");

	}
	@Test
	public void findById(){
		String sql = "select * from t_user where id = ?";
		User user = DBUtils.quseryForObject(sql, User.class, 1);
		System.out.println(user);

	}
}
