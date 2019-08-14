package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.util.HtmlUtils;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Controller
public class GreetingController {
	
	
	@Autowired
    private SimpMessagingTemplate template;
	
	//前台客户端发到后台订阅主题后半部分的地址
    @MessageMapping("/hello")
    //发送到前台客户端stompClient.subscribe方法里写的订阅的地址
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
    	//参数是个实体
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }
  //前台客户端发到后台订阅主题后半部分的地址
    @MessageMapping("/hello1")
  //发送到前台客户端stompClient.subscribe方法里写的订阅的地址
    @SendTo("/topic/greetings")
    public Greeting greeting1(Message message) throws Exception {
    	//参数是个Message类,有分别获取头和内容实体的方法
    	byte[] bytes=(byte[])message.getPayload();
    	String str=new String(bytes);
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(str) + "!");
    }
    //依靠注入的SimpMessagingTemplate,就可以通过后台方法调用,往前台订阅了"/topic/greetings"的所有客户端发送消息
    @GetMapping("/serversendtoall")
    public void serversendmsgtoall(){
    	//依靠注入的SimpMessagingTemplate,就可以通过后台方法调用,往前台订阅了"/topic/greetings"的所有客户端发送消息
    	template.convertAndSend("/topic/greetings", "8888888888");
    }
  //依靠注入的SimpMessagingTemplate,就可以通过后台方法调用,往前台订阅了"/topic/greetings"的某一个客户端发送消息
    @GetMapping("/serversendtouser")
    public void serversendmsgtouser(){
    	/*
    	 * 底层调用其实是发给所有人,只是convertAndSendToUser方法的参数会和后面订阅的主题
    	 * 合并成一个"/user/liye/greetings"地址
    	 *  stompClient.subscribe('/user/liye/greetings/', function (greeting){})
    	 *  地址开头的"/user"是底层代码默认加上的
    	*/
    	template.convertAndSendToUser("liye", "/greetings/", "9999999999");
    }
}

