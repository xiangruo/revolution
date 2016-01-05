package com.revolution.redis;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

/**
 * @author xiangruo
 */
@Component
public class RedisClient {

	@Autowired
	private JedisSentinelPool msgPool;

	/**
	 * @param key
	 * @return 此方法使用了锁机制来防止防止缓存过期时所产生的惊群现象，保证只有一个进程不获取数据，可以更新，其他进程仍然获取过期数据
	 */
	public ResultVO getByLock(String key) {
		Jedis jedis = msgPool.getResource();
		Map<String, String> map = jedis.hgetAll(key);
		if (!CollectionUtils.isEmpty(map)) {
			String data = map.get("data");
			long expire = Long.parseLong(map.get("expire"));
			if (expire <= time()) {
				long lock = jedis.incr(key + ".lock");
				if (lock == 1) {
					// 返回false的时候直接去后台取数据。然后更新缓存
					return new ResultVO(false, null);
				}
				// 有一个线程去后台取数据了。剩下的还是取缓存的数据
				return new ResultVO(true, data);
			}
		}
		return new ResultVO(true, null);
	}

	/**
	 * 设置缓存
	 * @param key string name 缓存键
	 * @param value string ,array,object,number,boolean value 缓存值
	 * @param null ttl string ,number ttl 过期时间，如果不设置，则使用默认时间，如果为 infinity 则为永久保存
	 * @return bool
	 * @desc 此方法存储的数据会自动加入一些其他数据来避免惊群现象，如需保存原始数据，请使用 set
	 */
	public boolean setByLock(String key, String value, int ttl) {
		Jedis jedis = msgPool.getResource();
		long exp = 0l;
		Map<String, String> map = new HashMap<String, String>();
		if (ttl > 0) {
			exp = time() + ttl;
		}
		else {
			ttl = 600;
			exp = time() + ttl;
		}
		ttl += 200;// //增加redis缓存时间，使程序有足够的时间生成缓存
		map.put("data", value);
		map.put("expire", exp + "");
		jedis.hmset(key, map);
		jedis.expire(key, ttl);
		jedis.del(key + ".lock");
		return true;
	}

	private long time() {
		return System.currentTimeMillis();
	}

	public static class ResultVO {
		private boolean result;

		private String data;

		public ResultVO(boolean result, String data) {
			setData(data);
			setResult(result);
		}

		public boolean isResult() {
			return result;
		}

		public void setResult(boolean result) {
			this.result = result;
		}

		public String getData() {
			return data;
		}

		public void setData(String data) {
			this.data = data;
		}

	}

}
