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
package net.segoia.netcell.definitions;

import java.util.ArrayList;
import java.util.List;

public class ThreadTest {

    
    public synchronized void fistMethod(){
	System.out.println("First method");
	try {
	    Thread.sleep(5000);
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
    public synchronized void secondMethod(){
	System.out.println("Second method");
	try {
	    Thread.sleep(5000);
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
    class Accessor extends Thread{
	private String method;
	private ThreadTest tt;
	public Accessor(String method, ThreadTest tt){
	    this.method = method;
	    this.tt = tt;
	}
	
	public void run(){
	    if(method.equals("F")){
		tt.fistMethod();
	    }
	    else{
		tt.secondMethod();
	    }
	}
    }
    
    public static void main(String[] args) throws Exception{
//	ThreadTest tt = new ThreadTest();
//	
//	List<Thread> threads = new ArrayList<Thread>();
//	
//	for( int i=0;i<10;i++){
//	    threads.add(tt.new Accessor("F",tt));
//	}
//	
//	for( int i=0;i<10;i++){
//	    threads.add(tt.new Accessor("S",tt));
//	}
//	
//	for(Thread t : threads){
//	    t.start();
//	}
//	
//	Thread.sleep(353554545);
	
	String a="a";
	String b=a;
	a="c";
	System.out.println(b);
    }
}
