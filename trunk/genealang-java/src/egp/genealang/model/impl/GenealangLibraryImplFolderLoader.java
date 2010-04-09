package egp.genealang.model.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import egp.genealang.infra.NamedCaller;
import egp.genealang.model.GenealangLibrary;
import egp.genealang.proxy.GenealangProxy;
import egp.sphere.loader.SphereLoader;
import egp.sphere.loader.SphereLoader.SyntaxException;
import egp.sphere.model.Sphere;
import egp.sphere.model.Sphere.DuplicateLabelFoundException;
import egp.sphere.model.Sphere.LabelNotFoundException;

public class GenealangLibraryImplFolderLoader {
	public GenealangLibrary load(NamedCaller nc, final File folder) throws IOException, SyntaxException{
		final Sphere librarySphere=SphereLoader.lowlevel_fromDotSphereFile(
				new File(folder, "genealang.library.sphere")).get();
		return new GenealangLibraryImplFolder() {
			final Sphere librarySpherePrivate=librarySphere;
			@Override
			public String getLibraryDisplayNameLong(GenealangProxy proxy) throws LabelNotFoundException, DuplicateLabelFoundException{
				String libName=folder.getAbsolutePath();
				if(librarySphere!=null){
					libName=
						librarySphere.getUniqueSphereLabeled("genealang.library.display.name.long")
						.concatenateAllContentsNoLabel_CharactersLengthMax(256);
				}
				return libName;
			}
		};
	}
}
