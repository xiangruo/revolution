package com.revolution.zk;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.revolution.io.Factory;
import com.revolution.io.Writable;
import com.revolution.util.ZkUtil;

/**
 * 服务节点群组，EPHEMERAL模式。 服务启动时addNode注册节点。
 *
 * 节点发生变更时调用nodeChnage() children为当前有效的服务节点
 *
 * @author jingyun.zou@renren-inc.com
 */
public abstract class ZookeeperGroup<T extends Writable> extends ZookeeperHandler implements InitializingBean {

	public static final Logger LOG = LoggerFactory.getLogger(ZookeeperGroup.class);

	private String root;

	private Factory<T> factory;

	protected Map<String, T> children = new HashMap<String, T>();

	protected ZookeeperHandler nodeHandler = new NodeHandler();

	protected ArrayList<ACL> acl = Ids.OPEN_ACL_UNSAFE;

	/**
	 * init rootpath
	 */
	protected ZookeeperGroup(String rootpath, Factory<T> factory) {
		this.root = rootpath;
		this.factory = factory;
	}

	@Override
	public void afterPropertiesSet() {
		try {
			root = ZooKeeperManager.getInstance().create(root, null, acl);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		ZooKeeperManager.getInstance().watch(root, this);
	}

	abstract protected void nodeChnage(String node);

	protected String addNode(String node, T data) throws KeeperException, InterruptedException {
		String path = ZkUtil.generatePath(root, node);

		ZooKeeper zk = ZooKeeperManager.getInstance().getZooKeeper();
		path = zk.create(path, factory.getBytes(data), acl, CreateMode.EPHEMERAL);
		return path;
	}

	protected void delNode(String node) throws KeeperException, InterruptedException {
		ZooKeeperManager.getInstance().delete(ZkUtil.generatePath(root, node));
	}

	private Set<String> watched = new HashSet<String>();

	/**
	 * 监控产生的新子节点, 由子节点维护自己的状态
	 */
	@Override
	public synchronized void nodeChildrenChanged(String rootpath, List<String> children) {
		if (nodeHandler == null || factory == null)
			return;

		if (children == null || children.size() == 0)
			return;

		Set<String> newNodes = new HashSet<String>(children);
		newNodes.removeAll(watched);
		for (String child : newNodes) {
			ZookeeperWatcher.LOG.debug("watch Node {}", child);
			String path = ZkUtil.generatePath(root, child);

			ZooKeeperManager.getInstance().watch(path, nodeHandler);
		}
		watched.addAll(newNodes);
	}

	/**
	 * expire 期间会丢失event watch
	 */
	@Override
	public void sessionExpired() {
		ZookeeperWatcher.LOG.debug("Session expire, clear nodes");
		children.clear();
	}

	@Override
	protected Set<EventType> watchedEvents(String path) {
		return EnumSet.of(EventType.NodeChildrenChanged);
	}

	public class NodeHandler extends ZookeeperHandler {
		@Override
		protected synchronized void nodeDeleted(String path) throws Exception {
			String node = ZkUtil.basename(path);
			LOG.debug("Node {} Deleted", node);

			if (children.containsKey(node)) {
				children.remove(node);
				nodeChnage(node); // TODO 传入 old
			}
		}

		@Override
		public synchronized void nodeDataChanged(String path, byte[] data) throws Exception {
			String node = ZkUtil.basename(path);
			T object = data == null ? null : factory.readBytes(data);

			T old = children.get(node);
			if (old != null) {
				if (old.equals(object))
					return;
			}
			else if (children.containsKey(node)) {
				if (object == null)
					return;
			}
			else {
				LOG.debug("Node {} Created", node);
			}
			LOG.debug("Node {} Data Changed : {} ", node, object);

			children.put(node, object);
			nodeChnage(node);
		}

		@Override
		protected Set<EventType> watchedEvents(String path) {
			return EnumSet.of(EventType.NodeDeleted, EventType.NodeDataChanged);
		}
	}
}
