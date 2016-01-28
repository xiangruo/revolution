package com.thrift.rpc.demo;

import org.apache.thrift.TException;

//实现类
public class EchoSerivceImpl implements EchoSerivce.Iface {

	private long id = 0l;

	public EchoSerivceImpl() {
		System.out.println(this);
		id = System.currentTimeMillis();
	}

	@Override
	public String echo(String msg) throws TException {
		return "server(" + id + ") :" + msg;
	}
}
