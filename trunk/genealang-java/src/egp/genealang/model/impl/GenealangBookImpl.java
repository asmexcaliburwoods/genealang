package egp.genealang.model.impl;

import egp.genealang.model.GenealangBook;
import egp.genealang.model.GenealangShelf;
import gtd.GTD;

public abstract class GenealangBookImpl extends GenealangNodeImpl implements GenealangBook{
	private final GenealangShelf shelf;
	private final String bookLocationDescriptiveNameForErrorReporting;

	public GenealangBookImpl(
			GenealangShelf shelf,
			String bookLocationDescriptiveNameForErrorReporting) {
		super();
		this.shelf = shelf;
		this.bookLocationDescriptiveNameForErrorReporting = bookLocationDescriptiveNameForErrorReporting;
	}

	@Override
	public GenealangShelf getShelf() {
		return shelf;
	}

	@Override
	public String getBookLocationDescriptiveNameForErrorReporting() {
		return bookLocationDescriptiveNameForErrorReporting;
	}

}
