package egp.genealang.model;

import java.util.List;

import egp.genealang.proxy.GenealangProxy;
import egp.sphere.model.Sphere.DuplicateLabelFoundException;
import egp.sphere.model.Sphere.LabelNotFoundException;

public interface GenealangFolder {
	String getFolderDisplayNameLong(GenealangProxy proxy) throws LabelNotFoundException, DuplicateLabelFoundException;
	List<GenealangFolder> listFolders();
	List<GenealangBook> listBooks();
}
