package egp.genealang.model;

import egp.genealang.proxy.GenealangProxy;
import egp.sphere.model.Sphere;

public interface GenealangLibrary extends Sphere{
	String getLibraryDisplayNameLong(GenealangProxy proxy);
}
