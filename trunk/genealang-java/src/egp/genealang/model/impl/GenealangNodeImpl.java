package egp.genealang.model.impl;

import egp.genealang.model.GenealangNode;
import egp.sphere.model.DuplicateLabelFoundException;
import egp.sphere.model.LabelNotFoundException;
import gtd.GTD;

public class GenealangNodeImpl implements GenealangNode{

	@Override
	public String getDisplayName() throws LabelNotFoundException,
			DuplicateLabelFoundException {
		// GTD Auto-generated method stub
		GTD.gtd();
		return "";
	}

}
