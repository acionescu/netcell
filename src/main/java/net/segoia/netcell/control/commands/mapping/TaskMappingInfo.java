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
package net.segoia.netcell.control.commands.mapping;

import java.util.List;

import net.segoia.distributed.framework.DistributedServiceDescription;
import net.segoia.distributed.framework.ProcessingResponseReceiver;

public class TaskMappingInfo {
    private DistributedServiceDescription serviceDescription;
    private String method;
    private boolean broadcast;
    private boolean asynchronous;
    private boolean stopOnFailedOn;
    private boolean sendCommandContextAsParamOn;
    private List<String> inputParamNames;
    private String outputParamName;
    private boolean useResponseAsOutputContextOn;
    private ProcessingResponseReceiver responseReceiver;
    /**
     * @return the method
     */
    public String getMethod() {
        return method;
    }
   
    /**
     * @return the broadcast
     */
    public boolean isBroadcast() {
        return broadcast;
    }
    /**
     * @return the asynchronous
     */
    public boolean isAsynchronous() {
        return asynchronous;
    }
    /**
     * @param method the method to set
     */
    public void setMethod(String method) {
        this.method = method;
    }
   
    /**
     * @param broadcast the broadcast to set
     */
    public void setBroadcast(boolean broadcast) {
        this.broadcast = broadcast;
    }
    /**
     * @param asynchronous the asynchronous to set
     */
    public void setAsynchronous(boolean asynchronous) {
        this.asynchronous = asynchronous;
    }
    /**
     * @return the stopOnFailedOn
     */
    public boolean isStopOnFailedOn() {
        return stopOnFailedOn;
    }
    /**
     * @param stopOnFailedOn the stopOnFailedOn to set
     */
    public void setStopOnFailedOn(boolean stopOnFailedOn) {
        this.stopOnFailedOn = stopOnFailedOn;
    }

    /**
     * @return the serviceDescription
     */
    public DistributedServiceDescription getServiceDescription() {
        return serviceDescription;
    }

    /**
     * @param serviceDescription the serviceDescription to set
     */
    public void setServiceDescription(DistributedServiceDescription serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    /**
     * @return the inputParamNames
     */
    public List<String> getInputParamNames() {
        return inputParamNames;
    }

    /**
     * @param inputParamNames the inputParamNames to set
     */
    public void setInputParamNames(List<String> inputParamNames) {
        this.inputParamNames = inputParamNames;
    }

    /**
     * @return the responseReceiver
     */
    public ProcessingResponseReceiver getResponseReceiver() {
        return responseReceiver;
    }

    /**
     * @param responseReceiver the responseReceiver to set
     */
    public void setResponseReceiver(ProcessingResponseReceiver responseReceiver) {
        this.responseReceiver = responseReceiver;
    }

    /**
     * @return the sendCommandContextAsParamOn
     */
    public boolean isSendCommandContextAsParamOn() {
        return sendCommandContextAsParamOn;
    }

    /**
     * @param sendCommandContextAsParamOn the sendCommandContextAsParamOn to set
     */
    public void setSendCommandContextAsParamOn(boolean sendCommandContextAsParamOn) {
        this.sendCommandContextAsParamOn = sendCommandContextAsParamOn;
    }

    /**
     * @return the outputParamName
     */
    public String getOutputParamName() {
        return outputParamName;
    }

    /**
     * @return the useResponseAsOutputContextOn
     */
    public boolean isUseResponseAsOutputContextOn() {
        return useResponseAsOutputContextOn;
    }

    /**
     * @param outputParamName the outputParamName to set
     */
    public void setOutputParamName(String outputParamName) {
        this.outputParamName = outputParamName;
    }

    /**
     * @param useResponseAsOutputContextOn the useResponseAsOutputContextOn to set
     */
    public void setUseResponseAsOutputContextOn(boolean useResponseAsOutputContextOn) {
        this.useResponseAsOutputContextOn = useResponseAsOutputContextOn;
    }
    
}
