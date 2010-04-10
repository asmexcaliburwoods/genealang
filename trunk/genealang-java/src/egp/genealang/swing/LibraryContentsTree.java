/**
 * 
 */
package egp.genealang.swing;

import gtd.GTD;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;

final class LibraryContentsTree extends JTree {
	/**
	 * 
	 */
	private final SwingConsole swingConsole;

	LibraryContentsTree(SwingConsole swingConsole, TreeModel newModel) {
		super(newModel);
		this.swingConsole = swingConsole;
		setRootVisible(false);
		setShowsRootHandles(true);
	}

	/**
     * Called by the renderers to convert the specified value to
     * text. This implementation returns <code>value.toString</code>, ignoring
     * all other arguments. To control the conversion, subclass this 
     * method and use any of the arguments you need.
     * 
     * @param value the <code>Object</code> to convert to text
     * @param selected true if the node is selected
     * @param expanded true if the node is expanded
     * @param leaf  true if the node is a leaf node
     * @param row  an integer specifying the node's display row, where 0 is 
     *             the first row in the display
     * @param hasFocus true if the node has the focus
     * @return the <code>String</code> representation of the node's value
     */
    public String convertValueToText(Object value, boolean selected,
                                     boolean expanded, boolean leaf, int row,
                                     boolean hasFocus) {
        if(value != null) {
        	String sValue = null;
        	if (value instanceof LibraryRootTreeNode){
        		LibraryRootTreeNode tn=(LibraryRootTreeNode) value;
        		sValue=tn.getTreeDisplayName();
        	}else
        		sValue = value.toString();
            if (sValue != null) {
                return sValue;
            }
        }
        return "";
    }

	public void libraryContentsRefreshButtonActionPerformed() {
		// GTD Auto-generated method stub
		GTD.gtd();
		swingConsole.refreshLibraryName();
	}
}