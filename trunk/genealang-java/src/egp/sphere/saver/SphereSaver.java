package egp.sphere.saver;

import java.io.File;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import egp.genealang.i18n.I18n;
import egp.genealang.infra.NamedCaller;
import egp.genealang.util.MsgBoxUtil;
import egp.sphere.model.Sphere;

public class SphereSaver {
	private static ResourceBundle i18n=I18n.getBundle(SphereSaver.class.getName());
	//GTD move MSGBOXES and negotiation into callbacks
	public void lowLevel_saveDotSphereFile(NamedCaller nc, Sphere sphere, File file){
//		@return  <code>true</code> if and only if the directory was created,
//	     *          along with all necessary parent directories; <code>false</code>
//	     *          otherwise
	     while(!file.getParentFile().mkdirs()){
	    	 int answer=MsgBoxUtil.askYesNo(nc, 
	    			 i18n.getString("error.cannot.create.parent.dirs.title"), 
	    			 new MessageFormat(i18n.getString("error.cannot.create.parent.dirs.message.format")).format(new String[]{
	    					 file.getParentFile().getAbsolutePath(),
	    					 file.getName()
	    			 }), 
	    			 new String[]{
 		 				i18n.getString("error.cannot.create.parent.dirs.retry"),
 		 				i18n.getString("error.cannot.create.parent.dirs.cancel")
	    	 		 }, 0);
	    	 if(answer==0)continue;
	    	 throw new RuntimeException(
	    			 new MessageFormat(i18n.getString("error.cannot.create.parent.dirs.canceled.message.format")).format(new String[]{
	    					 file.getParentFile().getAbsolutePath(),
	    					 file.getName()
	    			 }));	    			 
	     }
	     //mkdirs successful
	     //GTD
	}
}
