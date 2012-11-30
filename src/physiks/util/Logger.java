package physiks.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
	private FileWriter fStream;
	private static Logger instance = new Logger();
	private StringBuffer buffer;
	private BufferedWriter out;
	
	public static Logger getInstance() {
		return instance;
	}
	
	public void init() {
		buffer = new StringBuffer();
		 
		try {
			fStream = new FileWriter("log.txt");
			out = new BufferedWriter(fStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Logger() {
	}
	
	public void buffer(String str) {
		buffer.append(str + "\n");
	}
	
	public void dump() {
		try {
			out.write(buffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		buffer.delete(0, buffer.length());
	}
	
	public void log(String str) {
		try {
			out.write(str + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
