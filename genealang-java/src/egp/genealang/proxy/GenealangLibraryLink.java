package egp.genealang.proxy;

import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import egp.sphere.proxy.SphereLink;

public interface GenealangLibraryLink extends SphereLink{
	String getDisplayNameLong(GenealangProxy proxy);
	
	Enumeration<TreeNode> getChildren();
	TreeNode getChildAt(int childIndex);
	int getChildCount();
}
