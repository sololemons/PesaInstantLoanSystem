package com.verification.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String IMAGE_QUEUE = "image_queue";
    public static final String CRB_QUEUE = "crb_queue";
    public static final String CREDIT_SCORING_EXCHANGE = "creditScoringExchange";
    public static final String CREDIT_SCORING_QUEUE = "credit_scoring_queue";
    public static final String CREDIT_RESULT_QUEUE1 = "credit_result_queue1";
    public static final String CREDIT_RESULT_QUEUE2 = "credit_result_queue2";
    public static final String LOAN_LIMIT_QUEUE = "loan_limit_queue";


    @Bean
    public FanoutExchange creditScoringExchange() {
        return new FanoutExchange(CREDIT_SCORING_EXCHANGE);
    }
    @Bean
    public Queue imageQueue() {
        return new Queue(IMAGE_QUEUE, true);
    }
    @Bean
    public Queue loanLimitQueue() {
        return new Queue(LOAN_LIMIT_QUEUE, true);
    }


    @Bean
    public Queue crbQueue() {
        return new Queue(CRB_QUEUE, true);
    }

    @Bean
    public Queue creditScoringQueue() {
        return new Queue(CREDIT_SCORING_QUEUE, true);
    }
    @Bean
    public Queue creditResultQueue1() {
        return new Queue(CREDIT_RESULT_QUEUE1, true);
    }
    @Bean
    public Queue creditResultQueue2() {
        return new Queue(CREDIT_RESULT_QUEUE2, true);
    }
    @Bean
    public Binding bindCreditResultQueue1(FanoutExchange exchange, Queue creditResultQueue1) {
        return BindingBuilder.bind(creditResultQueue1).to(exchange);
    }

    @Bean
    public Binding bindCreditResultQueue2(FanoutExchange exchange, Queue creditResultQueue2) {
        return BindingBuilder.bind(creditResultQueue2).to(exchange);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);

        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setTrustedPackages(
                "com.shared.dtos",
                "com.verification.dtos"
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
