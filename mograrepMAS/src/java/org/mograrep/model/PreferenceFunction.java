package org.mograrep.model;

import java.util.List;

public interface PreferenceFunction {
	
	
	public List<ContextDeviation> compare(ContextInformation agreed, ContextInformation performed);
	
	

}
