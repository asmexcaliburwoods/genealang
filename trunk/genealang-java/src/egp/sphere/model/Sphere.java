package egp.sphere.model;

public interface Sphere {
	public class NoUnique_Exception extends Exception{public NoUnique_Exception() {}}
	public class NothingAndEverything_Exception extends Exception{public NothingAndEverything_Exception() {}}
	Sphere getUniqueSphereLabeled(String string)throws NoUnique_Exception, NothingAndEverything_Exception;
	long INFINITY_LENGTH_CHARACTERS_LIMIT=-1L;
	//GTD replace by more sane API call 
	String concatenateAllContentsNoLabel(long lengthCharactersLimit);
}
