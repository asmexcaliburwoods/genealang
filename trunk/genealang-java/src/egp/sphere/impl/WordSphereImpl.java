package egp.sphere.impl;

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
	public String concatenateAllContentsNoLabel(long lengthCharactersLimit) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Sphere getUniqueSphereLabeled(String string)
			throws NoUnique_Exception, NothingAndEverything_Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getFullQuote() {
		return fullQuote;
	}
}
