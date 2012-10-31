package org.mograrep.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SimpleMapReduce <In, Intermediate, Out> {
	
	

	public Out mapReduce(MapFunction<In, Intermediate> mapF, ReduceFunction<Intermediate, Out> reduceF, List<In> in){
		return reduce(reduceF, map(mapF, in));

	}

	public Out mapReduce(MapFunction<In, Intermediate> mapF, ReduceFunction<Intermediate, Out> reduceF, Iterator<In> it){
		
		return reduce(reduceF, map(mapF, it));
	}




	public List<Intermediate> map(MapFunction<In, Intermediate> function, Iterator<In> it){
		List<Intermediate> intermediate = new ArrayList<Intermediate>();
		while(it.hasNext()){
			intermediate.add(function.f(it.next()));
		}
		return intermediate;
	}

	public List<Intermediate> map(MapFunction<In, Intermediate> function,  List<In> in){
		return map(function, in.iterator());
	}

	public Out reduce(ReduceFunction<Intermediate, Out> function, List<Intermediate> intermediate){
		return function.reduce(intermediate);
	}
}
