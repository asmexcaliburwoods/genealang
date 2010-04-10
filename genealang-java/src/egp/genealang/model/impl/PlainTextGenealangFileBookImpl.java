package egp.genealang.model.impl;

import java.io.File;

import egp.genealang.model.GenealangBook;
import egp.genealang.model.GenealangShelf;
import egp.sphere.model.DuplicateLabelFoundException;
import egp.sphere.model.LabelNotFoundException;
import egp.sphere.model.Sphere;
import gtd.GTD;

public class PlainTextGenealangFileBookImpl extends GenealangBookImpl {

	private GenealangBookImplLoader loader;
	private Sphere bookSphere;
	private File file;

	public PlainTextGenealangFileBookImpl(GenealangBookImplLoader loader,
			Sphere bookSphere, FolderShelfImpl folderShelfImpl, File book) {
		super(folderShelfImpl,"book file absolute path: "+book.getAbsolutePath());
		this.loader=loader;
		this.bookSphere=bookSphere;
		file=book;
	}

	@Override
	public String getDisplayName() throws LabelNotFoundException,
			DuplicateLabelFoundException {
		return bookSphere.getUniqueSphereLabeled("book.display.name")
				.concatenateAllContentsNoLabel_CharactersLengthMax(256);
	}
}
