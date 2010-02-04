package egp.sphere.loader;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import egp.sphere.impl.NullSphereImpl;
import egp.sphere.impl.SphereImpl;
import egp.sphere.impl.WordSphereImpl;
import egp.sphere.model.Sphere;

public class SphereLoader {
	private static final int EOF_INT=-1;  
	public static SphereLoader lowlevel_fromDotSphereFile(File file)throws IOException{
		FileInputStream fis=new FileInputStream(file);
		BufferedInputStream bis=new BufferedInputStream(fis,64*1024);
		Reader r=new InputStreamReader(bis);
		return lowlevel_fromDotSphereFile(r,file.getAbsolutePath());
	}
	public Sphere get(){
		return sphere;
	}
	private Sphere sphere;
	private void gettok() throws IOException{
		int ch;
		token[0]=ch=br.read();
		switch(ch){
		case '\r':
			break;
		case '\n':
			break;
		}
	}
	private void ws() throws IOException{
		while(true){
			switch(token[0]){
			case ' ':
			case '\t':
			case '\r':
			case '\n':
				gettok();
				continue;
			}
			break;
		}
	}
	private void wsAndComments() throws IOException{
		while(true){
			ws();
//			if(token[0]==';'){
//				gettok();//skip character `;'
//				br.readLine();
//				gettok();//take first character in the next line
//				continue;
//			}
			break;
		}
	}
	private void expect(int chExpected, String chDisplayName) throws IOException, SyntaxException{
		if(token!=chExpected)
			throw new SyntaxException("Character \""+chDisplayName+"\" (`"+chExpected+"') expected, but found: `"+token+"'.");
		if(chExpected!=EOF_INT)if(token!=EOF_INT)gettok();
	}
	public class SyntaxException extends Exception{
		private String nameOfFrom;
		private BigInteger line;
		private BigInteger column;
		private void init() {
			this.line = getCurrentLine();
			this.column = getCurrentColumn();
			this.nameOfFrom = getNameOfFrom();
		}
		
		public BigInteger getLine() {
			return line;
		}

		public BigInteger getColumn() {
			return column;
		}

		public SyntaxException() {
			super();
			init();
		}
		public SyntaxException(String message, Throwable cause) {
			super(message, cause);
			init();
		}

		public SyntaxException(String message) {
			super(message);
			init();
		}

		public SyntaxException(Throwable cause) {
			super(cause);
			init();
		}
		
	    public String getMessage() {
	        return super.getMessage()+
	        	" From: \""+nameOfSomethingThatWeAreReadingFrom+
	        	"\", line: "+line+
	        	", column: "+column+
	        	".";
	    }
	}
	private final int token=EOF_INT;
	private final BufferedReader br;
	private final String nameOfSomethingThatWeAreReadingFrom;
	private final BigInteger line=new BigInteger("0");
	private final BigInteger column=new BigInteger("0");
	
	private SphereLoader(BufferedReader br, String nameOfSomethingThatWeAreReadingFrom) throws IOException, SyntaxException {
		super();
		this.br = br;
		this.nameOfSomethingThatWeAreReadingFrom = nameOfSomethingThatWeAreReadingFrom;
		gettok();//take the first character in the reader
		sphere=sphere();
		expect(EOF_INT, "End of stream");//GTD think about circular streams: like ones on the neck
	}
	private Sphere sphere() throws IOException, SyntaxException{//TODO we handle closed worlds not emitting energy, think about how we can handle other worlds
		wsAndComments();
		expect('(', "Left round bracket");
		List<Sphere> items=new LinkedList<Sphere>();
		while(token[0]!=')'&&token[0]!=EOF_INT){
			items.add(item());
			wsAndComments();
		}
		expect(')', "Right round bracket");
		wsAndComments();
		return new SphereImpl(items);
	}
	private Sphere item() throws IOException, SyntaxException {
		switch(token[0]){
		case '(': return sphere();
		case ' ':
		case '\n':
		case '\r':
		case '\t': 
		case ';': return new NullSphereImpl();
		default: return word();
		}
	}
	private Sphere word() throws IOException {
		StringBuilder sb=new StringBuilder();
		character_loop:
		while(true){
			switch(token[0]){
			case ' ':
			case '\n':
			case '\r':
			case '\t':
			case EOF_INT:
				break character_loop;
			}
			sb.append((char)token[0]);
			gettok();
		}
		return new WordSphereImpl(sb.toString());
	}
	public static SphereLoader lowlevel_fromDotSphereFile(Reader r, String nameOfSomethingThatWeAreReadingFrom)throws IOException{
		return lowlevel_fromDotSphereFile(new BufferedReader(r,64*1024), nameOfSomethingThatWeAreReadingFrom)
	}
	public static SphereLoader lowlevel_fromDotSphereFile(BufferedReader br, String nameOfSomethingThatWeAreReadingFrom)throws IOException, SyntaxException{
		return new SphereLoader(br, nameOfSomethingThatWeAreReadingFrom);
	}
}
