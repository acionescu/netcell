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

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

public class DummyJob implements Job {

    public void execute(JobExecutionContext context) throws JobExecutionException {
//	System.out.println("Executing job at " + System.currentTimeMillis());
	try {
	    System.out.println("Jobs executing at "+System.currentTimeMillis()+" : "+context.getScheduler().getCurrentlyExecutingJobs().size());
	} catch (SchedulerException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}
	try {
	    Thread.sleep(15000);
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	System.out.println("Finished");
    }
}
