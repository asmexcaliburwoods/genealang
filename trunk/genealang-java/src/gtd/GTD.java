package gtd;

public class GTD {
	public static void gtd(){
		RuntimeException e=new RuntimeException("GTD");
		e.printStackTrace();
		throw e;
	}
	public static void gtd(String t){
		RuntimeException e=new RuntimeException("GTD: "+t);
		e.printStackTrace();
		throw e;
	}
}
