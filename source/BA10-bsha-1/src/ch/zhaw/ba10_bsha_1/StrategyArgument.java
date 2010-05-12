package ch.zhaw.ba10_bsha_1;


import android.os.Parcel;
import android.os.Parcelable;


public class StrategyArgument implements Parcelable {

	
	private String strategyName;
	private String argumentName;
	private String argumentValue;
	private String description;

	
	public StrategyArgument(String strat_name, String arg_name, String arg_val, String descr) {
		strategyName  = strat_name;
		argumentName  = arg_name;
		argumentValue = arg_val;
		description   = descr;
	}

	public StrategyArgument(String strat_name, String arg_name, String descr) {
		this(strat_name, arg_name, null, "");
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

	
	public String getStrategyName() {
		return strategyName;
	}

	public String getArgumentName() {
		return argumentName;
	}

	public String getArgumentValue() {
		return argumentValue;
	}

	public boolean isSet() {
		return (argumentValue != null);
	}
	
	public String getDescription() {
		return description;
	}

	public void setStrategyName(String strat_name) {
		strategyName = strat_name;
	}

	public void setArgumentName(String arg_name) {
		argumentName = arg_name;
	}

	public void setArgumentValue(String arg_val) {
		argumentValue = arg_val;
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
}