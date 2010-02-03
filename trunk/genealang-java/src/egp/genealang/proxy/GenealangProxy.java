package egp.genealang.proxy;

import java.io.File;

import egp.genealang.infra.NamedCaller;

public interface GenealangProxy {
	GenealangLibraryLink getGenealangLibraryLink(NamedCaller nc, File folder);
	String getGenealangLibraryDisplayName(GenealangLibraryLink link);
}
