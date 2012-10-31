package org.mograrep.utils;

import java.util.List;

public interface ReduceFunction<In, Out> {
	public Out reduce(List<In> in);
}
