package egp.genealang.libtreenodeimpl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

import egp.genealang.infra.NamedVersionedCaller;
import egp.genealang.model.GenealangBook;
import egp.genealang.model.GenealangShelf;
import egp.genealang.util.ExceptionUtil;
import egp.genealang.util.IteratorEnumeration;
import gtd.GTD;

public class GenealangShelfTreeNodeImpl implements TreeNode{
	private final String displayNameLong;
	private final GenealangShelf shelf;
	private final List<TreeNode> treeNodes;
	private final TreeNode parent;
	private NamedVersionedCaller nvc;
	
	public GenealangShelfTreeNodeImpl(NamedVersionedCaller nvc, GenealangShelf shelf, TreeNode parent) throws Exception {
		super();
		this.nvc=nvc;
		this.parent=parent;
		this.shelf=shelf;
		String libraryDisplayNameLong = null;
		try {
			libraryDisplayNameLong=shelf.getDisplayName();
		} catch (Exception e) {
			throw new BadLibraryStructureException("Bad library structure at "+shelf.getShelfLocationDescriptiveNameForErrorReporting()+". "+e,e);
		}
		this.displayNameLong = libraryDisplayNameLong;
		if(displayNameLong==null)throw new AssertionError("displayNameLong==null");
		treeNodes=new ArrayList<TreeNode>(
				shelf.getSubshelves().size()+
				shelf.getBooks().size());
		convertGenealangShelfToTreeNodes();
	}

	public String toString(){
		return displayNameLong;
	}
	
	private void convertGenealangShelfToTreeNodes() throws Exception {
		for(GenealangShelf subshelf:shelf.getSubshelves()){
			treeNodes.add(new GenealangShelfTreeNodeImpl(nvc, subshelf, this));
		}
		for(GenealangBook book:shelf.getBooks()){
			try {
				treeNodes.add(new GenealangBookTreeNodeImpl(book, this));
			} catch (Exception e) {
				ExceptionUtil.handleException(nvc, e);
			}
		}
	}

	public String getDisplayName() {
		return displayNameLong;
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return treeNodes.get(childIndex);
	}

	@Override
	public int getChildCount() {
		return treeNodes.size();
	}

	@Override
	public Enumeration<TreeNode> children() {
		return new IteratorEnumeration<TreeNode>(treeNodes.iterator());
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public int getIndex(TreeNode node) {
		for(int i=0;;i++){
			if(i>=treeNodes.size())break;
			if(treeNodes.get(i).equals(node))return i;
		}
		return -1;
	}

	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public boolean isLeaf() {
		return false;
	}
}
