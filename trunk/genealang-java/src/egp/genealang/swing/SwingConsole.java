package egp.genealang.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultTreeModel;

import sun.swing.DefaultLookup;
import egp.genealang.i18n.I18n;
import egp.genealang.infra.NamedVersionedCaller;
import egp.genealang.libtreenodeimpl.BadLibraryStructureException;
import egp.genealang.libtreenodeimpl.GenealangShelfTreeNodeImpl;
import egp.genealang.model.GenealangShelf;
import egp.genealang.model.impl.GenealangShelfImplLoader;
import egp.genealang.util.ExceptionUtil;
import egp.genealang.util.MsgBoxUtil;
import egp.genealang.util.ScreenBoundsUtil;
import egp.sphere.loader.SphereLoader.SyntaxException;

public class SwingConsole extends JFrame implements NamedVersionedCaller{
	private final class RefreshButtonAbstractAction1 extends
			AbstractAction {
		private static final long serialVersionUID = 6397957628823787511L;

		private RefreshButtonAbstractAction1(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				libraryContentsTree
						.libraryContentsRefreshButtonActionPerformed();
			} catch (Throwable e2) {
				ExceptionUtil.handleException(nvc, e2);
			}
		}
	}
	private final class DefaultListCellRenderer1 extends
			DefaultListCellRenderer {
		private static final long serialVersionUID = 3286606344832032396L;

		public Component getListCellRendererComponent(
		        JList list,
			Object value,
		        int index,
		        boolean isSelected,
		        boolean cellHasFocus)
		    {
		        setComponentOrientation(list.getComponentOrientation());

		        Color bg = null;
		        Color fg = null;

		        JList.DropLocation dropLocation = list.getDropLocation();
		        if (dropLocation != null
		                && !dropLocation.isInsert()
		                && dropLocation.getIndex() == index) {

		            bg = DefaultLookup.getColor(this, ui, "List.dropCellBackground");
		            fg = DefaultLookup.getColor(this, ui, "List.dropCellForeground");

		            isSelected = true;
		        }

			if (isSelected) {
		            setBackground(bg == null ? list.getSelectionBackground() : bg);
			    setForeground(fg == null ? list.getSelectionForeground() : fg);
			}
			else {
			    setBackground(list.getBackground());
			    setForeground(list.getForeground());
			}
		        
			if (value instanceof GenealangShelfTreeNodeImpl) {
			    setIcon(null);
			    GenealangShelfTreeNodeImpl link=(GenealangShelfTreeNodeImpl) value;
			    setText(getGenealangLibraryDisplayName(link));
			}
			else
		    	if (value instanceof Icon) {
		    	    setIcon((Icon)value);
		    	    setText("");
		    	}
		    	else
			{
			    setIcon(null);
			    setText((value == null) ? "" : value.toString());
			}

			setEnabled(list.isEnabled());
			setFont(list.getFont());
		        
		        Border border = null;
		        if (cellHasFocus) {
		            if (isSelected) {
		                border = DefaultLookup.getBorder(this, ui, "List.focusSelectedCellHighlightBorder");
		            }
		            if (border == null) {
		                border = DefaultLookup.getBorder(this, ui, "List.focusCellHighlightBorder");
		            }
		        } else {
		            border = getNoFocusBorder();
		        }
			setBorder(border);

			return this;
		    }

		private Border getNoFocusBorder() {
		    Border border = DefaultLookup.getBorder(this, ui, "List.cellNoFocusBorder");
		    if (System.getSecurityManager() != null) {
		        if (border != null) return border;
		        return SAFE_NO_FOCUS_BORDER;
		    } else {
		        if (border != null &&
		                (noFocusBorder == null ||
		                noFocusBorder == DEFAULT_NO_FOCUS_BORDER)) {
		            return border;
		        }
		        return noFocusBorder;
		    }
		}
	}
	private final class ListSelectionListener1 implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			try{
//					if(e.getFirstIndex() != e.getLastIndex()){
//						System.out.println("e.getFirstIndex() != e.getLastIndex(): "+e.getFirstIndex()+" != "+e.getLastIndex()+"\n"+e);
//						return;
//					}
////						throw new AssertionError();
				int index=foldersList.getSelectedIndex();
				GenealangShelfTreeNodeImpl link=genealangFolderLinks.get(index);
				currentlySelectedLibraryLink=link;
				libraryTreeModel.nodeStructureChanged(libraryTreeRootNode);
				refreshLibraryName();
			}catch(Throwable tr){
				ExceptionUtil.handleException(nvc, tr);
			}
		}
	}
	private static final long serialVersionUID = -9073324859042990633L;
	private static NamedVersionedCaller nvc=new NamedVersionedCaller(){

		@Override
		public String versionString() {
			return getVersionString();
		}

		@Override
		public String name() {
			return getAppName();
		}
		
	};
	private static String getVersionString(){return "0";}  
	static ResourceBundle i18n=I18n.getBundle(SwingConsole.class.getName());
	private JPanel mainComponent;
	private JPanel mainToolbar;
	private JButton loadFolderButton=new JButton(new AbstractAction(i18n.getString("load.genealang.folder")){
		private static final long serialVersionUID = 5087613065301020079L;

		@Override
		public void actionPerformed(ActionEvent e) {
			try{
				loadGenealogyFolder();
			}catch(Throwable tr){
				ExceptionUtil.handleException(nvc, tr);
			}
		}
	});
	private JPanel foldersPane;

	private final List<GenealangShelfTreeNodeImpl> genealangFolderLinks=new ArrayList<GenealangShelfTreeNodeImpl>();
	private DefaultListModel foldersListModel=new DefaultListModel();	
	private JList foldersList=new JList(foldersListModel);
	
	GenealangShelfTreeNodeImpl currentlySelectedLibraryLink;
	private LibraryRootTreeNode libraryTreeRootNode=new LibraryRootTreeNode(this);
	private DefaultTreeModel libraryTreeModel=new DefaultTreeModel(libraryTreeRootNode);
	private JPanel libraryPane;
	private LibraryContentsTree libraryContentsTree=new LibraryContentsTree(this, libraryTreeModel);
	private JScrollPane libraryContentsScroller=new JScrollPane(libraryContentsTree);
	private JPanel libraryContentsToolbar;
	private JButton libraryContentsRefreshButton;
	private JPanel libraryPaneNested;
	private JLabel libraryNameLabel;
	private JSplitPane splitter1;
	private JPanel foldersNestedPane;
	
	{
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if(0==MsgBoxUtil.askYesNo(nvc, SwingConsole.this, 
						i18n.getString("exitApplicationTitle"), 
						i18n.getString("exitApplicationMessage"), 
						new String[]{
					i18n.getString("OK"),
					i18n.getString("Cancel"),
						}, 1))System.exit(0);
				//GTD ask to save results/keep committing every change
			}
		});
		setExtendedState(MAXIMIZED_BOTH);
		setBounds(ScreenBoundsUtil.getScreenBounds());
		mainComponent=new JPanel(new BorderLayout());
		mainToolbar=new JPanel();
		foldersPane=new JPanel(new BorderLayout());
		libraryPane=new JPanel(new BorderLayout());
		splitter1=new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, foldersPane, libraryPane);
		splitter1.setDividerLocation(80);
		mainComponent.add(splitter1,BorderLayout.CENTER);
		
		mainToolbar.setLayout(new FlowLayout(FlowLayout.LEFT));
		mainToolbar.add(loadFolderButton);
		
		foldersPane.add(new JLabel(i18n.getString("genealang.libraries")), BorderLayout.NORTH);
		foldersNestedPane=new JPanel(new BorderLayout());
		foldersPane.add(foldersNestedPane, BorderLayout.CENTER);
		foldersNestedPane.add(mainToolbar, BorderLayout.NORTH);
		foldersNestedPane.add(foldersList, BorderLayout.CENTER);
		
		libraryNameLabel = new JLabel();
		libraryPane.add(libraryNameLabel,BorderLayout.NORTH);
		libraryPaneNested=new JPanel(new BorderLayout());
		libraryPane.add(libraryPaneNested,BorderLayout.CENTER);
		libraryContentsToolbar=new JPanel(new FlowLayout(FlowLayout.LEFT));
		libraryPaneNested.add(libraryContentsToolbar,BorderLayout.NORTH);
		libraryPaneNested.add(libraryContentsScroller,BorderLayout.CENTER);
		libraryContentsRefreshButton=new JButton(new RefreshButtonAbstractAction1(i18n.getString("libraryContentsRefreshButton.name")));
		refreshLibraryName();
		libraryContentsToolbar.add(libraryContentsRefreshButton);
		
		foldersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		foldersList.addListSelectionListener(new ListSelectionListener1());
		
//		libraryContentsTree.setCellRenderer(new DefaultTreeCellRenderer(){
//			private static final long serialVersionUID = -7048559298173704678L;
//			
//		});

		foldersList.setCellRenderer(new DefaultListCellRenderer1());
		
//		libraryContentsTree.setShowsRootHandles(false);
	}
	void refreshLibraryName() {
		libraryNameLabel.setText(new MessageFormat(
				i18n.getString("library.name.label.format")
			).format(new Object[]{libraryTreeRootNode.getLibraryDisplayName()}));
	}
	public SwingConsole() throws HeadlessException {
		super();
		setTitle(new MessageFormat(i18n.getString("frame.title.format")).format(
				new String[]{i18n.getString("genealang.swingconsole.appname"),getVersionString()}));
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(getMainComponent(), BorderLayout.CENTER);
	}
	protected void loadGenealogyFolder() throws Exception {
		FileDialog fd=new FileDialog(this,i18n.getString("filedialog.load.genealang.folder.title"),FileDialog.LOAD);
		fd.setModal(true);
		fd.setBounds(ScreenBoundsUtil.getScreenBounds());
		fd.setFilenameFilter(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return new File(dir,name).isDirectory();
			}
		});
		fd.setVisible(true);
		String s=fd.getDirectory();
		if(s==null){
			return;
		}
		File dir=new File(s);
		//GTD show progress and allow to pause and cancel loading of genealangbook shelves
		GenealangShelfImplLoader loader=new GenealangShelfImplLoader();
		GenealangShelf gf=loader.load(nvc, dir);
		GenealangShelfTreeNodeImpl link=new GenealangShelfTreeNodeImpl(nvc, gf, libraryTreeRootNode);
		genealangFolderLinks.add(link);
		foldersListModel.addElement(link);
		foldersList.setSelectedIndex(genealangFolderLinks.size()-1);
	}
	public JComponent getMainComponent(){
		return mainComponent;
	}
	public static void main(String[] args) {
		try{
			SwingConsole sc=new SwingConsole();
			sc.setVisible(true);
			sc.loadGenealogyFolder();
		}catch(Throwable tr){
			ExceptionUtil.handleException(nvc, tr);
		}
	}

	@Override
	public String name() {
		return nvc.name();
	}
	
	private static String getAppName(){
		return SwingConsole.class.getSimpleName();
	}

	@Override
	public String versionString() {
		return nvc.versionString();
	}
	
	private String getGenealangLibraryDisplayName(GenealangShelfTreeNodeImpl link){
		return link.getDisplayName();
	}
    private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
    private static final Border DEFAULT_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
}
