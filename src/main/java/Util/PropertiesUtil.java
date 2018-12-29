package Util;

import javax.rmi.CORBA.Util;
import java.io.*;
import java.util.Properties;

/**
 * @ClassName PropertiesUtil
 * @Description TODO
 * @Author Administrator
 * @Date 2018/12/11 9:27
 */
public class PropertiesUtil {
	private static Properties properties = null;

	private PropertiesUtil(){

	}

	/**
	 *读取文件
	 * @param configPath
	 * @return
	 */
	public static Properties loadProperties(String configPath){
		properties = new Properties();
		try {
			properties.load(readConfigFile(configPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

	/**
	 * 没有给定路径就是用默认路径
	 * @return
	 */
	public static Properties loadProperties(){
		properties = new Properties();
		try {
			properties.load(readConfigFile(new File("").getAbsolutePath()+"/src/main/java/conf/db.properties"));
		} catch (IOException e) {
			throw new RuntimeException("加载配置文件失败");
		}
		return properties;
	}

	/**
	 * 获取流
	 * @param configPath 文件位置
	 * @return
	 */
	public static InputStream readConfigFile(String configPath){

		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(configPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return inputStream;
	}

	public static void main(String[] args) {
		String driverName = PropertiesUtil.loadProperties().getProperty("jdbc.driver.name");
		System.out.println(driverName);

	}

}
