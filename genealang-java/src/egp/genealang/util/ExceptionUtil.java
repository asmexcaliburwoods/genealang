package egp.genealang.util;

import java.text.MessageFormat;

import egp.genealang.infra.NamedCaller;
import gtd.GTD;

public class ExceptionUtil {
    public static void handleException(NamedCaller nc, Throwable throwable) {
    	handleException(nc, "", throwable);
    }
    public static void handleException(NamedCaller nc, String error, Throwable throwable) {
		System.err.println(error+":");throwable.printStackTrace();
		try{
			try{
				MsgBoxUtil.showError(nc, error, throwable);
			}catch(Throwable tr2){
				tr2.printStackTrace();
				EnglishMsgBoxUtil.showError(nc, error, throwable);
			}
		}catch(Throwable tr){
			tr.printStackTrace();
			GTD.gtd("Print throwable to nc.log on Desktop");
		}
    }

    public static String getExceptionMessage(String error1, Throwable tr) {
        return StringUtil.isEmptyTrimmed(error1)?""+tr:new MessageFormat("{0}: {1}").format(new String[]{error1.trim(), ""+tr});
    }
}
