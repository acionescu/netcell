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
package ro.zg.netcell.async;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import ro.zg.netcell.vo.WorkflowContext;
import ro.zg.util.data.GenericNameValueContext;
import ro.zg.util.processing.Processor;

public class AsyncCallManagerConfig {
    private ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newCachedThreadPool();
    private Processor<WorkflowContext, GenericNameValueContext> processor = new NetCellWrapper();
   
    /**
     * @return the executor
     */
    public ThreadPoolExecutor getExecutor() {
        return executor;
    }
    /**
     * @return the processor
     */
    public Processor<WorkflowContext, GenericNameValueContext> getProcessor() {
        return processor;
    }
   
    /**
     * @param executor the executor to set
     */
    public void setExecutor(ThreadPoolExecutor executor) {
        this.executor = executor;
    }
    /**
     * @param processor the processor to set
     */
    public void setProcessor(Processor<WorkflowContext, GenericNameValueContext> processor) {
        this.processor = processor;
    }
   
}
