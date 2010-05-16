package ch.zhaw.ba10_bsha_1;


import android.app.Service;
import android.os.Parcel;
import android.os.Parcelable;


/**
 * Argument to a Strategy that implements {@link Parcelable} and 
 * can therefore be send to and from a {@link Service}.
 * 
 * @author Julian Hanhart, Dominik Giger
 */
public class StrategyArgument implements Parcelable {


	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------
	
	
	private String strategyName;
	private String argumentName;
	private String argumentValue;
	private String description;

	
	//---------------------------------------------------------------------------
	// Constructors and Creators
	//---------------------------------------------------------------------------
	
	
	public StrategyArgument(String strat_name, String arg_name, String arg_val, String descr) {
		strategyName  = strat_name;
		argumentName  = arg_name;
		argumentValue = arg_val;
		description   = descr;
	}

	public StrategyArgument(String strat_name, String arg_name, String descr) {
		this(strat_name, arg_name, null, descr);
	}

	public StrategyArgument(String arg_name, String arg_val) {
		this(null, arg_name, arg_val, null);
	}
	
	public StrategyArgument(Parcel source) {
		readFromParcel(source);
	}

	public static final Parcelable.Creator<StrategyArgument> CREATOR = new Parcelable.Creator<StrategyArgument>() {
		public StrategyArgument createFromParcel(Parcel in) {
			return new StrategyArgument(in);
		}

		public StrategyArgument[] newArray(int size) {
			return new StrategyArgument[size];
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
		dest.writeString(strategyName);
		dest.writeString(argumentName);
		dest.writeString(argumentValue);
		dest.writeString(description);
	}

	public void readFromParcel(Parcel source) {
		strategyName  = source.readString();
		argumentName  = source.readString();
		argumentValue = source.readString();
		description   = source.readString();
	}

	
	//---------------------------------------------------------------------------
	// Getter-/Setter-methods
	//---------------------------------------------------------------------------

	
	public String getStrategyName() {
		return strategyName;
	}

	public void setStrategyName(String strat_name) {
		strategyName = strat_name;
	}

	
	public String getArgumentName() {
		return argumentName;
	}

	public void setArgumentName(String arg_name) {
		argumentName = arg_name;
	}
	

	public boolean isSet() {
		return (argumentValue != null);
	}
	
	public String getArgumentValue() {
		return argumentValue;
	}

	public void setArgumentValue(String arg_val) {
		argumentValue = arg_val;
	}
	
	
	public String getDescription() {
		return description;
	}

	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("Argument (");
		result.append(strategyName);
		result.append("): ");
		result.append(argumentName);
		result.append(" = \"");
		result.append(argumentValue);
		result.append('\"');
		return result.toString();
    }
}