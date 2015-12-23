package com.revolution.zk;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revolution.spring.ZookeeperClient;
import com.revolution.util.ZkUtil;

public class ZooKeeperManager extends ZookeeperClient implements Watcher {

	static final Logger LOG = LoggerFactory.getLogger(ZooKeeperManager.class);

	private static ZooKeeperManager INSTANCE;

	public static ZooKeeperManager getInstance() {
		return INSTANCE;
	}

	private ZookeeperWatcher watcher = new ZookeeperWatcher(this);

	private List<ACL> defAcl = Ids.OPEN_ACL_UNSAFE;

	private ZooKeeperManager() {
		INSTANCE = this;
	}

	@Override
	protected void register(ZooKeeper zookeeper) throws Exception {
		zookeeper.register(watcher);
		Stat stat = new Stat();
		defAcl = zookeeper.getACL("/", stat);
		watcher.initHandler(); // reconnect !!
	}

	public boolean exists(String path, boolean watch) throws KeeperException, InterruptedException {
		try {
			return getZooKeeper().exists(path, watch) != null;
		}
		catch (NoNodeException ex) {
			return false;
		}
	}

	public byte[] getData(String path, boolean watch) throws KeeperException, InterruptedException {
		try {
			return getZooKeeper().getData(path, watch, null);
		}
		catch (NoNodeException ex) {
			return null;
		}
	}

	public List<String> getChildren(String path, boolean watch) throws KeeperException, InterruptedException {
		try {
			return getZooKeeper().getChildren(path, watch, null);
		}
		catch (NoNodeException ex) {
			return null;
		}
	}

	public String create(String path, byte data[], List<ACL> acl) throws KeeperException, InterruptedException {
		ZooKeeper zk = getZooKeeper();

		path = ZkUtil.generatePath(path);
		if (zk.exists(path, false) != null)
			return path;

		String[] segments = path.split(ZkUtil.DELIMITER);
		path = "";

		for (String seg : segments) {
			if (seg.length() == 0)
				continue;
			try {
				path = ZkUtil.generatePath(path, seg);
				path = zk.create(path, data, acl, CreateMode.PERSISTENT);
			}
			catch (KeeperException.NodeExistsException ke) {
			}
		}
		return path;
	}

	public String create(String path, byte data[]) throws KeeperException, InterruptedException {
		getZooKeeper();
		return create(path, data, defAcl);
	}

	public void delete(String path) throws KeeperException, InterruptedException {
		ZooKeeper zk = getZooKeeper();
		for (String child : zk.getChildren(path, false)) {
			delete(ZkUtil.generatePath(path, child));
		}
		try {
			zk.delete(path, -1);
		}
		catch (KeeperException.NodeExistsException ke) {
		}
	}

	public void watch(String path, ZookeeperHandler handler) {
		watcher.register(path, handler);
	}

	@Override
	public void process(WatchedEvent event) {
		try {
			if (event.getState() == Event.KeeperState.Expired) {
				LOG.warn("ZK SESSION EXPIRED");
				close();
				if (event.getState() == Event.KeeperState.Expired) {

					for (ZookeeperHandler handle : watcher.handlers.values())
						handle.sessionExpired();
				}
				return;
			}
		}
		catch (Exception e) {
			LOG.error("process event error", e);
		}
	}
}