package hello;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        //允许客户端订阅的主题,是以"/topic"和"/user"开头的,有了这个方法,后台服务器端才能往前台客户端发送消息
    	config.enableSimpleBroker("/topic","/user");
        //服务器端订阅一个从前台客户端发送来的主题,去controller里找一个@MessageMapping("/hello")注解里,带后缀的方法
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
    	//下面的地址,就是写在页面上js文件的var socket = new SockJS('/duandiandizhi');这个地址
    	//后面的.withSockJS()是为了支持stomp连接
        registry.addEndpoint("/duandiandizhi").withSockJS();
    }

}