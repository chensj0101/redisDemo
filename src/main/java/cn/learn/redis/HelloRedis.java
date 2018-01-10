package cn.learn.redis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class HelloRedis {

	static Jedis jedis;
//	public static void main(String[] args) {
//		Jedis jedis = new Jedis("localhost");
//		System.out.println("连接redis服务器");
//		System.out.println("连接成功："+ jedis.ping());
//	}
	@BeforeClass
	public static void getJedis() {
		jedis = new Jedis("localhost");
	}
	@Test
	public void testString() {
		jedis.set("str:key:01", "title");
		System.out.println("test: "+ jedis.get("str:key:01"));
	}
	@Test
	public void testList() {
		jedis.lpush("list:01", "title");
		jedis.lpush("list:01", "author");
		jedis.lpush("list:01", "context");
		List<String> list = jedis.lrange("list:01", 0,10);
//		System.out.println("testResult: "+jedis.lrange("list:01", 0 ,10));
		for(String str: list) {
			System.out.println("testResult: " + str);
		}
	}
	@Test
	public void testKey() {
		Set<String> keys = jedis.keys("*");
        for(String k: keys) {
        	System.out.println("testResult: "  + k);
        }
	}
	@Test
	public void testHash() {
		for(int i=0; i<10;i++) {
			jedis.hset("hash:01", "field:"+i, "value:"+i);
		}
		Map<String, String> map = jedis.hgetAll("hash:01");
		Set<Entry<String,String>> entrySet = map.entrySet();
		for(Entry<String,String> es: entrySet) {
			System.out.println(es.getKey()+"-----------------"+es.getValue());
		}
	}
	@Test
	public void testSet() {
		String[] str = new String[] {"a","b","c","d"};
		jedis.sadd("set:01", str);
		Set<String> setstr = jedis.smembers("set:01");
		for(String string: setstr) {
			System.out.println("testResult: " +string);
		}
	}
	@Test
	public void testZSet() {
		String key = "zset_key";
		Map<String,Double> scoreMembers=new HashMap<String, Double>();
		scoreMembers.put("member01", 1.0);
		scoreMembers.put("member02", 2.0);
		jedis.zadd(key, scoreMembers);
		
		Set<String> zrange = jedis.zrange(key, 0, 2);
		
		for (String member : zrange) {
			System.out.println(member);
			System.out.println(jedis.zscore(key, member));
		}
	}
	@Test
	public void testSubscribe() {
		JedisPubSub jedisPubSub = new JedisPubSub() {
			
			@Override
			public void onMessage(String channel, String message) {
				System.out.println(channel);
				System.out.println(message);
			}

			@Override
			public void onPMessage(String arg0, String arg1, String arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPSubscribe(String arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPUnsubscribe(String arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSubscribe(String arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onUnsubscribe(String arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		};
		jedis.subscribe(jedisPubSub, "channel1");
	}
	
	@Test
	public void testPublish() {
		String channel = "channel1";
		String message = "third time to publish a content";
		jedis.publish(channel, message);
	}
}
