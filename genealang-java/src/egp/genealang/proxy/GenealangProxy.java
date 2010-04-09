package egp.genealang.proxy;

import java.io.File;
import java.io.IOException;

import egp.genealang.infra.NamedCaller;
import egp.sphere.loader.SphereLoader.SyntaxException;
import egp.sphere.model.Sphere.DuplicateLabelFoundException;
import egp.sphere.model.Sphere.LabelNotFoundException;

public interface GenealangProxy {
	public static class BadLibraryStructureException extends Exception{
		private static final long serialVersionUID = -1026080614301737091L;

		public BadLibraryStructureException() {
			super();
		}

		public BadLibraryStructureException(String message, Throwable cause) {
			super(message, cause);
		}

		public BadLibraryStructureException(String message) {
			super(message);
		}

		public BadLibraryStructureException(Throwable cause) {
			super(cause);
		}
	}
	GenealangLibraryLink getGenealangLibraryLink(NamedCaller nc, File folder) throws IOException, SyntaxException, BadLibraryStructureException;
	String getGenealangLibraryDisplayName(GenealangLibraryLink link);
}
