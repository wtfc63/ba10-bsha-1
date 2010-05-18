package ch.zhaw.ba10_bsha_1.strategies;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import android.util.Log;

import ch.zhaw.ba10_bsha_1.StrategyArgument;
import ch.zhaw.ba10_bsha_1.service.MicroGesture;


/**
 * Example of an implementation of {@link IMicroGestureDetectionStrategy} which logs the given
 * {@link MicroGesture}s to Logcat and into a log-file and returns them unchanged
 * 
 * @author Julian Hanhart, Dominik Giger
 */
public class MicroGestureDetectionStrategyLog extends BaseStrategy implements IMicroGestureDetectionStrategy {

	
	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------
	
	
	private static final String TAG = "MGDS_Log";
	private File logFile;
	private BufferedWriter writer;

	
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
			writer  = new BufferedWriter(new FileWriter(logFile, true));
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
		return "Logs the given MicroGestures into the specified log-file";
	}


	//---------------------------------------------------------------------------
	// Implementation of IMicroGestureDetectionStrategy
	//---------------------------------------------------------------------------
	

	/**
	 * Logs the given {@link MicroGesture}s to Logcat and into a file and returns the unchanged
	 * 
	 * @param micro_gestures
	 * @return
	 */
	@Override
	public Collection<MicroGesture> detectMicroGestures(Collection<MicroGesture> micro_gestures) {
		try {
			DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			for (MicroGesture mg : micro_gestures) {
				//Send MicroGesture to Logcat
				Log.i(TAG, "MicroGesture: " + mg.toString());
				//Write MicroGesture to log-file
				writer.append(format.format(new Date()));
				writer.append(": ");
				writer.append(TAG);
				writer.append(", detected MicroGesture: ");
				writer.append(mg.toString());
				writer.append('\n');
			}
			writer.close();
		} catch (Exception ex) {
			Log.e(TAG, ex.getMessage(), ex);
		}
		return micro_gestures;
	}

}
