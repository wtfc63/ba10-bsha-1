package ch.zhaw.ba10_bsha_1.fingerpad;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;


public class Pad extends Activity implements IObserver, OnClickListener {


    private static final int MENU_ITEM_INC    = Menu.FIRST;
    private static final int MENU_ITEM_DEC    = Menu.FIRST + 1;
    private static final int MENU_ITEM_POINTS = Menu.FIRST + 2;
    private static final int MENU_ITEM_DETECTION_STRATEGY = Menu.FIRST + 3;
    private static final int MENU_ITEM_LOG    = Menu.FIRST + 4;
    private static final int MENU_ITEM_CLEAR  = Menu.FIRST + 5;
    
	private PadView padView;
	private TextView txtOut;
	private TextView txtDetectedChars;
	private ToggleButton tglBtnLog;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        padView = (PadView) findViewById(R.id.padView);
        txtOut = (TextView) findViewById(R.id.txtOut);
        txtDetectedChars = (TextView) findViewById(R.id.txtDetectedChars);
        tglBtnLog = (ToggleButton) findViewById(R.id.tglBtnLog);
        
        padView.attachObserver(this);
        padView.setToShowPoints(true);
        tglBtnLog.setOnClickListener(this);
    }
    
	@Override
	public void onClick(View view) {
		if (view.equals(tglBtnLog)) {
			if (tglBtnLog.isChecked()) {
				AlertDialog.Builder alert = new AlertDialog.Builder(this);  
				  
				alert.setTitle("Logging");  
				alert.setMessage("Character to attempt:");  
				  
				// Set an EditText view to get user input   
				final EditText input = new EditText(this);
				if ((DetectionLogger.getInstance().getAttemptedChars() != null) 
						&& (DetectionLogger.getInstance().getAttemptedChars().length > 0)) {
					char[] chars = DetectionLogger.getInstance().getAttemptedChars();
					String init_value = "";
					for (int i = 0; i < chars.length; i++) {
						init_value += chars[i];
						if (i < (chars.length - 1)) {
							init_value += ',';
						}
					}
					input.setText(init_value);
				}
				alert.setView(input);  
				  
				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {  
				public void onClick(DialogInterface dialog, int whichButton) {
						String value = input.getText().toString();
						String[] splitted_value = value.split(",");
						char[] chars = new char[splitted_value.length];
						for (int i = 0; i < chars.length; i++) {
							chars[i] = splitted_value[i].charAt(0);
						}
						DetectionLogger.getInstance().setAttemptedChars(chars);
					}  
				});  
				  
				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {  
					public void onClick(DialogInterface dialog, int whichButton) {
						tglBtnLog.setChecked(false);
					}  
				});  
				  
				alert.show();
			}
			padView.enableLog(tglBtnLog.isChecked());
		}
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_ITEM_INC, 0, "Inc").setShortcut('1', 'i');
        menu.add(0, MENU_ITEM_DEC, 0, "Dec").setShortcut('2', 'd');
        menu.add(0, MENU_ITEM_POINTS, 0, "Points").setShortcut('3', 'p');
        menu.add(0, MENU_ITEM_DETECTION_STRATEGY, 0, "Strategy").setShortcut('4', 's');
        menu.add(0, MENU_ITEM_LOG, 0, "Show Log").setShortcut('5', 'l');
        menu.add(0, MENU_ITEM_CLEAR, 0, "Clear").setShortcut('6', 'c');
        return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	AlertDialog.Builder builder;
        switch (item.getItemId()) {
            case MENU_ITEM_INC:
                padView.incStrokeWidth(8);
                return true;
            case MENU_ITEM_DEC:
                padView.incStrokeWidth(-8);
                return true;
            case MENU_ITEM_POINTS:
                padView.setToShowPoints(!padView.showsPoints());
                return true;
            case MENU_ITEM_DETECTION_STRATEGY:
            	final CharSequence[] strategies = {"None", "Prediction", "Curvature"};
            	int selected_strategy = -1;
            	IMicroGestureDetectionStrategy active_strategy = padView.getMicroGestureDetectionStrategy(); 
            	if (active_strategy instanceof MicroGestureDetectionStrategyNone) {
            		selected_strategy = 0;
            	} else if (active_strategy instanceof MicroGestureDetectionStrategyPreditction) {
            		selected_strategy = 1;
            	} else if (active_strategy instanceof MicroGestureDetectionStrategyCurvature) {
            		selected_strategy = 2;
            	}
            	builder = new AlertDialog.Builder(this);
            	builder.setTitle("MicroGesture Detection Strategy");
            	builder.setSingleChoiceItems(strategies, selected_strategy, new DialogInterface.OnClickListener() {
            	    public void onClick(DialogInterface dialog, int item) {
            	    	ICharacterDetectionStrategy old_strat = padView.getCharacterDetectionStrategy();
            	        switch (item) {
            	        	case 0 :
            	        		padView.setDetectionStrategies(TouchInput.MG_DETECTION_STRATEGY_NONE, old_strat, true);
            	        		break;
            	        	case 1 :
            	        		padView.setDetectionStrategies(TouchInput.MG_DETECTION_STRATEGY_PREDICTION, old_strat, true);
            	        		break;
            	        	case 2 :
            	        		padView.setDetectionStrategies(TouchInput.MG_DETECTION_STRATEGY_CURVATURE, old_strat, true);
            	        		break;
            	        }
            	        dialog.dismiss();
            	    }
            	});
            	AlertDialog alert = builder.create();
            	alert.show();
                return true;
            case MENU_ITEM_LOG:
            	builder = new AlertDialog.Builder(this);
                builder.setTitle("Log");
                builder.setMessage(DetectionLogger.getInstance().getLog(true));
                AlertDialog alert_log = builder.create();
                alert_log.show();
            	return true;
            case MENU_ITEM_CLEAR:
            	padView.clear();
            	return true;
        }
        return (super.onOptionsItemSelected(item));
    }

	@Override
	public void update(IObservable updater) {
		if (updater instanceof PadView) {
			txtOut.setText(padView.getLastMicroGestureDetectionReport());
			txtDetectedChars.setText(padView.getLastCharacterDetectionReport());
		}
	}
}