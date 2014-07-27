package com.scan.ticket;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class SerializerClass {
	public static byte[] serializeObject(Object o) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutput out = new ObjectOutputStream(bos);
			out.writeObject(o);
			out.flush();
			out.close();
			byte[] buf = bos.toByteArray();
			return buf;
		} catch (IOException ioe) {
			return null;
		}
	}

	public static Object deserializeObject(byte[] b) {
		try {
			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(b));
			Object object = in.readObject();
			in.close();
			return object;
		} catch (ClassNotFoundException cnfe) {
			return null;
		} catch (IOException ioe) {
			return null;
		}
	}
}
