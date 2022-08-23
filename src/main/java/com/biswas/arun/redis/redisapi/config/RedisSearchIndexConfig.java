package com.biswas.arun.redis.redisapi.config;

import com.biswas.arun.redis.redisapi.dto.Customer;
import com.redislabs.lettusearch.CreateOptions;
import com.redislabs.lettusearch.Field;
import com.redislabs.lettusearch.RediSearchCommands;
import com.redislabs.lettusearch.StatefulRediSearchConnection;
import io.lettuce.core.RedisCommandExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RedisSearchIndexConfig implements CommandLineRunner {
    @Autowired
    private StatefulRediSearchConnection<String, String> searchConnection;

    @Value("${app.customerSearchIndexName}")
    private String searchIndexName;

    @Override
    public void run(String... args) throws Exception {
        RediSearchCommands<String, String> commands = searchConnection.sync();
        try {
            commands.ftInfo(searchIndexName);
        } catch (RedisCommandExecutionException rcee) {
            if (rcee.getMessage().equals("Unknown Index name")) {

                CreateOptions<String, String> options = CreateOptions.<String, String>builder()//
                        .prefix(String.format("%s:", Customer.class.getName())).build();

                Field<String> id = Field.numeric("id").sortable(true).build();
                Field<String> fullName = Field.text("fullName").sortable(true).build();
                Field<String> email = Field.text("email").sortable(true).build();
                Field<String> mobile = Field.text("mobile").sortable(true).build();
                Field<String> address = Field.text("address").build();
                Field<String> registeredOn = Field.numeric("registeredOn").sortable(true).build();
                Field<String> updateAt = Field.numeric("updateAt").sortable(true).build();

                commands.create(
                        searchIndexName,
                        options,
                        id, fullName, email,
                        mobile, address, registeredOn, updateAt
                );

                log.info(">>>> Created Customer Search Index...");
            }
        }

    }
}
