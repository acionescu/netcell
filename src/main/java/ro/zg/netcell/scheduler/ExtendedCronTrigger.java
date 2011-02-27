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
package ro.zg.netcell.scheduler;

import java.util.Date;

import org.quartz.CronTrigger;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

public class ExtendedCronTrigger extends CronTrigger implements ExtendedTrigger {

    /**
     * 
     */
    private static final long serialVersionUID = 5516558554501159477L;
    /**
     * 
     */
    public static final int MISFIRE_INSTRUCTION_FIRE_WHEN_POSSIBLE = 3;

    private int allowedConcurentJobsCount;

    public void updateAfterMisfire(org.quartz.Calendar cal) {
	super.updateAfterMisfire(cal);
	int instr = getMisfireInstruction();
	if (instr == MISFIRE_INSTRUCTION_FIRE_WHEN_POSSIBLE) {
	    Date newFireTime = getFireTimeAfter(new Date());
            while (newFireTime != null && cal != null
                    && !cal.isTimeIncluded(newFireTime.getTime())) {
                newFireTime = getFireTimeAfter(newFireTime);
            }
            setNextFireTime(newFireTime);
	}
    }

    protected boolean validateMisfireInstruction(int misfireInstruction) {
	if (misfireInstruction < MISFIRE_INSTRUCTION_SMART_POLICY) {
	    return false;
	}

	if (misfireInstruction > MISFIRE_INSTRUCTION_FIRE_WHEN_POSSIBLE) {
	    return false;
	}

	return true;
    }

    /**
     * @return the allowedConcurentJobsCount
     */
    public int getAllowedConcurentJobsCount() {
	return allowedConcurentJobsCount;
    }

    /**
     * @param allowedConcurentJobsCount
     *            the allowedConcurentJobsCount to set
     */
    public void setAllowedConcurentJobsCount(int allowedConcurentJobsCount) {
	this.allowedConcurentJobsCount = allowedConcurentJobsCount;
    }

    public void handleMissfiredJob(JobExecutionContext context) {
	int instr = getMisfireInstruction();
	if (instr == MISFIRE_INSTRUCTION_FIRE_WHEN_POSSIBLE) {
	    try {
		context.getScheduler().triggerJob(getJobName(), getJobGroup());
	    } catch (SchedulerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
	
    }

}
