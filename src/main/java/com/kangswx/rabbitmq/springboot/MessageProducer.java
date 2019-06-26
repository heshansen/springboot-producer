package com.kangswx.rabbitmq.springboot;

import com.kangswx.rabbitmq.springboot.domain.Employee;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        // 第一个参数， 消息的附加消息(自定义id)，
        // 第二个参数， 消息是否被Broker接收，isAck(true接收,false拒收)，
        // 第三个参数， 如果拒收，则返回拒收的原因
        @Override
        public void confirm(CorrelationData correlationData, boolean isAck, String s) {
            System.out.println("======》correlationData: " + correlationData);
            System.out.println("======》isAck: " + isAck);
            if(!isAck){
                System.err.println("======》s: "+s);
            }
        }
    };

    RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {

        // 第一个参数， 被退回的消息
        // 第二个参数， 错误编码
        // 第三个参数， 错误描述
        // 第四个参数， 交换机的名字
        // 第五个参数， 路由Key
        @Override
        public void returnedMessage(Message message, int replyCode, String replyText, String exchangeName, String routingKey) {
            System.err.println("-----returnedMessage++code: " + replyCode +",  Text: " + replyText);
            System.err.println("-----returnedMessage++exchangeName: " + exchangeName +",  routingKey: " + routingKey);
            System.err.println("-----returnedMessage++message: " + message);
        }
    };


    public void sendMessage(Employee employee){
        //CorrelationData作用是作为消息的附加消息传递，通常我们用它来保存消息的自定义id
        CorrelationData correlationData = new CorrelationData(employee.getEmpno()+"-"+new Date().getTime());
        //交换机名称, 路由key，消息的对象， 自定义id
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        rabbitTemplate.convertAndSend("springboot-exchange", "hr.employee", employee, correlationData);
    }


}
