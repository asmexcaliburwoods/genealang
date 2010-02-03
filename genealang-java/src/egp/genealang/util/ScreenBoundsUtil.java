package egp.genealang.util;

import java.awt.*;

public class ScreenBoundsUtil {
    public static Rectangle getScreenBounds(){
        //see also: GraphicsConfiguration.getBounds()
        GraphicsConfiguration gconf=
                GraphicsEnvironment.getLocalGraphicsEnvironment()
                        .getDefaultScreenDevice().getDefaultConfiguration();
        Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
        Insets i=Toolkit.getDefaultToolkit().getScreenInsets(gconf);
        return new Rectangle(
                i.left,
                i.top,
                d.width-i.left-i.right,
                d.height-i.top-i.bottom
        );
    }
}
