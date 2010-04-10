package egp.sphere.impl;

import java.util.ArrayList;
import java.util.List;

import egp.sphere.model.LabelNotFoundException;
import egp.sphere.model.NullSphere;
import egp.sphere.model.Sphere;

public class NullSphereImpl implements NullSphere {
	@Override
	public Sphere getUniqueSphereLabeled(String string) throws LabelNotFoundException{
		throw new LabelNotFoundException();
	}

	@Override
	public String concatenateAllContentsNoLabel_CharactersLengthMax(
			int charsLengthMax) {
		return "";
	}

	@Override
	public List<Sphere> getRecursivelyAllSpheresLabeled(String label) {
		return new ArrayList<Sphere>();
	}

	@Override
	public List<Sphere> getChildItems() {
		throw new UnsupportedOperationException();
	}
}
