package Util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;

/**
 * @ClassName DBUtils
 * @Description jdbc工具类
 * @Author Administrator
 * @Date 2018/12/10 16:54
 */
public class DBUtils {
	private static Properties properties = PropertiesUtil.loadProperties();

	/**
	 * 加载驱动
	 */
	static {
		try {
			Class.forName(properties.getProperty(JdbcEnum.JDBC_DRIVER_NAME.getKey()));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 建立连接
	 *
	 * @return
	 */
	public static Connection getConnection() {
		Connection connection = null;
		try {
			//获取连接
			connection = DriverManager.getConnection(
					properties.getProperty(JdbcEnum.JDBC_URL.getKey()),
					properties.getProperty(JdbcEnum.JDBC_USERNAME.getKey()),
					properties.getProperty(JdbcEnum.JDBC_PASSWORD.getKey()));
		} catch (SQLException e) {
			throw new RuntimeException("获取连接失败");
		}
		return connection;
	}

	/**
	 * 更新操作
	 *
	 * @param sql    sql语句
	 * @param object
	 * @param map
	 * @return
	 */
	public static int executeUpdate(String sql, Object object, Map<Integer, Object> map) {
		int result = 0;

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			//获取连接
			connection = getConnection();
			preparedStatement = createPreparedStatement(connection, sql);
			Set<Map.Entry<Integer, Object>> entrySet = map.entrySet();
			//反射获取对象
			Class<?> clazz = object.getClass();
			for (Map.Entry<Integer, Object> entry : entrySet) {
				//获取对象的get方法
				Method method = clazz.getDeclaredMethod("get" + upperCaseFirst(entry.getValue().toString()));
				//调用方法对象
				Object obj = method.invoke(object);
				//设置参数
				preparedStatement.setObject(entry.getKey(), obj);
			}
			result = preparedStatement.executeUpdate();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} finally {
			closeAll(preparedStatement, connection);
		}
		return result;
	}

	/**
	 * 关闭连接等操作
	 *
	 * @param arr
	 * @param <T>
	 */
	public static <T extends AutoCloseable> void closeAll(T... arr) {
		if (arr != null && arr.length > 0) {
			for (T t : arr) {
				if (t != null) {
					try {
						t.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 将首字母变大写
	 *
	 * @param string
	 * @return
	 */
	public static String upperCaseFirst(String string) {
		return string.substring(0, 1).toUpperCase() + string.substring(1);
	}

	/**
	 * 将字符串首字母变小写
	 *
	 * @param string
	 * @return
	 */
	public static String lowerCaseFirst(String string) {
		return string.substring(0, 1).toLowerCase() + string.substring(1);
	}

	/**
	 * 获取preparedStatement
	 *
	 * @param connection
	 * @param sql
	 * @return
	 */
	private static PreparedStatement createPreparedStatement(Connection connection, String sql) {
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return preparedStatement;
	}


	public static <T> T quseryForObject(String sql, Class<T> clazz, Object... objects) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		T t = null;
		try {
			//获取连接
			connection = getConnection();
			//获取statement
			preparedStatement = createPreparedStatement(connection, sql);
			//遍历参数
			if (objects != null && objects.length > 0) {
				for (int i = 0; i < objects.length; i++) {
					//设置位置参数
					preparedStatement.setObject(i + 1, objects[i]);
				}
				resultSet = preparedStatement.executeQuery();
				t = generateObject(resultSet, clazz);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	/**
	 * 生成对象的方法
	 *
	 * @param resultSet
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	private static <T> T generateObject(ResultSet resultSet, Class<T> clazz) {
		T t = null;
		String columnName = null;

		try {
			t = clazz.newInstance();
			//如果resultSet为空，则返回null
			if (!resultSet.next()) {
				return t;
			}
			//获取元数据
			ResultSetMetaData metaData = resultSet.getMetaData();
			int count = metaData.getColumnCount();
			for (int i = 0; i < count; i++) {
				//获取列标签
				columnName = metaData.getColumnLabel(i + 1);
				StringBuffer sb = new StringBuffer();
				//判断字段名是否包含下划线
				if (columnName.contains("_")) {
					//拆分字符串
					String[] split = columnName.split("_");
					for (String s : split) {
						sb.append(upperCaseFirst(s));
					}
					columnName = sb.toString();
				} else {
					//将字段名首字母变大写
					columnName = upperCaseFirst(columnName);
				}

				//获取属性
				Field field = clazz.getDeclaredField(lowerCaseFirst(columnName));
				//调用对象方法
				Method method = clazz.getDeclaredMethod("set" + columnName, field.getType());
				//执行方法
				method.invoke(t, resultSet.getObject(i + 1));
			}

		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return t;
	}


	public static <T> List<T> quseryForList(String sql, Class<T> clazz, Object... objects) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<T> ts = null;
		T t = null;
		try {
			//获取连接
			connection = getConnection();
			//获取statement
			preparedStatement = createPreparedStatement(connection, sql);
			//遍历参数
			if (objects != null && objects.length > 0) {
				for (int i = 0; i < objects.length; i++) {
					//设置位置参数
					preparedStatement.setObject(i + 1, objects[i]);
				}
				resultSet = preparedStatement.executeQuery();
				ts = generateList(resultSet, clazz);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ts;
	}


	private static <T> List<T> generateList(ResultSet resultSet, Class<T> clazz) {
		T t = null;
		String columnName = null;
		List<T> ts = new ArrayList<>();
		try {

			//如果resultSet为空，则返回null
			while (resultSet.next()) {
				t = clazz.newInstance();
				ts.add(t);
				//获取元数据
				ResultSetMetaData metaData = resultSet.getMetaData();
				int count = metaData.getColumnCount();
				for (int i = 0; i < count; i++) {
					//获取列标签
					columnName = metaData.getColumnLabel(i + 1);
					StringBuffer sb = new StringBuffer();
					//判断字段名是否包含下划线
					if (columnName.contains("_")) {
						//拆分字符串
						String[] split = columnName.split("_");
						for (String s : split) {
							sb.append(upperCaseFirst(s));
						}
						columnName = sb.toString();
					} else {
						//将字段名首字母变大写
						columnName = upperCaseFirst(columnName);
					}

					//获取属性
					Field field = clazz.getDeclaredField(lowerCaseFirst(columnName));
					//调用对象方法
					Method method = clazz.getDeclaredMethod("set" + columnName, field.getType());
					//执行方法
					method.invoke(t, resultSet.getObject(i + 1));
				}

			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ts;
	}


}
