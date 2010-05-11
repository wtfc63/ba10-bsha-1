package ch.zhaw.ba10_bsha_1.service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.PointF;
import android.os.RemoteException;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.
import ch.zhaw.ba10_bsha_1.Character;
import ch.zhaw.ba10_bsha_1.R;
import ch.zhaw.ba10_bsha_1.TouchPoint;
import ch.zhaw.ba10_bsha_1.ime.ServiceTest;

/**
 * This is an example of implementing an application service that runs in a
 * different process than the application.  Because it can be in another
 * process, we must use IPC to interact with it.  The
 * {@link RemoteServiceController} and {@link RemoteServiceBinding} classes
 * show how to interact with the service.
 */
public class DetectionService extends Service {
    /**
     * This is a list of callbacks that have been registered with the
     * service.  Note that this is package scoped (instead of private) so
     * that it can be accessed more efficiently from inner classes.
     */
    final RemoteCallbackList<IReturnRecognisedCharacters> callbacks = new RemoteCallbackList<IReturnRecognisedCharacters>();

    static final int BUFFER_SIZE = 10;
    
    private ArrayList<TouchPoint> inputPoints;
    private RingBuffer<TouchPoint> buffer;
    
    private PriorityQueue<IPreprocessingStrategy> preprocessingSteps;
    private PriorityQueue<IMicroGestureDetectionStrategy> mgDetectionSteps;
    private ICharacterDetectionStrategy charDetectionStrategy;
    private IPostprocessingStrategy postprocessingStrategy;
    
    NotificationManager mNM;
    
    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        initDetection();

        // Display a notification about us starting.
        showNotification();
        
        // While this service is running, it will continually increment a
        // number.  Send the first message that is used to perform the
        // increment.
        mHandler.sendEmptyMessage(REPORT_MSG);
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(R.string.service_started);

        // Tell the user we stopped.
        Toast.makeText(this, R.string.service_stopped, Toast.LENGTH_SHORT).show();
        
        // Unregister all callbacks.
        callbacks.kill();
        
        // Remove the next pending message to increment the counter, stopping
        // the increment loop.
        mHandler.removeMessages(REPORT_MSG);
    }
    
    
    private void initDetection() {
        inputPoints = new ArrayList<TouchPoint>();
        buffer = new RingBuffer<TouchPoint>(BUFFER_SIZE);
    	
        preprocessingSteps = new PriorityQueue<IPreprocessingStrategy>();
    	preprocessingSteps.add(PreprocessingStrategyManager.getInstance().getStrategy("Spline"));
    	
    	mgDetectionSteps = new PriorityQueue<IMicroGestureDetectionStrategy>();
    	mgDetectionSteps.add(MicroGestureDetectionStrategyManager.getInstance().getStrategy("Curvature"));
    	
    	charDetectionStrategy = CharacterDetectionStrategyManager.getInstance().getStrategy("None");
    	
    	postprocessingStrategy = PostprocessingStrategyManager.getInstance().getStrategy("None");
    }
    
    private Collection<Character> startDetection() {
		Log.i("DetectionService.startDetection()", "Started detection");
		
		while (!buffer.isEmpty()) {
			TouchPoint point = buffer.get();
			if (point != null) {
				inputPoints.add(point);
			}
		}
    	MicroGesture startMG = new MicroGesture(inputPoints);
    	
		Log.i("DetectionService.startDetection()", "Preprocessing...");
    	Iterator<IPreprocessingStrategy> prep_itr = preprocessingSteps.iterator();
    	while (prep_itr.hasNext()) {
    		Log.i("DetectionService.startDetection()", "Preprocessing: startMg = " + startMG.toString());
    		startMG = prep_itr.next().process(startMG);
    	}
    	
		Log.i("DetectionService.startDetection()", "MicroGesture detection...");
    	Collection<MicroGesture> tmpMGs = new ArrayList<MicroGesture>();
    	tmpMGs.add(startMG);
    	Iterator<IMicroGestureDetectionStrategy> mg_itr = mgDetectionSteps.iterator();
    	while (mg_itr.hasNext()) {
    		tmpMGs = mg_itr.next().detectMicroGestures(tmpMGs);
    	}
    	
		Log.i("DetectionService.startDetection()", "Character detection...");
    	Collection<Character> result = charDetectionStrategy.detectCharacter(tmpMGs);
		Log.i("DetectionService.startDetection()", "Detected: " + result.size() + " Characters");
    	for (Character character : result) {
    		Log.i("DetectionService.startDetection()", "Detected: " + character.toString());
		}
    	
		Log.i("DetectionService.startDetection()", "Postprocessing...");
    	result = postprocessingStrategy.process(result);
		Log.i("DetectionService.startDetection()", "Detected: " + result.size() + " Characters");
    	for (Character character : result) {
    		Log.i("DetectionService.startDetection()", "Detected: " + character.toString());
		}
    	
		Log.i("DetectionService.startDetection()", "Begin Broadcast...");
		int i = callbacks.beginBroadcast();
		while (i > 0) {
		    i--;
		    try {
	    		Log.i("DetectionService.startDetection()", "Sending Characters");
		        callbacks.getBroadcastItem(i).recognisedCharacters(new ArrayList<Character>(result));
		    	for (Character character : result) {
		    		Log.i("DetectionService.startDetection()", "Send: " + character.toString());
					//callbacks.getBroadcastItem(i).recognisedChar(character.getDetectedCharacter(), character.getDetectionProbability());
				}
		    } catch (RemoteException e) {
		        // The RemoteCallbackList will take care of removing
		    	// the dead object for us.
		    }
		}
		Log.i("DetectionService.startDetection()", "Finish Broadcast...");
		callbacks.finishBroadcast();
    	return result;
    }
    

    @Override
    public IBinder onBind(Intent intent) {
        // Select the interface to return.  If your service only implements
        // a single interface, you can just return it here without checking
        // the Intent.
        if (intent.getAction().equals(IDetectionService.class.getName())) {
            return serviceBinder;
        }
        return null;
    }

    /**
     * The IDetectionService Interface is defined through AIDL
     */
    private final IDetectionService.Stub serviceBinder = new IDetectionService.Stub() {
		@Override
        public void registerCallback(IReturnRecognisedCharacters cb) {
            if (cb != null) {
            	callbacks.register(cb);
            }
        }
		
		@Override
        public void unregisterCallback(IReturnRecognisedCharacters cb) {
            if (cb != null) {
            	callbacks.unregister(cb);
            }
        }
		
		@Override
		public void addTouchPoints(List<TouchPoint> points)	throws RemoteException {
			if (points != null) {
				for (TouchPoint point : points) {
					buffer.add(point);
					Log.i("DetectionService.addTouchPoints()", "Added: " + point.toString());
				}
				if (buffer.isFull()) {
					startDetection();
				}
			} else {
				Log.e("T3H_FAIL", "oh nose...");
			}
		}
		
		@Override
		public void addTouchPoint(float pos_x, float pos_y, float strength, long timestamp) {
			TouchPoint tmp = new TouchPoint(new PointF(pos_x, pos_y), strength, timestamp);
			buffer.add(tmp);
			Log.i("DetectionService.addTouchPoint()", "Added: " + tmp.toString());
			if (buffer.isFull()) {
				startDetection();
			}
		}
		
		@Override
		public void endSample() {
			startDetection();
			Log.i("DetectionService.endSample()", "Detection ended");
		}
    };
    
    private static final int REPORT_MSG = 1;
    
    /**
     * Our Handler used to execute operations on the main thread.  This is used
     * to schedule increments of our value.
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // It is time to bump the value!
                case REPORT_MSG:
                    // Broadcast to all clients the new value.
                    /*final int n = mCallbacks.beginBroadcast()Broadcast();
                    for (int i = 0; i < n; i++) {
                        try {
                        	ArrayList<Character> result = new ArrayList<Character>(1);
                        	result.add(new ch.zhaw.ba10_bsha_1.Character());
                            mCallbacks.getBroadcastItem(i).recognisedCharacters(result);
                        } catch (RemoteException e) {
                            // The RemoteCallbackList will take care of removing
                            // the dead object for us.
                        }
                    }
                    callbacks.finishBroadcast();*/
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    /**
     * Show a notification while this service is running.
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

        // Send the notification.
        // We use a string id because it is a unique number.  We use it later to cancel.
        mNM.notify(R.string.service_started, notification);
    }
}
