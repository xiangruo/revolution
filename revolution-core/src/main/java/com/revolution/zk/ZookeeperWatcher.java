package com.revolution.zk;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 封装ZK Watcher，自动持续设置watch
 *
 * 若节点不存在，支持延迟绑定datachange等事件
 *
 * @author jingyun.zou@renren-inc.com
 */
public class ZookeeperWatcher implements Watcher {

	public static final Logger LOG = LoggerFactory.getLogger(ZookeeperWatcher.class);

	protected ZooKeeperManager zk;
	protected Map<String, ZookeeperHandler> handlers;

	/**
	 * 注册Handler, 若节点不存在，则延迟绑定
	 */
	public void register(String path, ZookeeperHandler handler) {
		if (handler == null || path == null)
			throw new IllegalArgumentException();

		zk.connect(); // init
		handlers.put(path, handler);
		initHandler(path, handler);
	}

	/**
	 * SessionExpire 之后，重新绑定
	 */
	void initHandler() {
		for (Map.Entry<String, ZookeeperHandler> entry : handlers.entrySet()) {
			initHandler(entry.getKey(), entry.getValue());
		}
	}

	private void initHandler(String path, ZookeeperHandler handler) {
		try {
			handleExist(path, null, true);
			handler.sessionInit();
		} catch (Exception e) {
			handler.error(path, null, e);
		}
	}

	/**
	 * 自动重新绑定Handler
	 */
	@Override
	public void process(WatchedEvent event) {
		String path = event.getPath();
		EventType type = event.getType();

		if (path == null || type == EventType.None) {
			zk.process(event);
			return;
		}

		ZookeeperHandler handler = handlers.get(path);
		if (handler == null)
			return;

		try {
			switch (type) {
			// Created 或 Deleted 都需要 watch exist
			case NodeCreated:
			case NodeDeleted:
				handleExist(path, type, false);
				break;
			case NodeDataChanged:
			case NodeChildrenChanged:
				handleEvent(path, type);
				break;
			default:
				break;
			}
		} catch (Exception ex) {
			handler.error(path, type, ex);
		}
	}

	/**
	 * 1. 查询节点初始状态
	 * 2. 节点添加或删除
	 * 若exist=true, 则查询节点的data&children
	 */
	private void handleExist(String path, EventType type, boolean watch)
			throws Exception {
		ZookeeperHandler handler = handlers.get(path);

		/**
		 * 1. 注册Handler时强制绑定
		 * 2. 添加或删除，都要关注exist事件
		 */
		watch = watch || !handler.watchedEvents(path).isEmpty();

		boolean exist = zk.exists(path, watch);
		handler.nodeChanged(path, exist, type);

		if (exist) {
			handleEvent(path, EventType.NodeDataChanged);
			handleEvent(path, EventType.NodeChildrenChanged);
		}
	}

	private void handleEvent(String path, EventType type) {
		ZookeeperHandler handler = handlers.get(path);
		if (handler == null)
			throw new RuntimeException("Register Listener First");
		boolean watch = handler.autoWatch(path, type);

		try {
			switch (type) {
			case NodeDataChanged:
				byte[] data = zk.getData(path, watch);
				handler.nodeDataChanged(path, data);
				break;
			case NodeChildrenChanged:
				List<String> children = zk.getChildren(path, watch);
				handler.nodeChildrenChanged(path, children);
				break;
			default:
				break;
			}
		} catch (KeeperException.NoNodeException e) {
			LOG.warn("node not exist: {}", path);
		} catch (Exception ex) {
			handler.error(path, type, ex);
			return;
		}
	}

	ZookeeperWatcher(ZooKeeperManager zk) {
		handlers = new Hashtable<String, ZookeeperHandler>();
		this.zk = zk;
	}
}
