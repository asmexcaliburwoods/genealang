package egp.sphere.model;

import java.util.List;

public interface Sphere {
	List<Sphere> getChildItems();
	Sphere getUniqueSphereLabeled(String string) throws LabelNotFoundException, DuplicateLabelFoundException;
	List<Sphere> getRecursivelyAllSpheresLabeled(String label);
	String concatenateAllContentsNoLabel_CharactersLengthMax(int charsLengthMax);
}
