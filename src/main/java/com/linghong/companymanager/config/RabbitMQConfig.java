package com.linghong.companymanager.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/12 13:25
 * @Version 1.0
 * @Description:
 */
@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue directQueue() {
        // 第一个参数是队列名字， 第二个参数是指是否持久化
        return new Queue("companyTutor", true);
    }

}
