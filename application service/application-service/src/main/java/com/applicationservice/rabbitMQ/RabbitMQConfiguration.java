package com.applicationservice.rabbitMQ;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {
    public static final String IMAGE_QUEUE = "image_queue";
    public static  final  String CREDIT_RESULT_QUEUE1 = "credit_result_queue1";
    public  static  final  String LOAN_LIMIT_QUEUE = "loan_limit_queue";
    public static final  String PROGRESS_QUEUE = "progress_queue";

    @Bean
    public Queue queue() {
        return new Queue(IMAGE_QUEUE, true);
    }
    @Bean
    public Queue loanLimitQueue() {
        return new Queue(LOAN_LIMIT_QUEUE, true);
    }
    @Bean
    public Queue creditResult(){
        return new Queue(CREDIT_RESULT_QUEUE1, true);
    }
    @Bean
    public Queue progressQueue() {
        return new Queue(PROGRESS_QUEUE, true);
    }


    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);

        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setTrustedPackages(
                "com.shared.dtos",
                "com.applicationservice.dtos"
        );

        converter.setClassMapper(classMapper);
        return converter;
    }


    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter converter,
            SimpleRabbitListenerContainerFactoryConfigurer configurer) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setMessageConverter(converter);
        configurer.configure(factory, connectionFactory);
        return factory;
    }
}
