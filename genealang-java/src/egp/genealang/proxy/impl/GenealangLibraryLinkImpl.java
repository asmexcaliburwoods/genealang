package egp.genealang.proxy.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

import egp.genealang.model.GenealangLibrary;
import egp.genealang.proxy.GenealangLibraryLink;
import egp.genealang.proxy.GenealangProxy;
import egp.sphere.impl.SphereImpl;
import egp.sphere.model.NullSphere;
import egp.sphere.model.Sphere;
import gtd.GTD;

public class GenealangLibraryLinkImpl extends SphereImpl implements GenealangLibraryLink{
	private final String displayNameLong;
	private final GenealangLibrary library;
	private final List<TreeNode> treeNodes;
	private final File folder;
	
	public GenealangLibraryLinkImpl(String displayNameLong, GenealangLibrary lib, File folder) {
		super();
		if(displayNameLong==null)throw new AssertionError("displayNameLong==null");
		this.displayNameLong = displayNameLong;
		this.library=lib;
		this.folder=folder;
		treeNodes=new ArrayList<TreeNode>();
		convertGenealangLibraryToTreeNodes();
	}

	private void convertGenealangLibraryToTreeNodes() {
		for()
		
		// GTD Auto-generated method stub
		GTD.gtd();
	}

	@Override
	public String getDisplayNameLong(GenealangProxy proxy) {
		if(proxy==null)throw new NullPointerException("proxy==null");
		return displayNameLong;
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return childItems.get(childIndex);
	}

	@Override
	public int getChildCount() {
		return childItems.size();
	}

	@Override
	public Enumeration<TreeNode> getChildren() {
		// GTD Auto-generated method stub
		return null;
	}
}
