package com.thrift.rpc.demo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

//服务端启动
public class Server1 {

	public static void main(String[] args) {
		try {
			new ClassPathXmlApplicationContext("classpath:spring-context-thrift-server1.xml");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
