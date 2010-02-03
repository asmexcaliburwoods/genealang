package egp.genealang.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
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
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import sun.swing.DefaultLookup;
import egp.genealang.i18n.I18n;
import egp.genealang.infra.NamedVersionedCaller;
import egp.genealang.proxy.GenealangLibraryLink;
import egp.genealang.proxy.GenealangProxy;
import egp.genealang.proxy.impl.GenealangProxyImpl;
import egp.genealang.util.ExceptionUtil;
import egp.genealang.util.MsgBoxUtil;
import egp.genealang.util.ScreenBoundsUtil;

public class SwingConsole extends JFrame implements NamedVersionedCaller{
	private final class LibraryRootTreeNode implements TreeNode {
		@Override
		public boolean isLeaf() {
			return false;
		}

		@Override
		public TreeNode getParent() {
			return null;
		}

		@Override
		public int getIndex(TreeNode node) {
			return 0;
		}

		String getTreeDisplayName(){
			return currentlySelectedLibraryLink==null?"":currentlySelectedLibraryLink.getDisplayNameLong(proxy);
		}
		@Override
		public int getChildCount() {
			return currentlySelectedLibraryLink==null?0:currentlySelectedLibraryLink.getChildCount();
		}

		@Override
		public TreeNode getChildAt(int childIndex) {
			return currentlySelectedLibraryLink==null?null:currentlySelectedLibraryLink.getChildAt(childIndex);
		}

		@Override
		public boolean getAllowsChildren() {
			return true;
		}

		@Override
		public Enumeration<TreeNode> children() {
			return currentlySelectedLibraryLink==null?null:currentlySelectedLibraryLink.getChildren();
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
	private static ResourceBundle i18n=I18n.getBundle(SwingConsole.class.getName());
	private JPanel mainComponent=new JPanel();
	private JSplitPane mainSplitter=new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	private JPanel mainToolbar=new JPanel();
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
	private JPanel mainSplitter_LeftPane=new JPanel();
	private JScrollPane mainSplitter_LeftScroller=new JScrollPane(mainSplitter_LeftPane);
	private JPanel foldersPane=new JPanel();

	private final GenealangProxy proxy=new GenealangProxyImpl();
	private final List<GenealangLibraryLink> genealangFolderLinks=new ArrayList<GenealangLibraryLink>();
	private DefaultListModel foldersListModel=new DefaultListModel();	
	private JList foldersList=new JList(foldersListModel);
	
	private GenealangLibraryLink currentlySelectedLibraryLink;
	private TreeNode libraryTreeRootNode=new LibraryRootTreeNode();
	private DefaultTreeModel libraryTreeModel=new DefaultTreeModel(libraryTreeRootNode);
	private JPanel libraryPane=new JPanel();
	private JTree libraryContentsTree=new JTree(libraryTreeModel){
	    /**
	     * Called by the renderers to convert the specified value to
	     * text. This implementation returns <code>value.toString</code>, ignoring
	     * all other arguments. To control the conversion, subclass this 
	     * method and use any of the arguments you need.
	     * 
	     * @param value the <code>Object</code> to convert to text
	     * @param selected true if the node is selected
	     * @param expanded true if the node is expanded
	     * @param leaf  true if the node is a leaf node
	     * @param row  an integer specifying the node's display row, where 0 is 
	     *             the first row in the display
	     * @param hasFocus true if the node has the focus
	     * @return the <code>String</code> representation of the node's value
	     */
	    public String convertValueToText(Object value, boolean selected,
	                                     boolean expanded, boolean leaf, int row,
	                                     boolean hasFocus) {
	        if(value != null) {
	        	String sValue = null;
	        	if (value instanceof LibraryRootTreeNode){
	        		LibraryRootTreeNode tn=(LibraryRootTreeNode) value;
	        		sValue=tn.getTreeDisplayName();
	        	}else
	        		sValue = value.toString();
	            if (sValue != null) {
	                return sValue;
	            }
	        }
	        return "";
	    }
	};
	private JScrollPane libraryContentsScroller=new JScrollPane(libraryContentsTree);
	
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
		mainComponent.setLayout(new BorderLayout());
		mainComponent.add(mainSplitter, BorderLayout.CENTER);
		mainSplitter.setDividerLocation(400);
		mainComponent.add(mainToolbar, BorderLayout.NORTH);
		mainToolbar.setLayout(new FlowLayout(FlowLayout.LEFT));
		mainToolbar.add(loadFolderButton);
		mainSplitter.setLeftComponent(mainSplitter_LeftScroller);
		mainSplitter_LeftPane.setLayout(new GridLayout(2, 1));
		mainSplitter_LeftPane.add(foldersPane);
		foldersPane.setLayout(new BorderLayout());
		foldersPane.add(new JLabel(i18n.getString("genealang.libraries")), BorderLayout.NORTH);
		foldersPane.add(foldersList, BorderLayout.CENTER);
		mainSplitter_LeftPane.add(libraryPane);
		libraryPane.setLayout(new BorderLayout());
		libraryPane.add(new JLabel(i18n.getString("library.pane.label")),BorderLayout.NORTH);
		libraryPane.add(libraryContentsScroller,BorderLayout.CENTER);
		
		foldersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		foldersList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				try{
//					if(e.getFirstIndex() != e.getLastIndex()){
//						System.out.println("e.getFirstIndex() != e.getLastIndex(): "+e.getFirstIndex()+" != "+e.getLastIndex()+"\n"+e);
//						return;
//					}
////						throw new AssertionError();
					int index=foldersList.getSelectedIndex();
					GenealangLibraryLink link=genealangFolderLinks.get(index);
					currentlySelectedLibraryLink=link;
					libraryTreeModel.nodeStructureChanged(libraryTreeRootNode);
				}catch(Throwable tr){
					ExceptionUtil.handleException(nvc, tr);
				}
			}
		});
		
//		libraryContentsTree.setCellRenderer(new DefaultTreeCellRenderer(){
//			private static final long serialVersionUID = -7048559298173704678L;
//			
//		});

		foldersList.setCellRenderer(new DefaultListCellRenderer(){
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
		            
		    	if (value instanceof GenealangLibraryLink) {
		    	    setIcon(null);
		    	    GenealangLibraryLink link=(GenealangLibraryLink) value;
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

		});
	}
	public SwingConsole() throws HeadlessException {
		super();
		setTitle(new MessageFormat(i18n.getString("frame.title.format")).format(
				new String[]{i18n.getString("genealang.swingconsole.appname"),getVersionString()}));
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(getMainComponent(), BorderLayout.CENTER);
	}
	protected void loadGenealogyFolder() {
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
		//GTD show progress and allow to pause and cancel loading of genealang folders
		GenealangLibraryLink link=proxy.getGenealangLibraryLink(this,dir);
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
	
	private String getGenealangLibraryDisplayName(GenealangLibraryLink link){
		return proxy.getGenealangLibraryDisplayName(link);
	}
    private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
    private static final Border DEFAULT_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
}
