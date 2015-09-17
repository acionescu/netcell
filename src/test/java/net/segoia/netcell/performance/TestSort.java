/**
 * netcell - Java ESB with an embedded business process modeling engine
 * Copyright (C) 2009  Adrian Cristian Ionescu - https://github.com/acionescu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.segoia.netcell.performance;

import java.util.ArrayList;
import java.util.List;

import net.segoia.netcell.config.BaseEngineConfigTestCase;
import net.segoia.netcell.entities.GenericEntity;
import net.segoia.util.data.GenericNameValueContext;

public class TestSort extends BaseEngineConfigTestCase{
    
    private List<Integer> generateRandomList(int count, int max){
	List<Integer> list = new ArrayList<Integer>();
	for(int i=0;i<count;i++){
	    int n = (int)(Math.random()*max);
	    list.add(n);
	}
	return list;
    }

    private List<Integer> quickSort(List<Integer> input){
	List<Integer> left = new ArrayList<Integer>();
	List<Integer> right = new ArrayList<Integer>();
	int size = input.size();
	if(size < 2){
	    return input;
	}
	int pivot=input.get(0);
	
	for(int i=1;i<size;i++){
	    int e = input.get(i);
	    if( e < pivot ){
		left.add(e);
	    }
	    else{
		right.add(e);
	    }
	}
	left = quickSort(left);
	right = quickSort(right);
	left.add(pivot);
	left.addAll(right);
	return left;
    }
    
    public void test() throws Exception{
	List<Integer> input = generateRandomList(1000, 10000);
	System.out.println("input: "+input);
	long counter = System.currentTimeMillis();
	List<Integer> nativeOutput = quickSort(input);
	long nativeTime = System.currentTimeMillis()-counter;
	System.out.println("native: "+nativeOutput);
	System.out.println("native time: "+nativeTime);
	
//	GenericEntity<GenericNameValueContext> entity = (GenericEntity)cfgManager.getObjectById("quick-sort");
//	assertNotNull(entity);
	GenericNameValueContext c = new GenericNameValueContext();
	c.put("fid","quick-sort");
	c.put("original",input.toString());
	counter = System.currentTimeMillis();
	GenericNameValueContext res = engineController.execute(c);
	long engineTime = System.currentTimeMillis()-counter;
	System.out.println("engine: "+res.get("sorted"));
	System.out.println("engine time: "+engineTime);
	
    }
}
