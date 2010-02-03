package egp.genealang.proxy.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import egp.genealang.infra.NamedCaller;
import egp.genealang.model.GenealangLibrary;
import egp.genealang.model.impl.GenealangLibraryImplFolderLoader;
import egp.genealang.proxy.GenealangLibraryLink;
import egp.genealang.proxy.GenealangProxy;

public class GenealangProxyImpl implements GenealangProxy {
	private GenealangLibraryImplFolderLoader loader=new GenealangLibraryImplFolderLoader();
	private static class FolderProxyRecord{
		private FolderProxyRecord(GenealangLibrary folder,GenealangLibraryLink folderLink,File file){
			if(folder==null)throw new NullPointerException("fldr");
			if(folderLink==null)throw new NullPointerException("flink");
			if(file==null)throw new NullPointerException("file");
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
			File folder) {
		FolderProxyRecord r=folders.get(folder);
		if(r==null){
			GenealangLibrary gf=loader.load(nc, folder);
			r=new FolderProxyRecord(gf, new GenealangLibraryLinkImpl(gf.getLibraryDisplayNameLong(this)),folder);
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
