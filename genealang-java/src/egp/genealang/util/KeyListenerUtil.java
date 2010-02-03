package egp.genealang.util;

import java.awt.*;
import java.awt.event.KeyListener;

public class KeyListenerUtil {
    public static void addKeyListenerRecursively(Component c, KeyListener kl){
        c.addKeyListener(kl);
        if(c instanceof Container){
            Container container=(Container)c;
            Component[] components=container.getComponents();
            if(components==null)return;
            for(int i=0;i<components.length;i++){
                Component component=components[i];
                addKeyListenerRecursively(component,kl);
            }
        }
    }
    public static void removeKeyListenerRecursively(Component c,KeyListener kl){
        c.removeKeyListener(kl);
        if(c instanceof Container){
            Container container=(Container)c;
            Component[] components=container.getComponents();
            if(components==null)return;
            for(int i=0;i<components.length;i++){
                Component component=components[i];
                removeKeyListenerRecursively(component,kl);
            }
        }
    }
}
