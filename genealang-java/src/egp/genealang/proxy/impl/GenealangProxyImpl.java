package egp.genealang.proxy.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import egp.genealang.infra.NamedCaller;
import egp.genealang.model.GenealangLibrary;
import egp.genealang.model.impl.GenealangLibraryImplFolderLoader;
import egp.genealang.proxy.GenealangLibraryLink;
import egp.genealang.proxy.GenealangProxy;
import egp.genealang.proxy.GenealangProxy.BadLibraryStructureException;
import egp.genealang.util.ExceptionUtil;
import egp.sphere.loader.SphereLoader.SyntaxException;
import egp.sphere.model.Sphere.DuplicateLabelFoundException;
import egp.sphere.model.Sphere.LabelNotFoundException;

public class GenealangProxyImpl implements GenealangProxy {
	private GenealangLibraryImplFolderLoader loader=new GenealangLibraryImplFolderLoader();
	private static class FolderProxyRecord{
		private FolderProxyRecord(GenealangLibrary folder,GenealangLibraryLink folderLink,File file){
			if(folder==null)throw new AssertionError("folder is null");
			if(folderLink==null)throw new AssertionError("folderLink is null");
			if(file==null)throw new AssertionError("file is null");
			this.folder=folder;
			this.folderLink=folderLink;
			this.file=file;
		}
		final GenealangLibrary folder;
		final GenealangLibraryLink folderLink;
		final File file; 
	}
	private Map<File, FolderProxyRecord> folders=new HashMap<File, FolderProxyRecord>();
	private Map<GenealangLibraryLink, FolderProxyRecord> folderLink2record=new HashMap<GenealangLibraryLink, FolderProxyRecord>();
	
	@Override
	public GenealangLibraryLink getGenealangLibraryLink(NamedCaller nc,
			File folder) throws IOException, SyntaxException, BadLibraryStructureException{
		FolderProxyRecord r=folders.get(folder);
		if(r==null){
			GenealangLibrary gf=loader.load(nc, folder);
			String libraryDisplayNameLong = null;
			try {
				libraryDisplayNameLong=gf.getLibraryDisplayNameLong(this);
			} catch (LabelNotFoundException e) {
				throw new BadLibraryStructureException("Bad library structure at "+folder.getAbsolutePath()+". "+e,e);
			} catch (DuplicateLabelFoundException e) {
				throw new BadLibraryStructureException("Bad library structure at "+folder.getAbsolutePath()+". "+e,e);
			}
			r=new FolderProxyRecord(gf, new GenealangLibraryLinkImpl(libraryDisplayNameLong, gf),folder);
			folders.put(folder, r);
			folderLink2record.put(r.folderLink, r);
		}
		return r.folderLink;
	}

	@Override
	public String getGenealangLibraryDisplayName(GenealangLibraryLink link) {
		return link.getDisplayNameLong(this);
	}
}
