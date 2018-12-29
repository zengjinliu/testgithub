package jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

/**
 * @ClassName JedisTest
 * @Description Redis 简单事物测试
 * @Author Administrator
 * @Date 2018/12/12 11:14
 */
public class JedisTest {
	public static void main(String[] args) {
		Jedis jedis = new Jedis("127.0.0.1", 6379);

//		JedisPool jedisPool = new JedisPool("127.0.0.1", 6379);
//		Jedis jedis = jedisPool.getResource();



		int balance;
		int debt;
		int amount = 10;

		jedis.watch("balance");
		balance = Integer.parseInt(jedis.get("balance"));
		jedis.set("balance","40");
		if (amount > balance) {
			jedis.unwatch();
			System.out.println("数据被修改");
		} else {
			//开启事务
			Transaction transaction = jedis.multi();

			transaction.decrBy("balance", amount);
			transaction.incrBy("debt", amount);
			transaction.exec();

			balance = Integer.parseInt(jedis.get("balance"));
			debt = Integer.parseInt(jedis.get("debt"));
			System.out.println(balance + "," + debt);
			jedis.close();
		}
	}
}
