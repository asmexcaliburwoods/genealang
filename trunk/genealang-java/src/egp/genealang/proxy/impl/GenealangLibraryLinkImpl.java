package egp.genealang.proxy.impl;

import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import egp.genealang.proxy.GenealangLibraryLink;
import egp.genealang.proxy.GenealangProxy;
import egp.sphere.impl.SphereImpl;

public class GenealangLibraryLinkImpl extends SphereImpl implements GenealangLibraryLink{
	private String displayNameLong;
	
	public GenealangLibraryLinkImpl(String displayNameLong) {
		super();
		if(displayNameLong==null)throw new NullPointerException("displayNameLong==null");
		this.displayNameLong = displayNameLong;
	}

	@Override
	public String getDisplayNameLong(GenealangProxy proxy) {
		if(proxy==null)throw new NullPointerException("proxy==null");
		return displayNameLong;
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		// GTD Auto-generated method stub
		return null;
	}

	@Override
	public int getChildCount() {
		// GTD Auto-generated method stub
		return 0;
	}

	@Override
	public Enumeration<TreeNode> getChildren() {
		// GTD Auto-generated method stub
		return null;
	}
}
