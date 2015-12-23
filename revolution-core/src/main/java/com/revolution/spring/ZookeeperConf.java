package com.revolution.spring;

import java.util.Enumeration;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * 读取zookeeper配置 zookeeper.properties settings.properties
 *
 * @author jingyun.zou@renren-inc.com
 */
@Component
public class ZookeeperConf implements BeanFactoryPostProcessor {

	private static Logger LOG = LoggerFactory.getLogger(ZookeeperConf.class);

	public static final String PREFIX = "zookeeper.";

	@Override
	@SuppressWarnings("unchecked")
	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
		Properties properties = new Properties();

		try {
			Properties conf = PropertiesLoaderUtils
					.loadAllProperties("zookeeper.properties");
			Properties settings = beanFactory.getBean("settings",
					Properties.class);
			if (settings != null) {
				Enumeration<String> names = (Enumeration<String>) settings
						.propertyNames();
				while (names.hasMoreElements()) {
					String name = names.nextElement();
					if (name.startsWith(PREFIX))
						conf.put(name.substring(PREFIX.length()),
								settings.getProperty(name));
				}
			}
			CollectionUtils.mergePropertiesIntoMap(conf, properties);

			String hosts = properties.getProperty("hosts", "127.0.0.1:2181");
			String rootPath = properties.getProperty("rootPath", "");
			properties.put("connectString", hosts + rootPath);
		} catch (Exception e) {
			LOG.warn("load zookeeper properties fail", e);
		}
		beanFactory.registerSingleton("zkProperties", properties);
	}
}
