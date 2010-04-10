package egp.genealang.model.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import egp.genealang.infra.NamedVersionedCaller;
import egp.genealang.model.GenealangBook;
import egp.genealang.model.GenealangShelf;
import egp.sphere.impl.NullSphereImpl;
import egp.sphere.loader.SphereLoader;
import egp.sphere.loader.SphereLoader.SyntaxException;
import egp.sphere.model.Sphere;

public class GenealangBookImplLoader {
	public GenealangBook load(NamedVersionedCaller nvc, final File bookfile, FolderShelfImpl shelf) throws IOException, SyntaxException{
		Sphere bookSphere = SphereLoader.lowlevel_fromDotSphereFile(
				bookfile).get();
		return new PlainTextGenealangFileBookImpl(this, bookSphere, shelf, bookfile);
	}
}
