package egp.genealang.util;

import java.awt.*;

public class WindowBoundsUtil {
    public static void centerWindow(Component comp){
        Dimension d=comp.getSize();
        Rectangle screenBounds=ScreenBoundsUtil.getScreenBounds();
        int x=screenBounds.x+(screenBounds.width-d.width)/2;
        int y=screenBounds.y+(screenBounds.height-d.height)/2;
        comp.setBounds(x,y,d.width,d.height);
    }
}
