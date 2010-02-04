package egp.genealang.model.impl;

import java.io.File;

import egp.genealang.infra.NamedCaller;
import egp.genealang.model.GenealangLibrary;
import egp.genealang.proxy.GenealangProxy;
import egp.sphere.loader.SphereLoader;
import egp.sphere.model.Sphere;

public class GenealangLibraryImplFolderLoader {
	public GenealangLibrary load(NamedCaller nc, final File folder){
		final Sphere librarySphere=new SphereLoader().lowlevel_loadDotSphereFile(new File(folder, "genealang.library.sphere"));
		//GTD GenealangLibraryImplFolderLoader.load
		return new GenealangLibraryImplFolder() {
			final Sphere librarySpherePrivate=librarySphere;
			@Override
			public String getLibraryDisplayNameLong(GenealangProxy proxy) {
				String libName=folder.getAbsolutePath();
				if(librarySphere!=null){
					libName=librarySphere.getUniqueSphereLabeled("genealang.library.display.name.long").concatenateAllContentsNoLabel(256);
				}
				return libName;
			}
		};
	}
}
