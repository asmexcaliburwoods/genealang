/**
 * 
 */
package egp.genealang.swing;

import java.util.Enumeration;

import javax.swing.tree.TreeNode;

public final class LibraryRootTreeNode implements TreeNode {
	/**
	 * 
	 */
	private final SwingConsole swingConsole;

	/**
	 * @param swingConsole
	 * GTD hide root node of a library.
	 */
	LibraryRootTreeNode(SwingConsole swingConsole) {
		this.swingConsole = swingConsole;
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

	@Override
	public TreeNode getParent() {
		return null;
	}

	@Override
	public int getIndex(TreeNode node) {
		return 0;
	}

	String getTreeDisplayName(){
		return this.swingConsole.currentlySelectedLibraryLink==null?SwingConsole.i18n.getString("unspecified"):this.swingConsole.currentlySelectedLibraryLink.getDisplayNameLong(this.swingConsole.proxy);
	}
	@Override
	public int getChildCount() {
		return this.swingConsole.currentlySelectedLibraryLink==null?0:this.swingConsole.currentlySelectedLibraryLink.getChildCount();
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return this.swingConsole.currentlySelectedLibraryLink==null?null:this.swingConsole.currentlySelectedLibraryLink.getChildAt(childIndex);
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public Enumeration<TreeNode> children() {
		return this.swingConsole.currentlySelectedLibraryLink==null?null:this.swingConsole.currentlySelectedLibraryLink.getChildren();
	}

	public String getLibraryDisplayName() {
		return getTreeDisplayName();
	}
}