package com.revolution.util;

import java.util.regex.Pattern;

public class ZkUtil {

	public static String DELIMITER = "/";

	private static Pattern TRIM = Pattern.compile("^/+|/+$");

	public static String generatePath(String... paths) {
		StringBuffer buffer = new StringBuffer();
		for (String path : paths) {
			path = TRIM.matcher(path).replaceAll("");
			if (path.length() == 0)
				continue;
			buffer.append(DELIMITER).append(path);
		}
		return buffer.toString();
	}

	public static String basename(String path) {
		int end = path.length();
		while (end > 0) {
			int index = path.lastIndexOf(DELIMITER, end - 1);
			if (index == end - 1)
				end = index;
			else
				return path.substring(index + 1, end);
		}
		return "/";
	}
}
