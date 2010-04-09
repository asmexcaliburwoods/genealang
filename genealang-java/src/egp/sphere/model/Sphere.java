package egp.sphere.model;

import java.util.List;

public interface Sphere {
	public static class LabelNotFoundException extends Exception{}
	public static class DuplicateLabelFoundException extends Exception{}
	List<Sphere> getChildItems();
	Sphere getUniqueSphereLabeled(String string) throws LabelNotFoundException, DuplicateLabelFoundException;
	List<Sphere> getRecursivelyAllSpheresLabeled(String label);
	String concatenateAllContentsNoLabel_CharactersLengthMax(int charsLengthMax);
}
