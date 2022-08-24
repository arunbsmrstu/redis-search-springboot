package com.biswas.arun.redis.redisapi;

import com.biswas.arun.redis.redisapi.common.payload.Payload;
import com.biswas.arun.redis.redisapi.common.utils.Utils;
import com.google.gson.Gson;
import org.apache.commons.validator.GenericValidator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedisapiApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void test(){
//		System.out.println(GenericValidator.isDate("2019-02-28", "yyyy-MM-dd", true));
//		System.out.println(GenericValidator.isDate("2019-28-02", "yyyy-MM-dd", true));

		Payload payload= Utils.pagination(4,100,10);
		System.out.println(new Gson().toJson(payload));
	}

}
