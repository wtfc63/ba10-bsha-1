import java.util.ArrayList;
import java.util.Collection;


/**
 * Represents a detected Character and includes its detection-probability and {@link MicroGesture}s. 
 * Implements {@link Parcelable} and can therefore be send to and from a {@link Service}.
 * 
 * @author Julian Hanhart, Dominik Giger
 */
public class Character implements Comparable<Character> {


	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------
	
	
	private ArrayList<MicroGesture> microGestures;
	private char detectedCharacter;
	private float detectionProbability;


	//---------------------------------------------------------------------------
	// Constructors and Creators
	//---------------------------------------------------------------------------


	public Character() {
		microGestures = new ArrayList<MicroGesture>();
		detectedCharacter = '\0';
		detectionProbability = 0;
	}
	
	public Character(Collection<MicroGesture> micro_gestures, char character, float probability) {
		this();
		detectedCharacter    = character;
		detectionProbability = probability;
		setMicroGestures(micro_gestures);
	}
	

	//---------------------------------------------------------------------------
	// Implementation of the Comparable interface
	//---------------------------------------------------------------------------


	@Override
	public int compareTo(Character another) {
		int result = 0;
		if (detectionProbability > another.detectionProbability) {
			result = -1;
		} else if (detectionProbability < another.detectionProbability) {
			result = 1;
		}
		return result;
	}
	

	//---------------------------------------------------------------------------
	// Getter-/Setter-methods
	//---------------------------------------------------------------------------
	
	
	public ArrayList<MicroGesture> getMicroGestures() {
		return microGestures;
	}
	
	public void setMicroGestures(Collection<MicroGesture> micro_gestures) {
		microGestures = (micro_gestures != null) 
			? new ArrayList<MicroGesture>(micro_gestures) : new ArrayList<MicroGesture>();
	}
	
	public void addMicroGesture(MicroGesture micro_gesture) {
		microGestures.add(micro_gesture);
	}

	
	public char getDetectedCharacter() {
		return detectedCharacter;
	}

	public void setDetectedCharacter(char character) {
		this.detectedCharacter = character;
	}

	
	public float getDetectionProbability() {
		return detectionProbability;
	}

	public void setDetectionProbability(float probability) {
		this.detectionProbability = probability;
	}
	
	
	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append((detectedCharacter != '\0') ? detectedCharacter : "none");
		result.append(" (");
		result.append(detectionProbability);
		result.append(')');
		return result.toString();
	}
}
