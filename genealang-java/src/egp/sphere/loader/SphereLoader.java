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
	public static SphereLoader lowlevel_fromDotSphereFile(File file)throws IOException, SyntaxException{
		FileInputStream fis=new FileInputStream(file);
		BufferedInputStream bis=new BufferedInputStream(fis,64*1024);
		Reader r=new InputStreamReader(bis);
		BufferedReader r2=new BufferedReader(r,64*1024);
		return lowlevel_fromDotSphereFile(r2,file.getAbsolutePath());
	}
	public Sphere get(){
		return sphere;
	}
	private Sphere sphere;
	private void gettok() throws IOException{
//		int ch;
		token=
//			ch=
				br.read();
//		column=column.add(ONE);
//		switch(ch){
//		case '\r':
//		case '\n':
//			break;
//		}
	}
	private static final BigInteger ZERO=new BigInteger("0");
	private static final BigInteger ONE=new BigInteger("1");
	private void ws() throws IOException{
		while(true){
			switch(token){
			case ' ':
				column=column.add(ONE);
				gettok();
				continue;
			case '\t':
//				while(!column.mod(EIGHT).equals(ZERO))column=column.add(ONE);
				column=column.add(ONE);
				gettok();
				continue;
			case '\r':
			case '\n':
				column=ONE;
				line=line.add(ONE);
				gettok();
				continue;
			default:
				break;
			}
			break;
		}
	}
	private void wsAndComments() throws IOException{
		while(true){
			ws();
			if(token==';'){
				gettok();//skip character `;'
				br.readLine();
				token=' ';
				column=ZERO;
				line=line.add(ONE);
				continue;
			}
			break;
		}
	}
	private void expect(int chExpected, String chDisplayName) throws IOException, SyntaxException{
		if(token!=chExpected)
			throw new SyntaxException("Character \""+chDisplayName+"\" {"+
					char2charStringDisplayNameRepresentation(chExpected)+"} expected, but found: {"+
					char2charStringDisplayNameRepresentation(token)+"}.");
		if(chExpected!=EOF_INT)if(token!=EOF_INT){
			switch(token){
			case '\r':
			case '\n':
				line=line.add(ONE);
				column=ONE;
				break;
			default:
				column=column.add(ONE);
				break;
			}
			gettok();
		}
	}
	private static String char2charStringDisplayNameRepresentation(int ch){
		if(ch==EOF_INT)return "EOF";
		return "'"+(char)ch+"', (char)"+ch;
	}
	public class SyntaxException extends Exception{
		private String nameOfFrom;
		private BigInteger line;
		private BigInteger column;
		private void init() {
			SyntaxException.this.line = SphereLoader.this.getCurrentLine();
			SyntaxException.this.column = SphereLoader.this.getCurrentColumn();
			SyntaxException.this.nameOfFrom = SphereLoader.this.getNameOfFrom();
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
	        	" From: \""+getNameOfFrom()+
	        	"\", line: "+getLine()+
	        	", column: "+getColumn()+
	        	".";
	    }
	}
	private int token=EOF_INT;
	private final BufferedReader br;
	private final String nameOfSomethingThatWeAreReadingFrom;
	private BigInteger line=ONE;
	private BigInteger column=ONE;
	
	private SphereLoader(BufferedReader br, String nameOfSomethingThatWeAreReadingFrom) throws IOException, SyntaxException {
		super();
		this.br = br;
		this.nameOfSomethingThatWeAreReadingFrom = nameOfSomethingThatWeAreReadingFrom;
		gettok();//take the first character in the reader
		sphere=sphere();
		expect(EOF_INT, "End of stream");//TODO think about circular streams: like ones on the neck
	}
	public String getNameOfFrom() {
		return nameOfSomethingThatWeAreReadingFrom;
	}
	public BigInteger getCurrentColumn() {
		return column;
	}
	public BigInteger getCurrentLine() {
		return line;
	}
	private Sphere sphere() throws IOException, SyntaxException{//TODO we handle closed worlds not emitting energy, think about how we can handle other worlds
		wsAndComments();
		expect('(', "Left round bracket");
		List<Sphere> items=new LinkedList<Sphere>();
		while(token!=')'&&token!=EOF_INT){
			items.add(item());
			wsAndComments();
		}
		expect(')', "Right round bracket");
		wsAndComments();
		return new SphereImpl(items);
	}
	private Sphere item() throws IOException, SyntaxException {
		switch(token){
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
			switch(token){
			case '\n':
			case '\r':
			case ' ':
			case '\t':
			case EOF_INT:
			case '(':
			case ')':
				break character_loop;
			}
			column=column.add(ONE);
			sb.append((char)token);
			gettok();
		}
		return new WordSphereImpl(sb.toString());
	}
	public static SphereLoader lowlevel_fromDotSphereFile(BufferedReader br, String nameOfSomethingThatWeAreReadingFrom)throws IOException, SyntaxException{
		return new SphereLoader(br, nameOfSomethingThatWeAreReadingFrom);
	}
}
