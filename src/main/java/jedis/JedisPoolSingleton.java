package jedis;

/**
 * @ClassName JedisPoolCode
 * @Description TODO
 * @Author Administrator
 * @Date 2018/12/13 12:17
 */
public class JedisPoolSingleton {
	private static class Singleton{
		private static final JedisPoolSingleton INSTANCE = new JedisPoolSingleton();
	}
	private JedisPoolSingleton() {
	}
	public JedisPoolSingleton getInstance(){
		return Singleton.INSTANCE;
	}
}
