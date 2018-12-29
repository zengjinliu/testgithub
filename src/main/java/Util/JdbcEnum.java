package Util;

import lombok.Data;
import lombok.Getter;

/**
 * jdbc配置文件枚举
 */
@Getter
public enum JdbcEnum {
	JDBC_DRIVER_NAME("jdbc.driver.name"),

	JDBC_URL("jdbc.url"),

	JDBC_USERNAME("jdbc.username"),

	JDBC_PASSWORD("jdbc.password"),

	JDBC_INITSIZE("initSize"),

	jdbc_maxactive("maxActive"),

	jdbc_maxidle("maxIdle"),
	;

	private String key;

	JdbcEnum(String key) {
		this.key = key;
	}
}
