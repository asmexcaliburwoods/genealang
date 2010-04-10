package egp.genealang.model;

public interface GenealangBook extends GenealangNode{
	GenealangShelf getShelf();
	String getBookLocationDescriptiveNameForErrorReporting();
}
