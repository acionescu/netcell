/*******************************************************************************
 * Copyright 2011 Adrian Cristian Ionescu
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package ro.zg.netcell.control;

import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

import ro.zg.netcell.scheduler.ExtendedCronTrigger;
import ro.zg.netcell.scheduler.ExtendedJobExecutionController;

public class SchedulerTest {
    public static void main(String[] args) throws Exception {
	StdSchedulerFactory fact = new StdSchedulerFactory();

	Scheduler scheduler = fact.getScheduler();
//	 scheduler.start();
	ExtendedJobExecutionController jobController = new ExtendedJobExecutionController();
	scheduler.addJobListener(jobController);
	scheduler.addTriggerListener(jobController);

	JobDetail jb = new JobDetail("test-job", "group1", DummyJob.class);
	 ExtendedCronTrigger trigger = new ExtendedCronTrigger();
	 trigger.setAllowedConcurentJobsCount(3);
	 trigger.setMisfireInstruction(3);
	// CronTrigger trigger = new CronTrigger("trigger1", "group1", "test-job", "group1", "0/3 * * * * ?");
//	CronTrigger trigger = new CronTrigger();
	 trigger.setStartTime(new Date());
	 trigger.setName("test-trigger");
	 trigger.setCronExpression("0/4 * * * * ?");

	jb.addJobListener(jobController.getName());
	trigger.addTriggerListener(jobController.getName());

	scheduler.scheduleJob(jb, trigger);
	// scheduler.addJob(jb,true);

	// JobDetail jobDetail = new JobDetail("myJob", "test-group", DummyJob.class);
	//
	// Trigger trigger2 = TriggerUtils.makeSecondlyTrigger();
	// trigger2.setName("myTrigger");
	// trigger2.setGroup("test-group");
	// scheduler.scheduleJob(jobDetail, trigger2);

	scheduler.start();

	// Thread.sleep(100000);
	
//	Object l= new Object();
//	Callee c= new Callee();
//	Tr tr1 = new Tr(c,"m1",l);
//	Tr tr2 = new Tr(c,"m2",l);
//
//	Thread t1 = new Thread(tr1);
//	Thread t2=new Thread(tr2);
//	
//	t1.start();
//	Thread.sleep(1000);
//	t2.start();
//	
//	Thread.sleep(20000);
    }
}
class Tr implements Runnable{

	private Callee callee;
	private String identifier;
	private Object target;
	
	public Tr(Callee c,String i,Object t) {
	    this.callee=c;
	    identifier = i;
	    this.target=t;
	}
	
	public void run() {
	    if("m1".equals(identifier)) {
		callee.m1(target);
	    }
	    else if("m2".equals(identifier)) {
		callee.m2(target);
	    }
	}
	
}

class Callee{
	
	public void m1(Object o) {
	    synchronized (o) {
		System.out.println(Thread.currentThread()+" in m1");
		try {
		    Thread.sleep(4000);
		} catch (InterruptedException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		System.out.println(Thread.currentThread()+" out of m1");
	    }
	    try {
		Thread.sleep(100);
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    m2(o);
	}
	
	public void m2(Object o) {
	    synchronized (o) {
		System.out.println(Thread.currentThread()+" in m2");
		try {
		    Thread.sleep(5000);
		} catch (InterruptedException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		System.out.println(Thread.currentThread()+" out of m2");
	    }
	    try {
		Thread.sleep(100);
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    m1(o);
	}
}
