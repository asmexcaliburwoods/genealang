package egp.genealang.util;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import egp.genealang.infra.NamedCaller;
import egp.genealang.tostring.ToString;
import gtd.GTD;

public class MsgBoxUtil {
	/** tr may be null */
	private static JDialog show(
			NamedCaller nc,
			JFrame parent,String error,String title,Throwable tr){
		final JDialog dialog=new JDialog(parent,title,true);
		addPane(nc, dialog, dialog.getContentPane(), dialog.getRootPane(), error, tr);
		//    ThreadUtil.spawnThread("msgbox dialog",new Runnable(){
		//      public void run(){
		//        try{
		dialog.setVisible(true);
		//        } catch(Exception e){
		//          ExceptionUtil.handleException(e);
		//        }
		//      }
		//    });
		return dialog;
	}

	private static void addPane(NamedCaller nc, final Window dialog,
			Container contentPane, JRootPane rootPane, String error1, final Throwable tr) {
		String error2=tr!=null?ExceptionUtil.getExceptionMessage(error1,tr):error1;
		if(error2.startsWith(":"))error2=error2.substring(1);
//		if(StringUtil.isEmptyTrimmed(error2))error2=taygalove_shepherd.i18n.m.M.UNKNOWN_ERROR;
		if(StringUtil.isEmptyTrimmed(error2))error2="Unknown error.";
		error2=error2.trim();
		error2=Character.toUpperCase(error2.charAt(0))+error2.substring(1);
		final String error=error2;
		JTextArea textPane = createTextComponent(dialog, error);
		contentPane.setLayout(new BorderLayout());
		final Box texts=new Box(BoxLayout.Y_AXIS);
		texts.add(textPane);
		final JScrollPane scroll = new JScrollPane(texts);
		contentPane.add(scroll,BorderLayout.CENTER);
		final JButton okButton=new JButton(new AbstractAction("OK"){
			private static final long serialVersionUID = 6940826361872819015L;

			public void actionPerformed(ActionEvent e){
				//        dialog.dispose();
			}
		});
		final JPanel okButtonPanel=new JPanel(null){
			private static final long serialVersionUID = 3522372737014949739L;

			public Dimension getPreferredSize() {
				return okButton.getPreferredSize();
			}
		};
		okButtonPanel.add(okButton);
		final JPanel buttons=new JPanel(new BorderLayout());
		buttons.add(okButtonPanel,BorderLayout.CENTER);
		JPanel left=new JPanel(new FlowLayout(FlowLayout.LEFT));
		final boolean[] traceShown=new boolean[]{false};
		final String[] report=new String[]{null};
		final JTextArea[] reportPane=new JTextArea[]{null};
		final JButton traceButton=new JButton(">>");
		if(tr!=null){
			report[0]=getReport(nc, error,tr);
			if(report[0]==null)report[0]="";
			reportPane[0]=createTextComponent(dialog, report[0]);
			reportPane[0].setCaretPosition(0);
		}
		traceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				traceShown[0]=!traceShown[0];
				traceButton.setText(traceShown[0]?"<<":">>");
				showTrace(traceShown[0]);
			}

			private void showTrace(boolean show) {
				if(show){
					texts.add(reportPane[0]);
					dialog.validate();
				}else{
					texts.remove(1);
				}
				reportPane[0].setCaretPosition(0);
			}
		});
		if(tr!=null)left.add(traceButton);
		buttons.add(left,BorderLayout.WEST);
		contentPane.add(buttons,BorderLayout.SOUTH);
		rootPane.setDefaultButton(okButton);
		ComponentAdapter resize = new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				okButton.setSize(okButton.getPreferredSize());
				okButton.setLocation(
						(buttons.getWidth() - okButton.getWidth()) / 2 - (okButtonPanel.getX() - 0*buttons.getX()),
						(buttons.getHeight() - okButton.getHeight()) / 2 - (okButtonPanel.getY() - 0*buttons.getY()));
			}
		};
		okButtonPanel.addComponentListener(resize);
		buttons.addComponentListener(resize);
		initMessageBox(dialog);
		KeyAdapter kl=new KeyAdapter(){
			public void keyReleased(KeyEvent e){
				switch(e.getKeyCode()){
				case KeyEvent.VK_ENTER:
				case KeyEvent.VK_SPACE:
				case KeyEvent.VK_ESCAPE:
					dialog.dispose();
					e.consume();
					break;
				}
			}
		};
		okButton.addKeyListener(kl);
		textPane.addKeyListener(kl);
		okButton.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent e) {
				dialog.dispose();
			}
		});
	}

	private static JTextArea createTextComponent(final Window root,String error) {
		JTextArea textPane=new JTextArea(){
			private static final long serialVersionUID = 1092313226340737836L;

			public Dimension getPreferredSize() {
				Dimension superDim=super.getPreferredSize();
				Component scrollPaneC =this;
				while(scrollPaneC !=null&&!(scrollPaneC instanceof JScrollPane))scrollPaneC = scrollPaneC.getParent();
				JScrollPane scroll =(JScrollPane) scrollPaneC;
				superDim.width = root.getWidth() - root.getInsets().left - root.getInsets().right -
				(scroll == null ? 0 :
					scroll.getVerticalScrollBar().getWidth() +
					scroll.getInsets().left + scroll.getInsets().right);
				return superDim;
			}
		};
		textPane.setEditable(false);
		textPane.setText(error);
		textPane.setLineWrap(true);
		textPane.setWrapStyleWord(true);
		textPane.setFont(root.getFont());//otherwise it sets fixed width font.
		textPane.setCaretPosition(0);
		return textPane;
	}

	private static final String TITLE_INFO="Information";
	private static final String TITLE_ERROR="Error";
	public static JDialog showMessage(NamedCaller nc, JFrame parent,String message){
		return show(nc, parent,message,TITLE_INFO,null);
	}
	public static JFrame showMessage(NamedCaller caller, String msg){
		return show(caller, msg,TITLE_INFO,null);
	}
	private static JDialog show(
			NamedCaller nc, 
			JDialog parent,String error,String title){
		final JDialog dialog=new JDialog(parent,title,true);
		JTextArea textPane=createTextComponent(parent, error);
		dialog.getContentPane().setLayout(new BorderLayout());
		dialog.getContentPane().add(new JScrollPane(textPane),BorderLayout.CENTER);
		JButton okButton=new JButton(new AbstractAction("OK"){
			private static final long serialVersionUID = 5950949805862562063L;

			public void actionPerformed(ActionEvent e){
				//        dialog.dispose();
			}
		});
		JPanel p=new JPanel(new FlowLayout(FlowLayout.CENTER));
		p.add(okButton);
		dialog.getRootPane().setDefaultButton(okButton);
		dialog.getContentPane().add(p,BorderLayout.SOUTH);
		initMessageBox(dialog);
		KeyAdapter kl=new KeyAdapter(){
			public void keyReleased(KeyEvent e){
				switch(e.getKeyCode()){
				case KeyEvent.VK_ENTER:
				case KeyEvent.VK_SPACE:
				case KeyEvent.VK_ESCAPE:
					dialog.dispose();
					e.consume();
					break;
				}
			}
		};
		okButton.addKeyListener(kl);
		okButton.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent e) {
				dialog.dispose();
			}
		});
		textPane.addKeyListener(kl);
		//    ThreadUtil.spawnThread("msgbox dialog",new Runnable(){
		//      public void run(){
		//        try{
		dialog.setVisible(true);
		//        } catch(Exception e){
		//          ExceptionUtil.handleException(e);
		//        }
		//      }
		//    });
		return dialog;
	}

	/** tr may be null */
	private static JFrame show(NamedCaller caller, String error,String title,Throwable tr){
		final JFrame errDialog=new JFrame(title);
		addPane(caller, errDialog,errDialog.getContentPane(),errDialog.getRootPane(),error,tr);
		errDialog.setVisible(true);
		return errDialog;
	}

	//  public static void showError(JFrame parent,String precedingErrorMessage,Throwable tr){
	//    showError(parent,precedingErrorMessage,tr);
	//  }

	private static String getReport(NamedCaller caller, String precedingErrorMessage,Throwable tr){
		ByteArrayOutputStream bos=new ByteArrayOutputStream(4096);
		PrintWriter w=new PrintWriter(bos,false);
		w.println("\r\n\r\n***\r\n\r\n"+ToString.callerToNameAndVersionOfCaller(caller)+"\r\n");
		//    if(precedingErrorMessage!=null)w.println(precedingErrorMessage+":");
		if(tr==null){
			w.println("null throwable");
			final Exception exception=new Exception("dumpstack");
			exception.printStackTrace();
		}else{
			tr.printStackTrace(w);
		}
		w.flush();
		return new String(bos.toByteArray());
	}
	//  public static void showError(String precedingErrorMessage,Throwable tr){
	//    showError(exceptionToString(precedingErrorMessage,tr));
	//  }
	public static void showError(NamedCaller nc, JFrame parent,String error){
		showError(nc, parent,error,null);
	}
	public static void showError(NamedCaller nc, JFrame parent,String error,Throwable tr){
		show(nc, parent,error,TITLE_ERROR,tr);
	}
	public static void showError(NamedCaller nc, Component c,String error){
		showError(nc, c,error,null);
	}
	private static void showError(NamedCaller nc, Component c,String error,Throwable tr){
		Component parent=c;
		while(parent.getParent()!=null){
			parent=parent.getParent();
		}
		if(parent instanceof JFrame)
			showError(nc, (JFrame)parent,error,tr);
		else if(parent instanceof JDialog)
			showError(nc, (JDialog)parent,error,tr);
		else GTD.gtd("Implement better handling for MsgBoxUtil.showError for parent "+parent.getClass().getName());
	}
	public static void showError(NamedCaller nc, JDialog parent,String error){
		show(nc, parent,error,TITLE_ERROR);
	}
	//considered harmful
	//  public static void showError(JDialog parent,String error,Throwable tr){
	//    showError(parent,exceptionToString(error,tr));
	//  }
	public static void showError(NamedCaller nc, String error){
		showError(nc, error,null);
	}
	public static void showError(NamedCaller nc, String error,Throwable tr){
		show(nc, error,TITLE_ERROR,tr);
	}
	public static void initMessageBox(final Component dialog){
		dialog.setSize(400,200);
		WindowBoundsUtil.centerWindow(dialog);
	}
	public static void noimpl(){
		GTD.gtd();
	}
	public static int askYesNo(NamedCaller nc, JFrame parent,String title,String message,String[] options,int defaultButton){
		final JDialog dialog=new JDialog(parent,title,true);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		JTextArea textPane=createTextComponent(parent, message);
		textPane.addKeyListener(new KeyAdapter(){
			public void keyReleased(KeyEvent e){
				if((e.getKeyCode()==KeyEvent.VK_ENTER)||(e.getKeyCode()==KeyEvent.VK_SPACE)){
					Action action = dialog.getRootPane().getDefaultButton().getAction();
					//          System.out.println("action: \""+action.getValue(Action.NAME)+"\"");
					action.actionPerformed(null);
					dialog.dispose();
					e.consume();
				}
			}
		});
		dialog.getContentPane().setLayout(new BorderLayout());
		dialog.getContentPane().add(new JScrollPane(textPane),BorderLayout.CENTER);
		JPanel p=new JPanel(new FlowLayout(FlowLayout.CENTER));
		dialog.getContentPane().add(p,BorderLayout.SOUTH);
		MouseAdapter ml=new MouseAdapter(){
			public void mouseReleased(MouseEvent e) {
				dialog.dispose();
			}
		};
		final int[] result=new int[]{-1};
		for(int i=0;i<options.length;i++){
			final int optionResult=i;
			AbstractAction action=new AbstractAction(options[i]){
				private static final long serialVersionUID = 7814608547409326164L;

				public void actionPerformed(ActionEvent e){
					result[0]=optionResult;
				}
			};
			JButton button=new JButton(action);
			button.addMouseListener(ml);
			p.add(button);
			if(i==defaultButton){
				dialog.getRootPane().setDefaultButton(button);
			}
		}
		initMessageBox(dialog);
		KeyAdapter kl = new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if((e.getKeyCode()==KeyEvent.VK_ENTER)||(e.getKeyCode()==KeyEvent.VK_SPACE)){
					dialog.dispose();
				}
			}
		};
		KeyListenerUtil.addKeyListenerRecursively(dialog,kl);
		textPane.removeKeyListener(kl);
		dialog.setVisible(true);
		return result[0];
	}
	public static int askYesNo(JDialog parent,String title,String message,String[] options,int defaultButton){
		final JDialog dialog=new JDialog(parent,title,true);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		JTextArea textPane=createTextComponent(parent, message);
		textPane.addKeyListener(new KeyAdapter(){
			public void keyReleased(KeyEvent e){
				if((e.getKeyCode()==KeyEvent.VK_ENTER)||(e.getKeyCode()==KeyEvent.VK_SPACE)){
					dialog.getRootPane().getDefaultButton().getAction().actionPerformed(null);
					e.consume();
					dialog.dispose();
				}
			}
		});
		dialog.getContentPane().setLayout(new BorderLayout());
		dialog.getContentPane().add(new JScrollPane(textPane),BorderLayout.CENTER);
		JPanel p=new JPanel(new FlowLayout(FlowLayout.CENTER));
		dialog.getContentPane().add(p,BorderLayout.SOUTH);
		MouseAdapter ml=new MouseAdapter(){
			public void mouseReleased(MouseEvent e) {
				dialog.dispose();
			}
		};
		final int[] result=new int[]{-1};
		for(int i=0;i<options.length;i++){
			final int optionResult=i;
			AbstractAction action=new AbstractAction(options[i]){
				private static final long serialVersionUID = 5200828713485278413L;

				public void actionPerformed(ActionEvent e){
					result[0]=optionResult;
				}
			};
			JButton button=new JButton(action);
			button.addMouseListener(ml);
			p.add(button);
			if(i==defaultButton){
				dialog.getRootPane().setDefaultButton(button);
			}
		}
		initMessageBox(dialog);
		KeyAdapter kl = new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if((e.getKeyCode()==KeyEvent.VK_ENTER)||(e.getKeyCode()==KeyEvent.VK_SPACE)){
					dialog.dispose();
				}
			}
		};
		KeyListenerUtil.addKeyListenerRecursively(dialog,kl);
		textPane.removeKeyListener(kl);
		dialog.setVisible(true);
		return result[0];
	}
	/** Pass -1 as defaultButton to have no default button. */
	public static int askYesNo(NamedCaller nc, String title,String message,String[] options,int defaultButton){
		final int[] result=new int[]{-1};
		final JFrame dialog=new JFrame(title);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		JTextArea textPane=createTextComponent(dialog, message);
		textPane.addKeyListener(new KeyAdapter(){
			public void keyReleased(KeyEvent e){
				if((e.getKeyCode()==KeyEvent.VK_ENTER)||(e.getKeyCode()==KeyEvent.VK_SPACE)){
					dialog.getRootPane().getDefaultButton().getAction().actionPerformed(null);
					e.consume();
					dialog.dispose();
				}
			}
		});
		dialog.getContentPane().setLayout(new BorderLayout());
		dialog.getContentPane().add(new JScrollPane(textPane),BorderLayout.CENTER);
		JPanel p=new JPanel(new FlowLayout(FlowLayout.CENTER));
		dialog.getContentPane().add(p,BorderLayout.SOUTH);
		MouseAdapter ml=new MouseAdapter(){
			public void mouseReleased(MouseEvent e) {
				dialog.dispose();
				synchronized(result){
					result.notify();
				}
			}
		};
		for(int i=0;i<options.length;i++){
			final int optionResult=i;
			AbstractAction action=new AbstractAction(options[i]){
				private static final long serialVersionUID = 5064759988230867660L;

				public void actionPerformed(ActionEvent e){
					result[0]=optionResult;
				}
			};
			JButton button=new JButton(action);
			button.addMouseListener(ml);
			p.add(button);
			if(i==defaultButton){
				dialog.getRootPane().setDefaultButton(button);
			}
		}
		initMessageBox(dialog);
		KeyAdapter kl = new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if((e.getKeyCode()==KeyEvent.VK_ENTER)||(e.getKeyCode()==KeyEvent.VK_SPACE)){
					dialog.dispose();
					synchronized(result){
						result.notify();
					}
				}
			}
		};
		KeyListenerUtil.addKeyListenerRecursively(dialog,kl);
		textPane.removeKeyListener(kl);
		ThreadUtil.spawnThread(nc, "msg box util",new Runnable(){
			public void run(){
				dialog.setVisible(true);
			}
		});
		synchronized(result){
			while(result[0]==-1)
				try{
					result.wait();
				} catch(InterruptedException e){
					ExceptionUtil.handleException(nc, e);
					throw new RuntimeException();
				}
		}
		return result[0];
	}

	public static void main(String[] args){
		//    showError("Sample");
		showSystemProperties(new NamedCaller() {
			@Override
			public String name() {
				return MsgBoxUtil.class.getName();
			}
		});
		//    showError("Sample Prefix",new Exception("Sample ErrMsg  dkfjgjdksfhgsdj dgjksfh gksdjfh n,  fg d ffg jhfg.dkfjgjdksfhgsdj dgjksfh gksdjfh n,  fg d ffg jhfg.dkfjgjdksfhgsdj dgjksfh gksdjfh n,  fg d ffg jhfg.dkfjgjdksfhgsdj dgjksfh gksdjfh n,  fg d ffg jhfg.dkfjgjdksfhgsdj dgjksfh gksdjfh n,  fg d ffg jhfg.dkfjgjdksfhgsdj dgjksfh gksdjfh n,  fg d ffg jhfg.dkfjgjdksfhgsdj dgjksfh gksdjfh n,  fg d ffg jhfg.dkfjgjdksfhgsdj dgjksfh gksdjfh n,  fg d ffg jhfg.dkfjgjdksfhgsdj dgjksfh gksdjfh n,  fg d ffg jhfg.dkfjgjdksfhgsdj dgjksfh gksdjfh n,  fg d ffg jhfg.dkfjgjdksfhgsdj dgjksfh gksdjfh n,  fg d ffg jhfg.dkfjgjdksfhgsdj dgjksfh gksdjfh n,  fg d ffg jhfg."));
		//    MsgBoxUtil.showError(
		//            M.SAMPLE_ERROR);
	}

	public static void showSystemProperties(NamedCaller caller) {
		showProperties(caller, System.getProperties());
	}

	public static void showProperties(NamedCaller nc, Properties p) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintWriter w=new PrintWriter(new OutputStreamWriter(out));
		p.list(w);
		w.flush();
		MsgBoxUtil.showMessage(nc, new String(out.toByteArray()));
	}
}
