package br.edu.pucgo.vacinasocket

import com.fasterxml.jackson.databind.ObjectMapper
import redis.clients.jedis.DefaultJedisClientConfig
import redis.clients.jedis.HostAndPort
import redis.clients.jedis.Jedis
import redis.clients.jedis.Protocol

@Singleton(strict = false)
class ConfigService {

	private final ObjectMapper objectMapper = new ObjectMapper()
	private final Jedis jedis

	private ConfigService() {
		File redisConfFile = new File('.', 'redis.properties')
		Properties properties = new Properties()
		properties.load(redisConfFile.newInputStream())
		def jedisConfigBuilder = DefaultJedisClientConfig.builder().password((String) properties.get('password'))
		jedis = new Jedis(new HostAndPort(Protocol.DEFAULT_HOST, Protocol.DEFAULT_PORT), jedisConfigBuilder.build())
	}

	synchronized void putInMap(String key, String field, Object value) {
		jedis.hset(key, field, objectMapper.writeValueAsString(value))
	}

	synchronized void deleteFromMap(String key, String... fields) {
		jedis.hdel(key, fields)
	}

	synchronized Long increment(String key) {
		return jedis.incr(key)
	}

	synchronized Map<String, String> getMap(String key) {
		return jedis.hgetAll(key)
	}

	synchronized <T> T getMapField(String key, String field, Class<T> clazz = String) {
		final String value = jedis.hget(key, field)
		return parseJson(value, clazz)
	}

	synchronized <T> T getProperty(String key, Class<T> clazz = String) {
		final String value = jedis.get(key)
		return parseJson(value, clazz)
	}

	private <T> T parseJson(String value, Class<T> clazz) {
		if (!value || value == 'null') {
			return null
		}
		return objectMapper.readValue(value, clazz)
	}

	synchronized void setProperty(String key, Object value) {
		final String json = objectMapper.writeValueAsString(value)
		jedis.set(key, json)
	}

}
