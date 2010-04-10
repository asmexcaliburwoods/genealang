package egp.sphere.impl;

import java.util.LinkedList;
import java.util.List;

import egp.sphere.model.LabelNotFoundException;
import egp.sphere.model.Sphere;
import egp.sphere.model.WordSphere;

public class WordSphereImpl implements WordSphere {
	private final String fullQuote;

	public WordSphereImpl(String fullQuote) {
		if(fullQuote==null)throw new AssertionError("fullQuote==null");
		fullQuote=fullQuote.trim();
		if(fullQuote.length()==0)throw new AssertionError("fullQuote.length()==0");
		this.fullQuote = fullQuote;
	}

	@Override
	public Sphere getUniqueSphereLabeled(String string) throws LabelNotFoundException{
		throw new LabelNotFoundException();
	}

	@Override
	public String getFullQuote() {
		return fullQuote;
	}

	@Override
	public String concatenateAllContentsNoLabel_CharactersLengthMax(
			int charsLengthMax) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Sphere> getRecursivelyAllSpheresLabeled(String label) {
		return new LinkedList<Sphere>();
	}
	@Override
	public List<Sphere> getChildItems() {
		throw new UnsupportedOperationException();
	}
}
