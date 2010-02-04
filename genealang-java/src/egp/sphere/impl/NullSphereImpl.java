package egp.sphere.impl;

import egp.sphere.model.NullSphere;
import egp.sphere.model.Sphere;
import egp.sphere.model.Sphere.NothingAndEverything_Exception;

public class NullSphereImpl implements NullSphere {
	@Override
	public String concatenateAllContentsNoLabel(long lengthCharactersLimit) {
		return "";
	}

	@Override
	public Sphere getUniqueSphereLabeled(String string) throws NothingAndEverything_Exception{
		throw new NothingAndEverything_Exception();
	}
}
