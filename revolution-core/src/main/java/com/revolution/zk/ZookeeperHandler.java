package com.revolution.zk;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.zookeeper.Watcher.Event.EventType;
import org.slf4j.Logger;

/**
 * 封装Zookeeper的事件，关注节点的最终状态
 *
 * @author jingyun.zou@renren-inc.com
 */
public abstract class ZookeeperHandler {

	public static final Logger LOG = ZookeeperWatcher.LOG;

	/**
	 * 注意type可能与exist状态不同
	 * 初次绑定，type == null
	 */
	public void nodeChanged(String path, boolean exist, EventType type) throws Exception  {
		if (exist)
			nodeCreated(path);
		else
			nodeDeleted(path);
	}

	protected void nodeCreated(String path) throws Exception  {}

	protected void nodeDeleted(String path) throws Exception  {}

	/**
	 * 1. connection建立时，节初始状态
	 * 2. 节点创建时的初始状态
	 * 3. 数据改变
	 * 注意  session expire & delete 不调用
	 */
	public void nodeDataChanged(String path, byte[] data) throws Exception {}

	public void nodeChildrenChanged(String path, List<String> children) throws Exception {}

	public void error(String path, EventType type, Exception ex) {
		LOG.error("event handle error: {}", ex.getMessage());
	}

	public void sessionInit() {}
	public void sessionExpired() {}

	public boolean autoWatch(String path, EventType type) {
		return watchedEvents(path).contains(type);
	}
	protected Set<EventType> watchedEvents(String path) {

		return Collections.emptySet();
	}
}
