package com.revolution.spring;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class ZookeeperClient {

	static final Logger LOG = LoggerFactory.getLogger(ZookeeperClient.class);

	@Value("#{zkProperties['connectString'] ?: 'localhost:2181'}")
	private String connectString = "localhost:2181";

	@Value("#{zkProperties['sessionTimeout'] ?: 30000}")
	private Integer sessionTimeout = 30000;

	@Value("#{zkProperties['recoverInteval'] ?: 30000}")
	private Integer recoverInteval = 30000;

	private volatile ZooKeeper zookeeper;
	private volatile boolean init = false;

	protected ZookeeperClient() {
	}

	public ZooKeeper getZooKeeper() {
		if (zookeeper == null || !init)
			connect();
		return zookeeper;
	}

	/**
	 * register watch && initialize
	 */
	protected void register(ZooKeeper zookeeper) throws Exception {
	}

	public synchronized ZooKeeper connect() {
		try {
			if (zookeeper == null)
				zookeeper = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
					@Override
					public void process(WatchedEvent event) {
					}
				});
				zookeeper.exists("/", false);
			if (! init) {
				init = true;
				register(zookeeper);
			}
			return zookeeper;
		} catch (KeeperException.ConnectionLossException e) {
			LOG.error("zookeeper connect error: {}", e.getMessage());
		} catch (Exception ex) {
			LOG.error("init zookeeper error", ex);
		}
		init = false;
		throw new IllegalStateException("zookeeper not initialized");
	}

	/**
	 * Just for Test
	 */
	public synchronized ZooKeeper reconnect() throws Exception {
		if (zookeeper == null)
			throw new IllegalStateException("illegal connection");

		return new ZooKeeper(connectString, sessionTimeout, DEFAULT,
					zookeeper.getSessionId(), zookeeper.getSessionPasswd());
	}

	public synchronized void close() {
		ZooKeeper zk = zookeeper;
		zookeeper = null;
		init = false;

		close(zk);
	}

	private void close(ZooKeeper zk) {
		if (zk == null)
			return;

		timer.schedule(new RecoverTask(), recoverInteval);
		LOG.info("close zookeeper({})", zk.getSessionId());
		try {
			zk.close();
		}
		catch (Exception e) {
			LOG.warn("close zookeeper", e);
		}
	}

	private final Timer timer = new Timer(true);
	private class RecoverTask extends TimerTask {
		@Override
		public void run() {
			try {
				if (getZooKeeper() != null)
					return;
			} catch (Exception e) {
				LOG.warn("try recover zookeeper but fail");
			}
			timer.schedule(new RecoverTask(), recoverInteval);
		}
	};

	private static final Watcher DEFAULT =  new Watcher() {
		@Override
		public void process(WatchedEvent event) {
		}
	};
}
