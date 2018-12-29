package pool;

import Util.DBUtils;
import Util.JdbcEnum;
import Util.PropertiesUtil;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

/**
 * @ClassName JdbcConnectionPool
 * @Description TODO
 * @Author Administrator
 * @Date 2018/12/11 18:22
 */
public class JdbcConnectionPool {
	private static Properties properties = PropertiesUtil.loadProperties();
	private static BasicDataSource ds = new BasicDataSource();

	static {
		ds.setDriverClassName(properties.getProperty(JdbcEnum.JDBC_DRIVER_NAME.getKey()));
		ds.setUrl(properties.getProperty(JdbcEnum.JDBC_URL.getKey()));
		ds.setUsername(properties.getProperty(JdbcEnum.JDBC_USERNAME.getKey()));
		ds.setPassword(properties.getProperty(JdbcEnum.JDBC_PASSWORD.getKey()));
		ds.setInitialSize(Integer.parseInt(properties.getProperty(JdbcEnum.JDBC_INITSIZE.getKey())));
		ds.setMaxIdle(Integer.parseInt(properties.getProperty(JdbcEnum.jdbc_maxidle.getKey())));
	}

	/**
	 * 获取连接
	 *
	 * @return
	 */
	public static Connection getConnection() {
		Connection connection = null;
		try {
			connection = ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	/**
	 * 连接池的操作
	 * @param args
	 */
	public static void main(String[] args) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int result = 0;
		try {
			connection = getConnection();
			String sql = "insert into t_user(user_name,password,sex) values(?,?,?)";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1,"周杰伦");
			preparedStatement.setInt(2,1231231);
			preparedStatement.setString(3,"0");
			result = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtils.closeAll(preparedStatement,connection,ds);
		}
		System.out.println(result >=1 ?"添加成功":"添加失败");
	}


}
