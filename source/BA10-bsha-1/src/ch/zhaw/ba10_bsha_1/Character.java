package ch.zhaw.ba10_bsha_1;


import android.app.Service;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collection;

import ch.zhaw.ba10_bsha_1.service.MicroGesture;


/**
 * Represents a detected Character and includes its detection-probability and {@link MicroGesture}s. 
 * Implements {@link Parcelable} and can therefore be send to and from a {@link Service}.
 * 
 * @author Julian Hanhart, Dominik Giger
 */
public class Character implements Comparable<Character>, Parcelable {


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
	
	public Character(Parcel source) {
		readFromParcel(source);
	}
	
	public static final Parcelable.Creator<Character> CREATOR = new Parcelable.Creator<Character>() {
		public Character createFromParcel(Parcel in) {
			return new Character(in);
		}

		public Character[] newArray(int size) {
			return new Character[size];
		}
	};


	//---------------------------------------------------------------------------
	// Implementation of the Parcelable interface
	//---------------------------------------------------------------------------

	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(String.valueOf(detectedCharacter));
		dest.writeFloat(detectionProbability);
		MicroGesture[] tmp = new MicroGesture[microGestures.size()];
		dest.writeParcelableArray(microGestures.toArray(tmp), flags);
	}
	
	public void readFromParcel(Parcel source) {
		detectedCharacter = source.readString().charAt(0);
		detectionProbability = source.readFloat();
		Parcelable[] tmp = source.readParcelableArray(MicroGesture.class.getClassLoader());
		microGestures = new ArrayList<MicroGesture>(tmp.length);
		for (Parcelable element : tmp) {
			microGestures.add((MicroGesture) element); 
		}
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
