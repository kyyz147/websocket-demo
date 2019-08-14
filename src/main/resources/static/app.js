var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
	//写在websocketconfig里的端点地址
    var socket = new SockJS('/duandiandizhi');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        //前台客户端订阅后台服务端发来的消息,controller里@SendTo("/topic/greetings")注解里的地址
        stompClient.subscribe('/topic/greetings', function (greeting) {
        	debugger;
        	var msg=greeting.body;
            showGreeting(JSON.parse(greeting.body).content);
        });
        stompClient.subscribe('/user/liye/greetings/', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
	//地址是后台websocketconfig里写好的订阅主题的地址+controller里加@MessageMapping("/hello")注解的地址
    stompClient.send("/app/hello1", {}, JSON.stringify({'name': $("#name").val()}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});

