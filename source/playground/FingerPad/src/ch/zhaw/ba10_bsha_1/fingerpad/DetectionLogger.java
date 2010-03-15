package ch.zhaw.ba10_bsha_1.fingerpad;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Collection;
import java.util.Iterator;
import android.os.Environment;
import android.util.Log;

public class DetectionLogger {

	
	private static DetectionLogger instance;
	private final String TAG =  "FingerPad.DetectionLogger";
	private String LogFile = "fingerpad.log";
	
	
	public static DetectionLogger getInstance() {
		if (instance == null) {
			instance = new DetectionLogger();
		}
		return instance;
	}
	
	public void log(Collection<Character> characters) {
		BufferedWriter writer = null;
		try {
		    File root = Environment.getExternalStorageDirectory();
			File file = new File(root, LogFile);
			if (!file.exists()) {
				file.createNewFile();
			}
			writer = new BufferedWriter(new FileWriter(file, true));
			
			for (Character character : characters) {
				StringBuffer mgs = new StringBuffer();
				Iterator<MicroGesture> itr = character.getMicroGestures().iterator();
				while (itr.hasNext()) {
					mgs.append(itr.next());
					if (itr.hasNext()) {
						mgs.append(',');
					}
				}
				writer.write(character.getDetectedCharacter() + ";" + 
						character.getDetectionProbability() + ";" + 
						mgs.toString());
				writer.flush();
			}
		} catch (Exception ex) {
			Log.e(TAG, "Logger: Exception thrown", ex);
			Log.e(TAG, "Logger: StackTrace\n" + Log.getStackTraceString(ex));
		} finally {
			try {
				writer.close();
			} catch (Exception e) {}
		}
	}
}
