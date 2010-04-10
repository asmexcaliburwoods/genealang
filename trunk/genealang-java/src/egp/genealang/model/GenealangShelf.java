package egp.genealang.model;

import java.util.List;

public interface GenealangShelf extends GenealangNode{
	List<GenealangBook> getBooks();
	List<GenealangShelf> getSubshelves() throws Exception;
	GenealangShelf getSupershelf();
	String getShelfLocationDescriptiveNameForErrorReporting();
}
