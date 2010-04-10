package egp.genealang.util;

import java.util.Enumeration;
import java.util.Iterator;

public class IteratorEnumeration<E> implements Enumeration<E>{
	private final Iterator<E> iter;
	public IteratorEnumeration(Iterator<E> iter){
		this.iter=iter;
		hasMoreElements();//check asserts
	}
	@Override
	public boolean hasMoreElements() {
		return iter.hasNext();
	}
	@Override
	public E nextElement() {
		return iter.next();
	}
}
