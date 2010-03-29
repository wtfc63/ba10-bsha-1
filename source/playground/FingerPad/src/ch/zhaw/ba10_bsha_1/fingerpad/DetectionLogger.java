package ch.zhaw.ba10_bsha_1.fingerpad;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Iterator;
import android.os.Environment;
import android.util.Log;

public class DetectionLogger {

	
	private static DetectionLogger instance;
	private final String TAG =  "FingerPad.DetectionLogger";
	private String logFile = "fingerpad.log";
	private char[] attemptedChars = null;
	
	
	public static DetectionLogger getInstance() {
		if (instance == null) {
			instance = new DetectionLogger();
		}
		return instance;
	}
	
	public char[] getAttemptedChars() {
		return attemptedChars;
	}

	public void setAttemptedChars(char[] attemptedChars) {
		this.attemptedChars = attemptedChars;
	}

	public void log(Collection<Character> characters) {
		StringBuffer att_chars = new StringBuffer();
		int i;
		if (attemptedChars != null) {
			for (i = 0; i < attemptedChars.length; i++) {
				att_chars.append(attemptedChars[i]);
				if (i < (attemptedChars.length - 1)) {
					att_chars.append(',');
				}
			}
		} else {
			att_chars.append('-');
		}
		i = 0;
		for (Character character : characters) {
			StringBuffer mgs = new StringBuffer();
			Iterator<MicroGesture> itr = character.getMicroGestures().iterator();
			while (itr.hasNext()) {
				mgs.append(itr.next());
				if (itr.hasNext()) {
					mgs.append(',');
				}
			}
			StringBuffer line = new StringBuffer();
			line.append(i++);
			line.append(';');
			line.append(att_chars);
			line.append(';');
			line.append((character.getDetectedCharacter() != '\0') 
					? character.getDetectedCharacter() : '-');
			line.append(';');
			line.append(character.getDetectionProbability());
			line.append(';');
			line.append(mgs);
			writeLine(line.toString());
		}
	}
	
	public void logComment(String comment) {
		writeLine("# " + comment);
	}
	
	private void writeLine(String line) {
		BufferedWriter writer = null;
		try {
		    File root = Environment.getExternalStorageDirectory();
			File file = new File(root, logFile);
			if (!file.exists()) {
				file.createNewFile();
			}
			writer = new BufferedWriter(new FileWriter(file, true));
			writer.write(line + System.getProperty("line.separator"));
			writer.flush();
		} catch (Exception ex) {
			Log.e(TAG, "Logger: Exception thrown", ex);
			Log.e(TAG, "Logger: StackTrace\n" + Log.getStackTraceString(ex));
		} finally {
			try {
				writer.close();
			} catch (Exception e) {}
		}
	}
	
	public String getLog(boolean mark_line) {
		StringBuffer log = new StringBuffer();
		FileInputStream stream = null;
		BufferedReader reader = null;
		try {
			File root = Environment.getExternalStorageDirectory();
			File file = new File(root, logFile);
			stream = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(stream));
			String line = new String();
			while ((line = reader.readLine()) != null) {
				if (mark_line && !line.startsWith("#")) {
					if (line.startsWith("0;")) {
						log.append("-----");
						log.append(System.getProperty("line.separator"));
					}
					log.append("> ");
				} else if (mark_line && line.startsWith("#")) {
					log.append(System.getProperty("line.separator"));
				}
				log.append(line);
				log.append(System.getProperty("line.separator"));
			}
		} catch (Exception ex) {
			Log.e(TAG, "Logger: Exception thrown", ex);
			Log.e(TAG, "Logger: StackTrace\n" + Log.getStackTraceString(ex));
		} finally {
			try {
				reader.close();
				stream.close();
			} catch (Exception e) {}
		}
		return log.toString();
	}
}
