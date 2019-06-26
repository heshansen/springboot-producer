package com.kangswx.rabbitmq.springboot;

import com.kangswx.rabbitmq.springboot.domain.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageProducerTest {

    @Resource
    private MessageProducer messageProducer;

    @Test
    public void sendMessageTest(){
        messageProducer.sendMessage(new Employee("3306", "kangswx", 25));
    }

}