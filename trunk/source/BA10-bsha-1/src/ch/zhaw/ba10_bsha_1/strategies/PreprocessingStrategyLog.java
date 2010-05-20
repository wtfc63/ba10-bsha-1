package ch.zhaw.ba10_bsha_1.strategies;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

import ch.zhaw.ba10_bsha_1.StrategyArgument;
import ch.zhaw.ba10_bsha_1.TouchPoint;
import ch.zhaw.ba10_bsha_1.service.MicroGesture;


/**
 * Example of an implementation of {@link IPreprocessingStrategy} which logs the given
 * {@link TouchPoint}s to Logcat and into a log-file and returns them unchanged
 * 
 * @author Julian Hanhart, Dominik Giger
 */
public class PreprocessingStrategyLog extends BaseStrategy implements IPreprocessingStrategy {

	
	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------
	
	
	private static final String TAG = "PPS_Log";
	private File logFile;

	
	//---------------------------------------------------------------------------
	// Implementation of BaseStrategy's abstract methods
	//---------------------------------------------------------------------------
	
	
	@Override
	public void initialize() {
		if (!hasArgument("Path")) {
			addArgument(new StrategyArgument(getStrategyName(), "Path", "/sdcard/ba10_bsha_1.log", "Path of the log-file"));
		}
		try {
			logFile = new File(getArgument("Path").getArgumentValue());
			if (!logFile.exists()) {
				logFile.createNewFile();
			}
		} catch (IOException ex) {
			Log.e(TAG, ex.getMessage(), ex);
		}
	}

	@Override
	protected String getStrategyName() {
		return "Log";
	}
	
	@Override
	protected String getStrategyDescription() {
		return "Logs the TouchPoints of the given MicroGesture into the specified log-file";
	}


	//---------------------------------------------------------------------------
	// Implementation of IMicroGestureDetectionStrategy
	//---------------------------------------------------------------------------
	

	/**
	 * Logs the {@link TouchPoint}s of the given {@link MicroGesture} 
	 * to Logcat and into a file and returns the unchanged
	 * 
	 * @param micro_gesture
	 * @return
	 */
	@Override
	public MicroGesture process(MicroGesture micro_gesture) {
		try {
			Log.i(TAG, "Number of TouchPoints processed: " + micro_gesture.getPoints().size());
			DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			BufferedWriter writer  = new BufferedWriter(new FileWriter(logFile, true), 8192);
			writer.append(format.format(new Date()));
			writer.append(": ");
			writer.append(TAG);
			writer.append(", processed ");
			writer.append(Integer.valueOf(micro_gesture.getPoints().size()).toString());
			writer.append(" TouchPoints:\n");
			for (TouchPoint point : micro_gesture.getPoints()) {
				//Send MicroGesture to Logcat
				Log.i(TAG, "\t'-TouchPoint: " + point.toString());
				//Write MicroGesture to log-file
				writer.append(format.format(new Date()));
				writer.append(": \t'-");
				writer.append(TAG);
				writer.append(", processed TouchPoint: ");
				writer.append(point.toString());
				writer.append('\n');
			}
			writer.close();
		} catch (Exception ex) {
			Log.e(TAG, ex.getMessage(), ex);
		}
		return micro_gesture;
	}
}
