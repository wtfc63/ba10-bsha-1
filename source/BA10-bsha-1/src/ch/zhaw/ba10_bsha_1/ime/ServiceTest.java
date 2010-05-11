package ch.zhaw.ba10_bsha_1.ime;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

//Need the following import to get access to the app resources, since this
//class is in a sub-package.
import ch.zhaw.ba10_bsha_1.Character;
import ch.zhaw.ba10_bsha_1.R;
import ch.zhaw.ba10_bsha_1.TouchPoint;
import ch.zhaw.ba10_bsha_1.service.IDetectionService;
import ch.zhaw.ba10_bsha_1.service.IReturnRecognisedCharacters;


public class ServiceTest extends Activity {
	
	
    /** The primary interface we will be calling on the service. */
    IDetectionService detectionService = null;
    
    private boolean serviceIsBound = false;
    private boolean serviceIsRunning = false;
    private DetectionServiceConnection serviceConnection;
    
    private TextView txtViewControl;
    private TextView txtViewBinding;
    private TextView txtViewResult;
    

    /**
     * Class for interacting with the main interface of the service.
     */
    class DetectionServiceConnection implements ServiceConnection {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  We are communicating with our
            // service through an IDL interface, so get a client-side
            // representation of that from the raw service object.
        	detectionService = IDetectionService.Stub.asInterface(service);
            txtViewBinding.setText("Attached.");

            // We want to monitor the service for as long as we are
            // connected to it.
            try {
                detectionService.registerCallback(serviceCallback);
            } catch (RemoteException e) {
                // In this case the service has crashed before we could even
                // do anything with it; we can count on soon being
                // disconnected (and then reconnected if it can be restarted)
                // so there is no need to do anything here.
            }
            
            // As part of the sample, tell the user what happened.
            Toast.makeText(ServiceTest.this, R.string.service_bound, Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            detectionService = null;
            txtViewBinding.setText("Disconnected.");

            // As part of the sample, tell the user what happened.
            Toast.makeText(ServiceTest.this, R.string.service_unbound, Toast.LENGTH_SHORT).show();
        }
    };
	
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceConnection = new DetectionServiceConnection();

        setContentView(R.layout.service_test_activity);
        
        txtViewControl = (TextView) findViewById(R.id.tv_control);
        txtViewBinding = (TextView) findViewById(R.id.tv_binding);
        txtViewResult  = (TextView) findViewById(R.id.tv_result);

        // Watch for button clicks.
        Button button = (Button)findViewById(R.id.start);
        button.setOnClickListener(serviceStartListener);
        button = (Button)findViewById(R.id.stop);
        button.setOnClickListener(serviceStopListener);
        button = (Button)findViewById(R.id.bind);
        button.setOnClickListener(serviceBindListener);
        button = (Button)findViewById(R.id.unbind);
        button.setOnClickListener(serviceUnbindListener);
        button = (Button)findViewById(R.id.send);
        button.setOnClickListener(serviceSendListener);
    }
        
	
    private OnClickListener serviceStartListener = new OnClickListener() {
        public void onClick(View v) {
            if (!serviceIsRunning) {
            	startService(new Intent("ch.zhaw.ba10_bsha_1.DETECTION_SERVICE"));
            	serviceIsRunning = true;
            	Toast.makeText(ServiceTest.this, R.string.service_started, Toast.LENGTH_SHORT).show();
            	txtViewControl.setText(R.string.service_started);
            } else {
            	Toast.makeText(ServiceTest.this, R.string.service_already_running, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private OnClickListener serviceStopListener = new OnClickListener() {
        public void onClick(View v) {
            stopService(new Intent("ch.zhaw.ba10_bsha_1.DETECTION_SERVICE"));
            serviceIsRunning = false;
            Toast.makeText(ServiceTest.this, R.string.service_stopped, Toast.LENGTH_SHORT).show();
        	txtViewControl.setText(R.string.service_stopped);
        }
    };

    private OnClickListener serviceBindListener = new OnClickListener() {
        public void onClick(View v) {
            if (!serviceIsBound) {
	            // Establish a couple connections with the service, binding
	            // by interface names.  This allows other applications to be
	            // installed that replace the remote service by implementing
	            // the same interface.
            	serviceIsBound =  bindService(new Intent(IDetectionService.class.getName()), serviceConnection, Context.BIND_AUTO_CREATE);
	            txtViewBinding.setText("Binding.");
            } else {
            	Toast.makeText(ServiceTest.this, R.string.service_bound, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private OnClickListener serviceUnbindListener = new OnClickListener() {
        public void onClick(View v) {
            if (serviceIsBound) {
                // If we have received the service, and hence registered with
                // it, then now is the time to unregister.
                if (detectionService != null) {
                    try {
                        detectionService.unregisterCallback(serviceCallback);
                    } catch (RemoteException e) {
                        // There is nothing special we need to do if the service
                        // has crashed.
                    }
                }
                
                // Detach our existing connection.
                unbindService(serviceConnection);
                serviceIsBound = false;
                txtViewBinding.setText("Unbinding.");
            } else {
            	Toast.makeText(ServiceTest.this, R.string.service_not_running, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private OnClickListener serviceSendListener = new OnClickListener() {
        public void onClick(View v) {
            if (serviceIsBound) {
                // If we have received the service, and hence registered with
                // it, then now is the time to unregister.
                if (detectionService != null) {
                    try {
                    	ArrayList<TouchPoint> tmp = new ArrayList<TouchPoint>(1);
                    	tmp.add(new TouchPoint(10, 10));
                    	tmp.add(new TouchPoint(20, 20));
                        detectionService.addTouchPoints(tmp);
                    	/*TouchPoint p = tmp.get(0);
                    	detectionService.addTouchPoint(p.x, p.y, p.getStrength(), p.getTimeStamp());
                    	p = tmp.get(1);
                    	detectionService.addTouchPoint(p.x, p.y, p.getStrength(), p.getTimeStamp());*/
                    	detectionService.endSample();
                    } catch (RemoteException e) {
                        // There is nothing special we need to do if the service
                        // has crashed.
                    }
                }
            }
        }
    };
    
    // ----------------------------------------------------------------------
    // Code showing how to deal with callbacks.
    // ----------------------------------------------------------------------
    
    /**
     * This implementation is used to receive callbacks from the remote
     * service.
     */
    private IReturnRecognisedCharacters serviceCallback = new IReturnRecognisedCharacters.Stub() {
        /**
         * This is called by the remote service regularly to tell us about
         * new values.  Note that IPC calls are dispatched through a thread
         * pool running in each process, so the code executing here will
         * NOT be running in our main thread like most other things -- so,
         * to update the UI, we need to use a Handler to hop over there.
         */
		@Override
		public void recognisedCharacters(List<ch.zhaw.ba10_bsha_1.Character> characters) throws RemoteException {
			serviceHandler.sendMessage(serviceHandler.obtainMessage(CHARS_RESULT_MSG, characters));
		}

		@Override
		public void recognisedChar(char character, float probability) throws RemoteException {
			serviceHandler.sendMessage(serviceHandler.obtainMessage(CHAR_RESULT_MSG, character, Math.round(probability * 1000000)));
		}
    };
    
    private static final int CHARS_RESULT_MSG = 1;
    private static final int CHAR_RESULT_MSG  = 2;
    
    private Handler serviceHandler = new Handler() {
        @Override public void handleMessage(Message msg) {
            switch (msg.what) {
                case CHARS_RESULT_MSG:
                	ArrayList<Character> chars = (ArrayList<Character>) msg.obj;
                	if (chars.size() > 0) {
                		txtViewResult.setText("Received from service: " + chars.get(0).toString());
                	} else {
                		txtViewResult.setText("Received from service: nothing");
                	}
                    break;
                case CHAR_RESULT_MSG:
                	char recogn_char = (char) msg.arg1;
                	float prob = (float) (msg.arg2 / 1000000.0);
                	txtViewResult.setText(txtViewResult.getText().toString() + recogn_char + "(" + prob + ")");
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
        
    };
}
