package ch.zhaw.ba10_bsha_1.fingerpad;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class Pad extends Activity implements IObserver {


    private static final int MENU_ITEM_INC    = Menu.FIRST;
    private static final int MENU_ITEM_DEC    = Menu.FIRST + 1;
    private static final int MENU_ITEM_POINTS = Menu.FIRST + 2;
    private static final int MENU_ITEM_DETECTION_STRATEGY = Menu.FIRST + 3;
    private static final int MENU_ITEM_CLEAR  = Menu.FIRST + 4;
    
	private PadView padView;
	private TextView textOut;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        padView = (PadView) findViewById(R.id.padView);
        textOut = (TextView) findViewById(R.id.textOut);
        padView.attachObserver(this);
        padView.setToShowPoints(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_ITEM_INC, 0, "Inc").setShortcut('1', 'i');
        menu.add(0, MENU_ITEM_DEC, 0, "Dec").setShortcut('2', 'd');
        menu.add(0, MENU_ITEM_POINTS, 0, "Points").setShortcut('3', 'p');
        menu.add(0, MENU_ITEM_DETECTION_STRATEGY, 0, "Strategy").setShortcut('4', 's');
        menu.add(0, MENU_ITEM_CLEAR, 0, "Clear").setShortcut('5', 'c');
        return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
            	IMicroGestureDetectionStrategy active_strategy = padView.getDetectionStrategy(); 
            	if (active_strategy instanceof MicroGestureDetectionStrategyNone) {
            		selected_strategy = 0;
            	} else if (active_strategy instanceof MicroGestureDetectionStrategyPreditction) {
            		selected_strategy = 1;
            	} else if (active_strategy instanceof MicroGestureDetectionStrategyCurvature) {
            		selected_strategy = 2;
            	}
            	AlertDialog.Builder builder = new AlertDialog.Builder(this);
            	builder.setTitle("MicroGesture Detection Strategy");
            	builder.setSingleChoiceItems(strategies, selected_strategy, new DialogInterface.OnClickListener() {
            	    public void onClick(DialogInterface dialog, int item) {
            	        switch (item) {
            	        	case 0 :
            	        		padView.setDetectionStrategy(TouchInput.DETECTION_STRATEGY_NONE, true);
            	        		break;
            	        	case 1 :
            	        		padView.setDetectionStrategy(TouchInput.DETECTION_STRATEGY_PREDICTION, true);
            	        		break;
            	        	case 2 :
            	        		padView.setDetectionStrategy(TouchInput.DETECTION_STRATEGY_CURVATURE, true);
            	        		break;
            	        }
            	        dialog.dismiss();
            	    }
            	});
            	AlertDialog alert = builder.create();
            	alert.show();
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
			textOut.setText(padView.getLastDetectionReport());
		}
	}
}