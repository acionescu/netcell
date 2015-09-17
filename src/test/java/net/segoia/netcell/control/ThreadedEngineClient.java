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
package net.segoia.netcell.control;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import net.segoia.util.net.nio.NioClient;
import net.segoia.util.net.nio.RspHandler;
import net.segoia.util.statistics.Monitor;

public class ThreadedEngineClient {

    public static void main(String[] args) throws Exception {
	NioClient engineClient = new NioClient(InetAddress.getByName("localhost"), 9000);
	Thread clientThread = new Thread(engineClient);
	clientThread.setDaemon(true);
	clientThread.start();
	List<Thread> threads = new ArrayList<Thread>();
	for (int i = 0; i < 100; i++) {
	    Thread t = new Thread(new SortListRequest(engineClient,i));
	    threads.add(t);
	    t.start();
	    Thread.sleep(5);
	}
	for (Thread t : threads) {
	    if (t.isAlive()) {
		System.out.println("joining " + t);
		t.join();
	    }
	}
	System.out.println("Finshed");
	Monitor execMon = Monitor.getMonitor("execMon");
	System.out.println("avg process time : " + execMon.getAverageExecutionTime());
	System.out.println("min process time : "+execMon.getMinExecutionTime());
	System.out.println("max process time : "+execMon.getMaxExecutionTime());
	System.out.println("req count : " + execMon.getCallCount());
	System.out.println("pending calls : " + execMon.getPendingCallsCount());
	System.out.println("req/sec : " + execMon.getAverageRps());
    }

}

class SortListRequest implements Runnable {
    private NioClient client;
    int id;
    
    public SortListRequest(NioClient cl,int id) {
	client = cl;
	this.id = id;
    }

    public void run() {
	System.out.println("Start "+Thread.currentThread());
	String input = getUnsortedList().toString();
	RspHandler handler = new RspHandler();
	try {
	    String command = "execute(fid=test.sorting.quick-sort, req=req"+id+", input=" + input + ")\r";
	    Monitor execMon = Monitor.getMonitor("execMon");
	    long counterId = execMon.startCounter();
	    client.send(command.getBytes(), handler);
	    String output = handler.getResponseAsString();
	    execMon.stopCounter(counterId);
	    System.out.println(Thread.currentThread() + " : " + input + " -> " + output);
	    ;
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    private List<Integer> getUnsortedList() {
	int count = 10;// 2 + (int)(Math.random()*18);
	List<Integer> list = new ArrayList<Integer>();

	for (int i = 0; i < count; i++) {
	    list.add((int) (Math.random() * 100));
	}
	return list;
    }

}
