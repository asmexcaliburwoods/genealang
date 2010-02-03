package egp.genealang.i18n;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class I18n {
	public static ResourceBundle getBundle(String key){
		return PropertyResourceBundle.getBundle(key);
	}
}
