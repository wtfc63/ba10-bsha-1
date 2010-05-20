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

import ch.zhaw.ba10_bsha_1.Character;
import ch.zhaw.ba10_bsha_1.StrategyArgument;


/**
 * Example of an implementation of {@link IPostprocessingStrategy} which logs the given
 * {@link Character}s to Logcat and into a log-file and returns them unchanged
 * 
 * @author Julian Hanhart, Dominik Giger
 */
public class PostprocessingStrategyLog extends BaseStrategy implements IPostprocessingStrategy {

	
	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------
	
	
	private static final String TAG = "CDS_Log";
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
		return "Logs the given Characters into the specified log-file";
	} 


	//---------------------------------------------------------------------------
	// Implementation of IPostprocessingStrategy
	//---------------------------------------------------------------------------
	

	/**
	 * Logs the given {@link Character}s to Logcat and into a file and returns the unchanged
	 * 
	 * @param chars
	 * @return
	 */
	@Override
	public Collection<Character> process(Collection<Character> chars) {
		try {
			Log.i(TAG, "Number of Characters detected: " + chars.size());
			DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			BufferedWriter writer  = new BufferedWriter(new FileWriter(logFile, true), 8192);
			writer.append(format.format(new Date()));
			writer.append(": ");
			writer.append(TAG);
			writer.append(", detected ");
			writer.append(Integer.valueOf(chars.size()).toString());
			writer.append(" Characters:\n");
			for (Character c : chars) {
				//Send MicroGesture to Logcat
				Log.i(TAG, "\t'-Character: " + c.toString());
				//Write MicroGesture to log-file
				writer.append(format.format(new Date()));
				writer.append(": \t'-");
				writer.append(TAG);
				writer.append(", detected Character: ");
				writer.append(c.toString());
				writer.append('\n');
			}
			writer.close();
		} catch (Exception ex) {
			Log.e(TAG, ex.getMessage(), ex);
		}
		return chars;
	}
}
