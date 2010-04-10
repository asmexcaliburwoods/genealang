package egp.genealang.libtreenodeimpl;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import egp.genealang.model.GenealangBook;
import egp.genealang.util.IteratorEnumeration;

public class GenealangBookTreeNodeImpl implements TreeNode {

	private GenealangShelfTreeNodeImpl parent;
	private GenealangBook book;
	private String displayNameLong;

	public GenealangBookTreeNodeImpl(GenealangBook book, GenealangShelfTreeNodeImpl parent) throws BadBookStructureException {
		this.book=book;
		this.parent=parent;
		String bookDisplayNameLong = null;
		try {
			bookDisplayNameLong=book.getDisplayName();
		} catch (Exception e) {
			throw new BadBookStructureException("Bad book structure at "+book.getBookLocationDescriptiveNameForErrorReporting()+". "+e,e);
		}
		this.displayNameLong = bookDisplayNameLong;
		if(displayNameLong==null)throw new AssertionError("displayNameLong==null");
	}

	public String toString(){
		return displayNameLong;
	}
	
	@Override
	public Enumeration<TreeNode> children() {
		return new IteratorEnumeration<TreeNode>(
				new ArrayList<TreeNode>().iterator());
	}

	@Override
	public boolean getAllowsChildren() {
		return false;
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getChildCount() {
		return 0;
	}

	@Override
	public int getIndex(TreeNode node) {
		throw new UnsupportedOperationException();
	}

	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public boolean isLeaf() {
		return true;
	}
}
