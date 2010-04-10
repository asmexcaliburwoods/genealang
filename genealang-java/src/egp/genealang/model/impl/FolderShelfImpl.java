/**
 * 
 */
package egp.genealang.model.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import egp.genealang.infra.NamedVersionedCaller;
import egp.genealang.model.GenealangBook;
import egp.genealang.model.GenealangShelf;
import egp.genealang.util.ExceptionUtil;
import egp.sphere.loader.SphereLoader.SyntaxException;
import egp.sphere.model.DuplicateLabelFoundException;
import egp.sphere.model.LabelNotFoundException;
import egp.sphere.model.Sphere;

final class FolderShelfImpl extends GenealangShelfImpl {
	private final GenealangShelfImplLoader loader;
	private final Sphere librarySphere;
	private final File folder;
	final Sphere librarySpherePrivate;
	private GenealangShelf supershelf;
	private NamedVersionedCaller nvc;

	FolderShelfImpl(
			NamedVersionedCaller nvc, 
			GenealangShelfImplLoader genealangShelfImplLoader, 
			Sphere librarySphere, File folder, GenealangShelf supershelf) {
		this.nvc=nvc;
		loader = genealangShelfImplLoader;
		this.librarySphere = librarySphere;
		this.folder = folder;
		librarySpherePrivate = librarySphere;
		this.supershelf=supershelf;
	}

	@Override
	public String getDisplayName() throws LabelNotFoundException, DuplicateLabelFoundException{
		String libName=folder.getName();
		if(librarySphere!=null){
			libName=
				librarySphere.getUniqueSphereLabeled("shelf.display.name")
				.concatenateAllContentsNoLabel_CharactersLengthMax(256);
		}
		return libName;
	}

	@Override
	public List<GenealangBook> getBooks() {
		File[] books=folder.listFiles(new FileFilter(){
			@Override
			public boolean accept(File pathname) {
				return pathname.isFile()&&pathname.getName().toLowerCase().endsWith(".genealangbook");
			}
			
		});
		List<GenealangBook> booksl=new ArrayList<GenealangBook>(books.length);
		for(File book:books){
			try {
				GenealangBookImplLoader loader = new GenealangBookImplLoader();
				GenealangBook bookSph = loader.load(nvc, book, this);
				booksl.add(bookSph);
			} catch (Exception e) {
				ExceptionUtil.handleException(nvc, e);
			}
		}
		return booksl;
	}

	@Override
	public List<GenealangShelf> getSubshelves() throws Exception{
		File[] subs=folder.listFiles(new FileFilter(){
			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
			
		});
		List<GenealangShelf> subsl=new ArrayList<GenealangShelf>(subs.length);
		for(File subshelf:subs){
			GenealangShelfImplLoader loader=new GenealangShelfImplLoader();
			GenealangShelf libSph=loader.load(nvc, subshelf);
			subsl.add(libSph);
		}
		return subsl;
	}

	@Override
	public GenealangShelf getSupershelf() {
		return supershelf;
	}

	@Override
	public String getShelfLocationDescriptiveNameForErrorReporting() {
		return "folder absolute path: "+folder.getAbsolutePath();
	}
}