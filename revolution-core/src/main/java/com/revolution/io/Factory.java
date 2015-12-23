package com.revolution.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Factory<T extends Writable> {

	public abstract T newInstance();

	public byte[] getBytes(T object) {
		if (object == null)
			return null;

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			object.write(new DataOutputStream(out));
		} catch (IOException e) {
			return null;
		}
		return out.toByteArray();
	}

	public T readBytes(byte[] bytes) {
		if (bytes == null)
			return null;

		T object = newInstance();
		try {
			object.readFields(new DataInputStream(new ByteArrayInputStream(bytes)));
		} catch (IOException e) {
			return null;
		}
		return object;
	}
}
