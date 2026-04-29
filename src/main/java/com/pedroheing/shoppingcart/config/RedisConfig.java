    package com.pedroheing.shoppingcart.config;

    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.data.redis.connection.RedisConnectionFactory;
    import org.springframework.data.redis.core.RedisTemplate;
    import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
    import org.springframework.data.redis.serializer.StringRedisSerializer;
    import tools.jackson.databind.DefaultTyping;
    import tools.jackson.databind.ObjectMapper;
    import tools.jackson.databind.json.JsonMapper;
    import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

    @Configuration
    public class RedisConfig {

        @Bean
        public RedisTemplate<String, Object> redisTemplate(
                RedisConnectionFactory connectionFactory
        ) {
            ObjectMapper redisMapper = JsonMapper.builder()
                    .activateDefaultTyping(
                            BasicPolymorphicTypeValidator.builder()
                                    .allowIfSubType(Object.class)
                                    .build(),
                            DefaultTyping.NON_FINAL
                    )
                    .build();

            RedisTemplate<String, Object> template = new RedisTemplate<>();
            template.setConnectionFactory(connectionFactory);

            var keySerializer = new StringRedisSerializer();
            var valueSerializer = new GenericJacksonJsonRedisSerializer(redisMapper );

            template.setKeySerializer(keySerializer);
            template.setHashKeySerializer(keySerializer);
            template.setValueSerializer(valueSerializer);
            template.setHashValueSerializer(valueSerializer);

            template.afterPropertiesSet();
            return template;
        }
    }