package egp.sphere.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import egp.sphere.model.Sphere;
import egp.sphere.model.WordSphere;
import egp.sphere.model.Sphere.DuplicateLabelFoundException;
import gtd.GTD;

public class SphereImpl implements Sphere {
	private final List<Sphere> items;
	
	public SphereImpl(List<Sphere> items) {
		this.items=items;
	}
	
	public SphereImpl() {
		this.items=new ArrayList<Sphere>();
	}

	@Override
	public List<Sphere> getChildItems() {
		return Collections.unmodifiableList(items);
	}

	@Override
	public Sphere getUniqueSphereLabeled(String label) throws LabelNotFoundException, DuplicateLabelFoundException {
		List<Sphere> spheresMatchingGivenLabel=getRecursivelyAllSpheresLabeled(label);
		if(spheresMatchingGivenLabel.isEmpty())throw new LabelNotFoundException();
		if(spheresMatchingGivenLabel.size()>=2)throw new DuplicateLabelFoundException();
		if(spheresMatchingGivenLabel.size()!=1)throw new AssertionError();
		return spheresMatchingGivenLabel.get(0);
	}
	//GTD make this routine work
	@Override
	public String concatenateAllContentsNoLabel_CharactersLengthMax(
			int charsLengthMax) {
		Sphere sphere=items.get(0);
		StringBuilder sb=new StringBuilder(charsLengthMax);
		if(!(sphere instanceof WordSphere))throw new AssertionError();
		for(int i=1;;i++){
			if(i>=items.size())break;
			sphere=items.get(i);
			if(!(sphere instanceof WordSphere))throw new AssertionError();
			WordSphere ws=(WordSphere) sphere;
			sb.append(ws.getFullQuote());
			if(sb.length()>charsLengthMax)break;
			if(i<items.size()-1)sb.append(' ');
		}
		
		if(sb.length()>charsLengthMax){
			sb.delete(charsLengthMax-3, sb.length());
			String s=sb.toString().trim();
			return s+"...";
		}
		return sb.toString();
	}
	@Override
	public List<Sphere> getRecursivelyAllSpheresLabeled(String label) {
		List<Sphere> spheresMatchingGivenLabel=new LinkedList<Sphere>();
		//if this sphere matches, add it 
		Sphere sphere1=items.get(0);
		if(sphere1 instanceof WordSphere){
			WordSphere wordSphere=(WordSphere) sphere1;
			if(wordSphere.getFullQuote().equals(label)){
				spheresMatchingGivenLabel.add(this);
			}
		}
		//add spheres from every child item
		for(Sphere s:items){
			List<Sphere> s_AllSpheresLabeled=s.getRecursivelyAllSpheresLabeled(label);
			spheresMatchingGivenLabel.addAll(s_AllSpheresLabeled);
		}
		return spheresMatchingGivenLabel;
	}
}
