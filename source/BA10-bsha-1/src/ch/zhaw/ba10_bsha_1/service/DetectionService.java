package ch.zhaw.ba10_bsha_1.service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.os.RemoteException;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import ch.zhaw.ba10_bsha_1.R;
import ch.zhaw.ba10_bsha_1.Character;
import ch.zhaw.ba10_bsha_1.RingBuffer;
import ch.zhaw.ba10_bsha_1.StrategyArgument;
import ch.zhaw.ba10_bsha_1.TouchPoint;
import ch.zhaw.ba10_bsha_1.ime.ServiceTest;
import ch.zhaw.ba10_bsha_1.strategies.CharacterDetectionStrategyManager;
import ch.zhaw.ba10_bsha_1.strategies.ICharacterDetectionStrategy;
import ch.zhaw.ba10_bsha_1.strategies.IMicroGestureDetectionStrategy;
import ch.zhaw.ba10_bsha_1.strategies.IPostprocessingStrategy;
import ch.zhaw.ba10_bsha_1.strategies.IPreprocessingStrategy;
import ch.zhaw.ba10_bsha_1.strategies.IStrategy;
import ch.zhaw.ba10_bsha_1.strategies.MicroGestureDetectionStrategyManager;
import ch.zhaw.ba10_bsha_1.strategies.PostprocessingStrategyManager;
import ch.zhaw.ba10_bsha_1.strategies.PreprocessingStrategyManager;
import ch.zhaw.ba10_bsha_1.strategies.StrategyManager;


/**
 * Implementation of the remote {@link Service} used for the detection of 
 * {@link Character}s out of a list of input points.
 * 
 * Based on the RemoteService code sample from the 
 * API Demos example of the Android SDK.
 * 
 * @author Julian Hanhart, Dominik Giger
 */
public class DetectionService extends Service {


	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------
	
	
    private static final int STRATEGY_TYPE_PREPROCESSING = 0;
    private static final int STRATEGY_TYPE_MICROGESTURE_DETECTION = 1;
    private static final int STRATEGY_TYPE_CHARACTER_DETECTION = 2;
    private static final int STRATEGY_TYPE_POSTPROCESSING = 3;
    
    private static final String TAG = "DetectionService";
    private static Context CONTEXT;
    
    /**
     * This is a list of callbacks that have been registered with the service. 
     * Note that this is package scoped (instead of private) so that it can 
     * be accessed more efficiently from inner classes.
     */
    final RemoteCallbackList<IReturnResults> callbacks = new RemoteCallbackList<IReturnResults>();
    private final int BUFFER_SIZE = 10;
    private final boolean DEBUG = true; 
    
    private ArrayList<TouchPoint> inputPoints;
    private RingBuffer<TouchPoint> buffer;
    
    private StrategyQueue<IPreprocessingStrategy> preprocessingSteps;
    private StrategyQueue<IMicroGestureDetectionStrategy> mgDetectionSteps;
    private ICharacterDetectionStrategy charDetectionStrategy;
    private IPostprocessingStrategy postprocessingStrategy;
    
    private NotificationManager notificationManager;

	//---------------------------------------------------------------------------
	// Detection-initialization
	//---------------------------------------------------------------------------
	
    
    /**
     * Initialisation of the detection. This is the place where one can define, 
     * which strategies should be used on the input and in what order they 
     * should be applied
     */
    private void initDetection() {
        inputPoints = new ArrayList<TouchPoint>();
        buffer = new RingBuffer<TouchPoint>(BUFFER_SIZE);
        
        int priority = 1;
    	preprocessingSteps = new StrategyQueue<IPreprocessingStrategy>();
    	preprocessingSteps.enqueue(PreprocessingStrategyManager.getInstance().getStrategy("Spline"), priority++);
    	
    	priority = 1;
    	mgDetectionSteps = new StrategyQueue<IMicroGestureDetectionStrategy>();
    	mgDetectionSteps.enqueue(MicroGestureDetectionStrategyManager.getInstance().getStrategy("Edges"), priority++);
    	mgDetectionSteps.enqueue(MicroGestureDetectionStrategyManager.getInstance().getStrategy("Circles"), priority++);
    	mgDetectionSteps.enqueue(MicroGestureDetectionStrategyManager.getInstance().getStrategy("RemoveTiny"), priority++);
    	mgDetectionSteps.enqueue(MicroGestureDetectionStrategyManager.getInstance().getStrategy("Lines"), priority++);
    	mgDetectionSteps.enqueue(MicroGestureDetectionStrategyManager.getInstance().getStrategy("HalfCircle"), priority++);
    	
    	charDetectionStrategy = CharacterDetectionStrategyManager.getInstance().getStrategy("Graph");
    	
    	postprocessingStrategy = PostprocessingStrategyManager.getInstance().getStrategy("None");
    }


	//---------------------------------------------------------------------------
	// Starting detection
	//---------------------------------------------------------------------------
	
    
    /**
     * Starts the detection of {@link Character}s from the input points (the buffer is first emptied)
     */
    private Collection<Character> startDetection() {
		Log.i("DetectionService.startDetection()", "Started detection");
		
		//Add all points from the buffer to the inputPoints
		while (!buffer.isEmpty()) {
			TouchPoint point = buffer.get();
			if ((point != null) && !inputPoints.contains(point)) {
				inputPoints.add(point);
			}
		}
		
		//Create temporary MicroGesture to start end empty input point list
    	MicroGesture startMG = new MicroGesture(inputPoints);
    	inputPoints.clear();
    	
    	//Preprocessing
    	Iterator<IPreprocessingStrategy> prep_itr = preprocessingSteps.iterator();
    	while (prep_itr.hasNext()) {
    		IPreprocessingStrategy prep_strat = prep_itr.next();
    		if (prep_strat.isEnabled()) {
    			startMG = prep_strat.process(startMG);
    		}
    	}
    	
		//MicroGesture detection
    	Collection<MicroGesture> tmpMGs = new ArrayList<MicroGesture>();
    	tmpMGs.add(startMG);
    	Iterator<IMicroGestureDetectionStrategy> mg_itr = mgDetectionSteps.iterator();
    	while (mg_itr.hasNext()) {
    		IMicroGestureDetectionStrategy mgd_strat = mg_itr.next();
    		if (mgd_strat.isEnabled()) {
    			tmpMGs = mgd_strat.detectMicroGestures(tmpMGs);
    		}
    	}
    	if (DEBUG) {
    		for (MicroGesture mg : tmpMGs) {
    			Log.i(TAG, "Recognized MicroGesture: " + mg.toString());
    		}
    	}
    	
    	//Character detection
    	Collection<Character> result = charDetectionStrategy.detectCharacter(tmpMGs);
    	if (DEBUG) {
			Log.i(TAG, "Detected: " + result.size() + " Characters");
	    	for (Character character : result) {
	    		Log.i(TAG, "Detected Character: " + character.toString());
			}
    	}
    	
    	//Postprocessing
    	if (postprocessingStrategy.isEnabled()) {
	    	result = postprocessingStrategy.process(result);
	    	if (DEBUG) {
				Log.i(TAG, "After Postprocessing:");
				Log.i(TAG, "  Detected: " + result.size() + " Characters");
		    	for (Character character : result) {
		    		Log.i(TAG, "  Detected Character: " + character.toString());
				}
	    	}
    	}
    	
		//Broadcast results
		int i = callbacks.beginBroadcast();
		while (i > 0) {
		    i--;
		    try {
		        callbacks.getBroadcastItem(i).recognisedCharacters(new ArrayList<Character>(result));
		    } catch (RemoteException e) {
		        // The RemoteCallbackList will take care of removing
		    	// the dead object for us.
		    }
		}
		callbacks.finishBroadcast();
		
    	return result;
    }


	//---------------------------------------------------------------------------
	// Lifecycle-methods
	//---------------------------------------------------------------------------
	
    
    /**
     * Called when the {@link Service} is created
     */
    @Override
    public void onCreate() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        CONTEXT = getApplicationContext();
        initDetection();

        // Display a notification about us starting.
        showNotification();
    	Toast.makeText(DetectionService.this, R.string.service_started, Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when the {@link Service} is destroyed
     */
    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        notificationManager.cancel(R.string.service_started);

        // Tell the user we stopped.
        Toast.makeText(this, R.string.service_stopped, Toast.LENGTH_SHORT).show();
        
        // Unregister all callbacks.
        callbacks.kill();
    }
    
    /**
     * Called when Service is bound to
     */
    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }


	//---------------------------------------------------------------------------
	// Implementation of the AIDL-interface
	//---------------------------------------------------------------------------
	
    
    /**
     * The {@link IDetectionService} Interface as defined through AIDL
     */
    private final IDetectionService.Stub serviceBinder = new IDetectionService.Stub() {

        /**
         * Registering callback interface with service.
         */
		@Override
        public void registerCallback(IReturnResults cb) throws RemoteException {
            if (cb != null) {
            	callbacks.register(cb);
            }
        }
		
		/**
	     * Remove a previously registered callback interface.
	     */
		@Override
        public void unregisterCallback(IReturnResults cb) throws RemoteException {
            if (cb != null) {
            	callbacks.unregister(cb);
            }
        }

		
	    /**
	     * Add {@link TouchPoint}s to detection queue and start detection if requested
	     */
		@Override
		public void addTouchPoints(List<TouchPoint> points, boolean start_detection) throws RemoteException {
			if (points != null) {
				inputPoints.addAll(points);
				if (start_detection || buffer.isFull()) {
					startDetection();
				}
			}
		}

	    /**
	     * Add a {@link TouchPoint} to detection queue
	     */
		@Override
		public void addTouchPoint(float pos_x, float pos_y, float strength, long timestamp) throws RemoteException {
			TouchPoint tmp = new TouchPoint(new PointF(pos_x, pos_y), strength, timestamp);
			buffer.add(tmp);
			Log.i("DetectionService.addTouchPoint()", "Added: " + tmp.toString());
			if (buffer.isFull()) {
				startDetection();
			}
		}

	    /**
	     * End sample (Touch-up) and start detection
	     */
		@Override
		public void endSample() throws RemoteException {
			startDetection();
		}
		

	    /**
	     * Get a List of all available strategies (separated by semicolon: "type;name;description;enabled")
	     */
		@Override
		public List<String> getAvailableStrategies() throws RemoteException {
			ArrayList<String> result = new ArrayList<String>();
			String[] strats = PreprocessingStrategyManager.getInstance().getStrategyList();
			for (String strat : strats) {		
				result.add(printStrategy(
						PreprocessingStrategyManager.getInstance().getStrategy(strat), 
						STRATEGY_TYPE_PREPROCESSING));
			}
			strats = MicroGestureDetectionStrategyManager.getInstance().getStrategyList();
			for (String strat : strats) {
				result.add(printStrategy(
						PreprocessingStrategyManager.getInstance().getStrategy(strat), 
						STRATEGY_TYPE_MICROGESTURE_DETECTION));
			}
			strats = CharacterDetectionStrategyManager.getInstance().getStrategyList();
			for (String strat : strats) {
				result.add(printStrategy(
						CharacterDetectionStrategyManager.getInstance().getStrategy(strat), 
						STRATEGY_TYPE_CHARACTER_DETECTION));
			}
			strats = PostprocessingStrategyManager.getInstance().getStrategyList();
			for (String strat : strats) {
				result.add(printStrategy(
						PostprocessingStrategyManager.getInstance().getStrategy(strat), 
						STRATEGY_TYPE_POSTPROCESSING));
			}
			return result;
		}

	    /**
	     * Get a List of all actively used strategies (separated by semicolon: "type;name;description")
	     */
		@Override
		public List<String> getActiveStrategies() throws RemoteException {
			ArrayList<String> result = new ArrayList<String>();
			Iterator<IPreprocessingStrategy> pps_itr = preprocessingSteps.iterator();
			while (pps_itr.hasNext()) {
				IStrategy strategy = pps_itr.next();
				if (strategy.isEnabled()) {
					result.add(printStrategy(strategy, STRATEGY_TYPE_PREPROCESSING));
				}
			}
			Iterator<IMicroGestureDetectionStrategy> mgds_itr = mgDetectionSteps.iterator();
			while (mgds_itr.hasNext()) {
				IStrategy strategy = mgds_itr.next();
				if (strategy.isEnabled()) {
					result.add(printStrategy(strategy, STRATEGY_TYPE_MICROGESTURE_DETECTION));
				}
			}
			result.add(printStrategy(charDetectionStrategy, STRATEGY_TYPE_CHARACTER_DETECTION));
			if (postprocessingStrategy.isEnabled()) {
				result.add(printStrategy(postprocessingStrategy, STRATEGY_TYPE_POSTPROCESSING));
			}
			return result;
		}

	    /**
	     * Get a listing of the arguments of a strategy
	     */
		@Override
		public List<StrategyArgument> getStrategyConfiguration(String strategy_name, int type) throws RemoteException {
			IStrategy strategy = getManagerByType(type).getStrategy(strategy_name);
			return (strategy != null) ? new ArrayList<StrategyArgument>(strategy.getConfiguration()) : null;
		}

	    /**
	     * Set an argument in all strategies supporting the given argument
	     */
		@Override
		public void broadcastArgument(StrategyArgument argument) throws RemoteException {
			PreprocessingStrategyManager.getInstance().broadcastArgument(argument);
			MicroGestureDetectionStrategyManager.getInstance().broadcastArgument(argument);
			CharacterDetectionStrategyManager.getInstance().broadcastArgument(argument);
			PostprocessingStrategyManager.getInstance().broadcastArgument(argument);
		}

	    /**
	     * Set an argument of a strategy
	     */
		@Override
	    public void setStrategyArgument(StrategyArgument argument, int type) throws RemoteException {
			IStrategy strategy = getManagerByType(type).getStrategy(argument.getStrategyName());
			if (strategy != null) {
				strategy.setArgument(argument);
			}
		}
    };


	//---------------------------------------------------------------------------
	// Static helper-methods to the AIDL-implementation
	//---------------------------------------------------------------------------
	
    
    /**
     * Get String-representation of the given {@link IStrategy} to be send over the AIDL-interface
     * 
     * @param strategy
     * @param type
     * @return
     */
    static private String printStrategy(IStrategy strategy, int type) {
    	StringBuffer result = new StringBuffer();
		result.append(type);
		result.append(';');
		result.append(strategy.toString());
		result.append(';');
		result.append(strategy.getDescription());
		result.append(';');
		result.append(strategy.isEnabled() ? "true" : "false");
		return result.toString();
    }
    
    /**
     * Get the corresponding {@link StrategyManager} to the given Manager-Type
     * 
     * @param type
     * @return
     */
    @SuppressWarnings("unchecked")
	static private StrategyManager<IStrategy> getManagerByType(int type) {
    	StrategyManager result = null;
    	switch (type) {
    		case STRATEGY_TYPE_PREPROCESSING :
    			result = PreprocessingStrategyManager.getInstance();
    			break;
    		case STRATEGY_TYPE_MICROGESTURE_DETECTION :
    			result = MicroGestureDetectionStrategyManager.getInstance();
    			break;
    		case STRATEGY_TYPE_CHARACTER_DETECTION :
    			result = CharacterDetectionStrategyManager.getInstance();
    			break;
    		case STRATEGY_TYPE_POSTPROCESSING :
    			result = PostprocessingStrategyManager.getInstance();
    			break;
    		default :
    			result = CharacterDetectionStrategyManager.getInstance();
    	}
    	return result;
    }


	//---------------------------------------------------------------------------
	// Further methods
	//---------------------------------------------------------------------------
	
    
    /**
     * Get the Application's {@link Context}
     * 
     * @return
     */
    public static Context getContext() {
    	return CONTEXT;
    }

    /**
     * Show a {@link Notification} while this {@link Service} is running.
     */
    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.service_started);

        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification(R.drawable.icon, text, System.currentTimeMillis());

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, ServiceTest.class), 0);

        // Set the info for the views that show in the notification panel.
        String tmp = "Test Service";
        notification.setLatestEventInfo(this, tmp.subSequence(0, tmp.length()), text, contentIntent);

        // Send the notification. We use a string id because it is a unique number, it is later used to cancel
        notificationManager.notify(R.string.service_started, notification);
    }
}
