package com.revolution.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NullWritable implements Writable {

	public static NullWritable INSTANCE = new NullWritable();

	public static Factory FACTORY = new Factory();

	private NullWritable() {
	}

	@Override
	public void readFields(DataInput in) throws IOException {
	}

	public void write(DataOutput out) throws IOException {
	}

	static class Factory extends com.revolution.io.Factory<NullWritable> {
		static final byte[] NULL = new byte[0];

		public NullWritable newInstance() {
			return INSTANCE;
		}

		public NullWritable readBytes(byte[] bytes) {
			return INSTANCE;
		}

		public byte[] getBytes(NullWritable object) {
			return object == null ? null : NULL;
		}
	}
}
