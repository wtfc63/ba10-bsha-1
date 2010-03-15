package ch.zhaw.ba10_bsha_1.fingerpad;


import java.util.ArrayList;
import java.util.Collection;


public class Character {

	
	private ArrayList<MicroGesture> microGestures;
	private char detectedCharacter;
	private float detectionProbability;
	

	public Character() {
		microGestures = new ArrayList<MicroGesture>();
		detectedCharacter = '\0';
		detectionProbability = 0;
	}
	
	public Character(Collection<MicroGesture> micro_gestures) {
		this();
		microGestures = new ArrayList<MicroGesture>(micro_gestures);
	}
	
	
	public void addMicroGesture(MicroGesture micro_gesture) {
		microGestures.add(micro_gesture);
	}

	public ArrayList<MicroGesture> getMicroGestures() {
		return microGestures;
	}


	public char getDetectedCharacter() {
		return detectedCharacter;
	}

	public float getDetectionProbability() {
		return detectionProbability;
	}

	public void setDetectedCharacter(char character) {
		this.detectedCharacter = character;
	}

	public void setDetectionProbability(float probability) {
		this.detectionProbability = probability;
	}
	
	
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append((detectedCharacter != '\0') ? detectedCharacter : "none");
		result.append(" (");
		result.append(detectionProbability);
		result.append(')');
		return result.toString();
	}
}
