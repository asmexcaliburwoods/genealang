package egp.genealang.model.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import egp.genealang.infra.NamedVersionedCaller;
import egp.genealang.model.GenealangShelf;
import egp.sphere.impl.NullSphereImpl;
import egp.sphere.loader.SphereLoader;
import egp.sphere.loader.SphereLoader.SyntaxException;
import egp.sphere.model.Sphere;

public class GenealangShelfImplLoader {
	public GenealangShelf load(NamedVersionedCaller nvc, final File folder) throws IOException, SyntaxException{
		Sphere librarySphere=null;
		try {
			librarySphere = SphereLoader.lowlevel_fromDotSphereFile(
					new File(folder, "genealang.node.sphere")).get();
		} catch (FileNotFoundException e) {
			librarySphere=null;
		}
		return new FolderShelfImpl(nvc, this, librarySphere, folder, null);
	}
}
