package egp.genealang.tostring;

import java.text.MessageFormat;

import egp.genealang.infra.Caller;
import egp.genealang.infra.NamedCaller;
import egp.genealang.infra.NamedVersionedCaller;

public class ToString {
	public static String callerToNameOfCaller(Caller caller){
		if (caller instanceof NamedCaller){
			NamedCaller namedCaller=(NamedCaller) caller;
			return namedCaller.name();
		}
		return "Unnamed caller";
	}
	public static String callerToNameAndVersionOfCaller(Caller caller){
		String version="Unknown";
		if(caller instanceof NamedVersionedCaller){ 
			NamedVersionedCaller nvcaller=(NamedVersionedCaller) caller;
			version=nvcaller.versionString();
		}
		return new MessageFormat("{0} v.{1}").format(new String[]{callerToNameOfCaller(caller),version});
	}
}
