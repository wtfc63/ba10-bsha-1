package ch.zhaw.ba10_bsha_1.fingerpad;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class Pad extends Activity implements IObserver {


    private static final int MENU_ITEM_INC    = Menu.FIRST;
    private static final int MENU_ITEM_DEC    = Menu.FIRST + 1;
    private static final int MENU_ITEM_POINTS = Menu.FIRST + 2;
    private static final int MENU_ITEM_CLEAR  = Menu.FIRST + 3;
    
	private PadView padView;
	private TextView textOut;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        padView = (PadView) findViewById(R.id.padView);
        textOut = (TextView) findViewById(R.id.textOut);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_ITEM_INC, 0, "Inc").setShortcut('1', 'i');
        menu.add(0, MENU_ITEM_DEC, 0, "Dec").setShortcut('2', 'd');
        menu.add(0, MENU_ITEM_POINTS, 0, "Points").setShortcut('3', 'p');
        menu.add(0, MENU_ITEM_CLEAR, 0, "Clear").setShortcut('4', 'c');
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