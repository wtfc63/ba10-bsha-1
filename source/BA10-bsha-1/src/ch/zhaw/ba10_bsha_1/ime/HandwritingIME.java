package ch.zhaw.ba10_bsha_1.ime;


import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.text.method.MetaKeyKeyListener;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.zhaw.ba10_bsha_1.Character;
import ch.zhaw.ba10_bsha_1.R;
import ch.zhaw.ba10_bsha_1.StrategyArgument;
import ch.zhaw.ba10_bsha_1.TouchPoint;
import ch.zhaw.ba10_bsha_1.service.DetectionService;
import ch.zhaw.ba10_bsha_1.service.IDetectionService;
import ch.zhaw.ba10_bsha_1.service.IReturnResults;


/**
 * A basic implementation of the {@link InputMethodService} for our IME. Deals with 
 * the connection to the {@link DetectionService} and lets a {@link PadView} handle 
 * the gathering of the input data.
 * 
 * Based on the SoftKeyboard example from the Android SDK.
 */
public class HandwritingIME extends InputMethodService implements KeyboardView.OnKeyboardActionListener, IObserver {


	//---------------------------------------------------------------------------
	// Inner Class
	//---------------------------------------------------------------------------

    
    /**
     * Class for interacting with the main interface of the service.
     */
    class DetectionServiceConnection implements ServiceConnection {
    	/**
    	 * Called when the connection with the service has been established, 
    	 * giving us the service object we can use to interact with the service.
    	 */
        public void onServiceConnected(ComponentName className, IBinder service) {
        	detectionService = IDetectionService.Stub.asInterface(service);
            try {
                detectionService.registerCallback(serviceCallback);
            } catch (RemoteException e) {}
            Toast.makeText(HandwritingIME.this, R.string.service_bound, Toast.LENGTH_SHORT).show();
        }

        /**
         * Called when the service is unbound
         */
        public void onServiceDisconnected(ComponentName className) {
            detectionService = null;
            Toast.makeText(HandwritingIME.this, R.string.service_unbound, Toast.LENGTH_SHORT).show();
        }
    };


	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------

    
    private static final boolean PROCESS_HARD_KEYS = true;
    
    private IDetectionService detectionService = null;
    private DetectionServiceConnection serviceConnection;
    private boolean serviceIsBound = false;
    private boolean serviceIsRunning = false;
    
    private PadView padView;
    private boolean sendFieldHeight = false;
    private CandidateView candidateView;
    private CompletionInfo[] completions;
    
    private StringBuilder composingText = new StringBuilder();
    private boolean predictionOn;
    private boolean completionOn;
    private long metaState;
    
    private LatinKeyboard padKeyboard;
    private LatinKeyboard curKeyboard;
    
    private String wordSeparators;
    

	//---------------------------------------------------------------------------
	// Initialization methods
	//---------------------------------------------------------------------------

    
    /**
     * Main initialization of the input method component. Used to start the service if it's not already running.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        serviceConnection = new DetectionServiceConnection();
        if (!serviceIsRunning) {
            Toast.makeText(HandwritingIME.this, R.string.service_starting, Toast.LENGTH_SHORT).show();
        	startService(new Intent("ch.zhaw.ba10_bsha_1.DETECTION_SERVICE"));
        	serviceIsRunning = true;
        }
        wordSeparators = getResources().getString(R.string.word_separators);
    }
    
    /**
     * Initialization of the User Interface. It is called after creation and any configuration change.
     */
    @Override
	public void onInitializeInterface() {
    	padView = (PadView) getLayoutInflater().inflate(R.layout.input, null);
        padView.setMinimumWidth(getMaxWidth());
		padView.attachObserver(this);
        padKeyboard = new LatinKeyboard(this, R.xml.pad);
    }
    
    /**
     * Called by the framework when the view for creating input needs to
     * be generated. This will be called the first time the input method
     * is displayed, and every time it needs to be re-created such as due to
     * a configuration change.
     */
    @Override
    public View onCreateInputView() {
		padView.setOnKeyboardActionListener(this);
		padView.setKeyboard(padKeyboard);
        return padView;
    }
    

	//---------------------------------------------------------------------------
	// Implementation of the InputMethodService methods (partially)
	//---------------------------------------------------------------------------

    
    /**
     * Called every time when the input method is shown, therefore we use this to bind to the {@link Service}.
     */
    @Override
    public void onStartInputView(EditorInfo attribute, boolean restarting) {
        super.onStartInputView(attribute, restarting);
    	serviceIsBound =  bindService(new Intent(IDetectionService.class.getName()), serviceConnection, Context.BIND_AUTO_CREATE);
        padView.setKeyboard(curKeyboard);
        padView.closing();
    }
    
    /**
     * Called when the input method is destroy. Since there will be no input method around afterwards,
     * we can use this to unbind the input method from the {@link Service}.
     */
    @Override
    public void onDestroy() {
    	super.onDestroy();
        if (serviceIsBound) {
            if (detectionService != null) {
                try {
                    detectionService.unregisterCallback(serviceCallback);
                } catch (RemoteException e) {}
            }
            // Detach our existing connection.
            unbindService(serviceConnection);
            serviceIsBound = false;
        }
    }


	//---------------------------------------------------------------------------
	// Implementation of the IObserver interface
	//---------------------------------------------------------------------------

    
    /**
     * Deal with changes in the {@link PadView}, mostly relaying its input to the {@link DetectionService}
     */
	@Override
	public void update(IObservable updater) {
		if (updater instanceof PadView) {
			if (serviceIsBound && (detectionService != null)) {
				try {
					InputConnection ic = getCurrentInputConnection();
					switch (padView.getEventType()) {
						case PadView.EVENT_TYPE_NEW_POINTS :
							if (!sendFieldHeight) {
					        	StrategyArgument arg = new StrategyArgument("FieldHeight", Integer.valueOf(padView.getHeight()).toString());
								detectionService.broadcastArgument(arg);
								sendFieldHeight = true;
							}
							detectionService.addTouchPoints(new ArrayList<TouchPoint>(padView.getLastPoints()), false);
							break;
						case PadView.EVENT_TYPE_TOUCH_UP :
							if (!sendFieldHeight) {
					        	StrategyArgument arg = new StrategyArgument("FieldHeight", Integer.valueOf(padView.getHeight()).toString());
								detectionService.broadcastArgument(arg);
								sendFieldHeight = true;
							}
							if (padView.getLastPoints() != null) {
								detectionService.addTouchPoints(new ArrayList<TouchPoint>(padView.getLastPoints()), true);
							} else {
								detectionService.endSample();
							}
							break;
						case PadView.EVENT_TYPE_SPACE :
							ic.commitText(" ", 1);
							break;
						case PadView.EVENT_TYPE_BACKSPACE :
							handleBackspace();
							break;
						default :
							break;
					}
				} catch (RemoteException ex) {}
			}
		}
	}
	

	//---------------------------------------------------------------------------
	// Deal with callbacks
	//---------------------------------------------------------------------------

    
	
    /**
     * This implementation is used to receive callbacks from the remote service.
     */
    private IReturnResults serviceCallback = new IReturnResults.Stub() {
		@Override
		public void recognisedCharacters(List<ch.zhaw.ba10_bsha_1.Character> characters) throws RemoteException {
			serviceHandler.sendMessage(serviceHandler.obtainMessage(CHARACTER_RESULT_MSG, characters));
		}

		@Override
		public void recognisedChar(char character, float probability) throws RemoteException {
			serviceHandler.sendMessage(serviceHandler.obtainMessage(CHAR_RESULT_MSG, character, Math.round(probability * 1000000)));
		}
    };
    
    private static final int CHARACTER_RESULT_MSG = 1;
    private static final int CHAR_RESULT_MSG  = 2;
    
    private Handler serviceHandler = new Handler() {
    	
        @SuppressWarnings("unchecked")
		@Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            	// Most used case: Retrieving one or more Characters as a List
                case CHARACTER_RESULT_MSG:
                	ArrayList<Character> chars = (ArrayList<Character>) msg.obj;
                	if (chars.size() > 0) {
                		InputConnection conn = getCurrentInputConnection();
                		StringBuffer text = new StringBuffer();
                		for (Character character : chars) {
                			if (!character.toString().startsWith("none")) {
                				text.append(character.getDetectedCharacter());
                			}
						}
                		conn.commitText(text, 1);
                	}
                    break;
                // Backup case: Retrieving a single char and its probability (MircoGestures are lost)
                case CHAR_RESULT_MSG:
                	char recogn_char = (char) msg.arg1;
                	float prob = (float) (msg.arg2 / 1000000.0);
	                if (recogn_char != '\0') {
	            		InputConnection conn = getCurrentInputConnection();
	            		StringBuffer text = new StringBuffer();
	            		text.append(recogn_char);
	                	conn.commitText(text, 1);
	                	Log.i("HandwritingIME", "Received detected character: \'" + text + "\' with a probability of " + prob);
                	}
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };


    
	//---------------------------------------------------------------------------
	// Further legacy stuff for the input method
	//---------------------------------------------------------------------------


    /**
     * Called by the framework when the view for showing candidates needs to
     * be generated, like {@link #onCreateInputView}.
     */
    @Override
    public View onCreateCandidatesView() {
        candidateView = new CandidateView(this);
        candidateView.setService(this);
        return candidateView;
    }

    /**
     * This is the main point where we do our initialization of the input method
     * to begin operating on an application.  At this point we have been
     * bound to the client, and are now receiving all of the detailed information
     * about the target of our edits.
     */
    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);
        
        // Reset our state.  We want to do this even if restarting, because
        // the underlying state of the text editor could have changed in any way.
        composingText.setLength(0);
        updateCandidates();
        
        if (!restarting) {
            // Clear shift states.
            metaState = 0;
        }
        
        predictionOn = false;
        completionOn = false;
        completions = null;
        
        // We are now going to initialize our state based on the type of
        // text being edited.
        switch (attribute.inputType & EditorInfo.TYPE_MASK_CLASS) {
            case EditorInfo.TYPE_CLASS_NUMBER:
            case EditorInfo.TYPE_CLASS_DATETIME:
                // Numbers and dates default to the symbols keyboard, with
                // no extra features.
                //mCurKeyboard = mSymbolsKeyboard;
                break;
                
            case EditorInfo.TYPE_CLASS_PHONE:
                // Phones will also default to the symbols keyboard, though
                // often you will want to have a dedicated phone keyboard.
                //mCurKeyboard = mSymbolsKeyboard;
                break;
                
            case EditorInfo.TYPE_CLASS_TEXT:
                // This is general text editing.  We will default to the
                // normal alphabetic keyboard, and assume that we should
                // be doing predictive text (showing candidates as the
                // user types).
                curKeyboard = padKeyboard;
                predictionOn = true;
                
                // We now look for a few special variations of text that will
                // modify our behavior.
                int variation = attribute.inputType &  EditorInfo.TYPE_MASK_VARIATION;
                if (variation == EditorInfo.TYPE_TEXT_VARIATION_PASSWORD ||
                        variation == EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    // Do not display predictions / what the user is typing
                    // when they are entering a password.
                    predictionOn = false;
                }
                
                if (variation == EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS 
                        || variation == EditorInfo.TYPE_TEXT_VARIATION_URI
                        || variation == EditorInfo.TYPE_TEXT_VARIATION_FILTER) {
                    // Our predictions are not useful for e-mail addresses
                    // or URIs.
                    predictionOn = false;
                }
                
                if ((attribute.inputType&EditorInfo.TYPE_TEXT_FLAG_AUTO_COMPLETE) != 0) {
                    // If this is an auto-complete text view, then our predictions
                    // will not be shown and instead we will allow the editor
                    // to supply their own.  We only show the editor's
                    // candidates when in fullscreen mode, otherwise relying
                    // own it displaying its own UI.
                    predictionOn = false;
                    completionOn = isFullscreenMode();
                }
                
                // We also want to look at the current state of the editor
                // to decide whether our alphabetic keyboard should start out
                // shifted.
                //updateShiftKeyState(attribute);
                break;
                
            default:
                // For all unknown input types, default to the alphabetic
                // keyboard with no special features.
                curKeyboard = padKeyboard;
                //updateShiftKeyState(attribute);
        }
        
        // Update the label on the enter key, depending on what the application
        // says it will do.
        curKeyboard.setImeOptions(getResources(), attribute.imeOptions);
    }

    /**
     * This is called when the user is done editing a field.  We can use
     * this to reset our state.
     */
    @Override public void onFinishInput() {
        super.onFinishInput();

        // Clear current composing text and candidates.
        composingText.setLength(0);
        updateCandidates();
        
        // We only hide the candidates window when finishing input on
        // a particular editor, to avoid popping the underlying application
        // up and down if the user is entering text into the bottom of
        // its window.
        setCandidatesViewShown(false);
        if (padView != null) {
            padView.closing();
        }
    }
    
    /**
     * Deal with the editor reporting movement of its cursor.
     */
    @Override
    public void onUpdateSelection(int oldSelStart, int oldSelEnd, int newSelStart, int newSelEnd, int candidatesStart, int candidatesEnd) {
        super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd, candidatesStart, candidatesEnd);
        
        // If the current selection in the text view changes, we should
        // clear whatever candidate text we have.
        if (composingText.length() > 0 
        		&& (newSelStart != candidatesEnd || newSelEnd != candidatesEnd)) {
            composingText.setLength(0);
            updateCandidates();
            InputConnection ic = getCurrentInputConnection();
            if (ic != null) {
                ic.finishComposingText();
            }
        }
    }

    /**
     * This tells us about completions that the editor has determined based
     * on the current text in it.  We want to use this in fullscreen mode
     * to show the completions ourself, since the editor can not be seen
     * in that situation.
     */
    @Override
    public void onDisplayCompletions(CompletionInfo[] completion_infos) {
        if (completionOn) {
            completions = completion_infos;
            if (completion_infos == null) {
                setSuggestions(null, false, false);
                return;
            }
            
            List<String> stringList = new ArrayList<String>();
            for (int i=0; i<(completion_infos != null ? completion_infos.length : 0); i++) {
                CompletionInfo ci = completion_infos[i];
                if (ci != null) stringList.add(ci.getText().toString());
            }
            setSuggestions(stringList, true, true);
        }
    }
    
    /**
     * This translates incoming hard key events in to edit operations on an
     * {@link InputConnection}.  It is only needed when using the
     * PROCESS_HARD_KEYS option.
     */
    private boolean translateKeyDown(int keyCode, KeyEvent event) {
        metaState = MetaKeyKeyListener.handleKeyDown(metaState, keyCode, event);
        int c = event.getUnicodeChar(MetaKeyKeyListener.getMetaState(metaState));
        metaState = MetaKeyKeyListener.adjustMetaAfterKeypress(metaState);
        InputConnection ic = getCurrentInputConnection();
        if (c == 0 || ic == null) {
            return false;
        }
        
        //boolean dead = false;
        if ((c & KeyCharacterMap.COMBINING_ACCENT) != 0) {
            //dead = true;
            c = c & KeyCharacterMap.COMBINING_ACCENT_MASK;
        }
        
        if (composingText.length() > 0) {
            char accent = composingText.charAt(composingText.length() -1 );
            int composed = KeyEvent.getDeadChar(accent, c);

            if (composed != 0) {
                c = composed;
                composingText.setLength(composingText.length()-1);
            }
        }
        
        onKey(c, null);
        
        return true;
    }
    
    /**
     * Use this to monitor key events being delivered to the application.
     * We get first crack at them, and can either resume them or let them
     * continue to the app.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                // The InputMethodService already takes care of the back
                // key for us, to dismiss the input method if it is shown.
                // However, our keyboard could be showing a pop-up window
                // that back should dismiss, so we first allow it to do that.
                if (event.getRepeatCount() == 0 && padView != null) {
                    if (padView.handleBack()) {
                        return true;
                    }
                }
                break;                
            case KeyEvent.KEYCODE_DEL:
                // Special handling of the delete key: if we currently are
                // composing text for the user, we want to modify that instead
                // of let the application to the delete itself.
                if (composingText.length() > 0) {
                    onKey(Keyboard.KEYCODE_DELETE, null);
                    return true;
                }
                break;                
            case KeyEvent.KEYCODE_ENTER:
                // Let the underlying text editor always handle these.
                return false;                
            default:
                // For all other keys, if we want to do transformations on
                // text being entered with a hard keyboard, we need to process
                // it and do the appropriate action.
                if (PROCESS_HARD_KEYS) {
                    if (predictionOn && translateKeyDown(keyCode, event)) {
                        return true;
                    }
                }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Use this to monitor key events being delivered to the application.
     * We get first crack at them, and can either resume them or let them
     * continue to the app.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // If we want to do transformations on text being entered with a hard
        // keyboard, we need to process the up events to update the meta key
        // state we are tracking.
        if (PROCESS_HARD_KEYS) {
            if (predictionOn) {
                metaState = MetaKeyKeyListener.handleKeyUp(metaState, keyCode, event);
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * Helper function to commit any text being composed in to the editor.
     */
    private void commitTyped(InputConnection inputConnection) {
        if (composingText.length() > 0) {
            inputConnection.commitText(composingText, composingText.length());
            composingText.setLength(0);
            updateCandidates();
        }
    }
    
    /**
     * Helper to determine if a given character code is alphabetic.
     */
    private boolean isAlphabet(int code) {
        if (java.lang.Character.isLetter(code)) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Helper to send a key down / key up pair to the current editor.
     */
    private void keyDownUp(int keyEventCode) {
        getCurrentInputConnection().sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, keyEventCode));
        getCurrentInputConnection().sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, keyEventCode));
    }
    
    /**
     * Helper to send a character to the editor as raw key events.
     */
    private void sendKey(int keyCode) {
        switch (keyCode) {
            case '\n':
                keyDownUp(KeyEvent.KEYCODE_ENTER);
                break;
            default:
                if (keyCode >= '0' && keyCode <= '9') {
                    keyDownUp(keyCode - '0' + KeyEvent.KEYCODE_0);
                } else {
                    getCurrentInputConnection().commitText(String.valueOf((char) keyCode), 1);
                }
                break;
        }
    }

    /**
     * Implementation of KeyboardViewListener
     * @see android.inputmethodservice.KeyboardView.OnKeyboardActionListener#onKey(int, int[])
     */
    public void onKey(int primaryCode, int[] keyCodes) {
        if (isWordSeparator(primaryCode)) {
            // Handle separator
            if (composingText.length() > 0) {
                commitTyped(getCurrentInputConnection());
            }
            sendKey(primaryCode);
        } else if (primaryCode == Keyboard.KEYCODE_DELETE) {
            handleBackspace();
        } else if (primaryCode == Keyboard.KEYCODE_CANCEL) {
            handleClose();
            return;
        } else {
            handleCharacter(primaryCode, keyCodes);
        }
    }

    public void onText(CharSequence text) {
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) {
        	return;
        }
        ic.beginBatchEdit();
        if (composingText.length() > 0) {
            commitTyped(ic);
        }
        ic.commitText(text, 0);
        ic.endBatchEdit();
    }

    /**
     * Update the list of available candidates from the current composing
     * text.  This will need to be filled in by however you are determining
     * candidates.
     */
    private void updateCandidates() {
        if (!completionOn) {
            if (composingText.length() > 0) {
                ArrayList<String> list = new ArrayList<String>();
                list.add(composingText.toString());
                setSuggestions(list, true, true);
            } else {
                setSuggestions(null, false, false);
            }
        }
    }
    
    public void setSuggestions(List<String> suggestions, boolean completions,
            boolean typedWordValid) {
        if (suggestions != null && suggestions.size() > 0) {
            setCandidatesViewShown(true);
        } else if (isExtractViewShown()) {
            setCandidatesViewShown(true);
        }
        if (candidateView != null) {
            candidateView.setSuggestions(suggestions, completions, typedWordValid);
        }
    }
    
    /**
     * Clean up text suggestions if needed and send the keycode for the backspace-key.
     */
    private void handleBackspace() {
        final int length = composingText.length();
        if (length > 1) {
            composingText.delete(length - 1, length);
            getCurrentInputConnection().setComposingText(composingText, 1);
            updateCandidates();
        } else if (length > 0) {
            composingText.setLength(0);
            getCurrentInputConnection().commitText("", 0);
            updateCandidates();
        } else {
            keyDownUp(KeyEvent.KEYCODE_DEL);
        }
    }
    
    /**
     * Add character to suggestion of needed or send directly through the {@link InputConnection}
     */
    private void handleCharacter(int primaryCode, int[] keyCodes) {
        if (isAlphabet(primaryCode) && predictionOn) {
            composingText.append((char) primaryCode);
            getCurrentInputConnection().setComposingText(composingText, 1);
            updateCandidates();
        } else {
            getCurrentInputConnection().commitText(String.valueOf((char) primaryCode), 1);
        }
    }

    /**
     * React on closing of the input method
     */
    private void handleClose() {
        commitTyped(getCurrentInputConnection());
        requestHideSelf(0);
        padView.closing();
    }
    
    private String getWordSeparators() {
        return wordSeparators;
    }
    
    public boolean isWordSeparator(int code) {
        String separators = getWordSeparators();
        return separators.contains(String.valueOf((char)code));
    }

    /**
     * Pick the first suggested candidate
     */
    public void pickDefaultCandidate() {
        pickSuggestionManually(0);
    }
    
    /**
     * Pick one of the suggested candidates
     * @param index
     */
    public void pickSuggestionManually(int index) {
        if (completionOn && completions != null && index >= 0 && index < completions.length) {
            CompletionInfo ci = completions[index];
            getCurrentInputConnection().commitCompletion(ci);
            if (candidateView != null) {
                candidateView.clear();
            }
        } else if (composingText.length() > 0) {
            // If we were generating candidate suggestions for the current
            // text, we would commit one of them here.  But for this sample,
            // we will just commit the current text.
            commitTyped(getCurrentInputConnection());
        }
    }
    
	@Override
	public void swipeRight() {}
    
    public void swipeLeft() {
        handleBackspace();
    }

    public void swipeDown() {
        handleClose();
    }

    public void swipeUp() {}
    public void onPress(int primaryCode) { }
    public void onRelease(int primaryCode) {}
}
