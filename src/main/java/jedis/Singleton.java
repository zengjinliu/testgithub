package jedis;

/**
 * @ClassName Singleton
 * @Description TODO
 * @Author Administrator
 * @Date 2018/12/13 12:26
 */
public class Singleton {
	/**
	 * 懒汉式
	 */
//	private static Singleton instance;
//	private Singleton() {
//	}
//	public static Singleton getInstance(){
//		if (instance==null){
//			synchronized (Singleton.class){
//				if (instance==null){
//					instance = new Singleton();
//				}
//			}
//		}
//		return instance;
//	}
	/**
	 * 饿汉式
	 */
	private static Singleton instance = new Singleton();

	private Singleton() {
	}
	public static Singleton getInstance(){
		return instance;
	}
}
