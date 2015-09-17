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
package net.segoia.netcell.security;

public class AccessRule {
    private String id;
    private String targetPathRegex;
    private String validationFlowId;
    
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTargetPathRegex() {
        return targetPathRegex;
    }
    public void setTargetPathRegex(String targetPathRegex) {
        this.targetPathRegex = targetPathRegex;
    }
    public String getValidationFlowId() {
        return validationFlowId;
    }
    public void setValidationFlowId(String validationFlowId) {
        this.validationFlowId = validationFlowId;
    }
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((id == null) ? 0 : id.hashCode());
	result = prime * result
		+ ((targetPathRegex == null) ? 0 : targetPathRegex.hashCode());
	result = prime
		* result
		+ ((validationFlowId == null) ? 0 : validationFlowId.hashCode());
	return result;
    }
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	AccessRule other = (AccessRule) obj;
	if (id == null) {
	    if (other.id != null)
		return false;
	} else if (!id.equals(other.id))
	    return false;
	if (targetPathRegex == null) {
	    if (other.targetPathRegex != null)
		return false;
	} else if (!targetPathRegex.equals(other.targetPathRegex))
	    return false;
	if (validationFlowId == null) {
	    if (other.validationFlowId != null)
		return false;
	} else if (!validationFlowId.equals(other.validationFlowId))
	    return false;
	return true;
    }
    @Override
    public String toString() {
	return "AccessRule [id=" + id + ", targetPathRegex=" + targetPathRegex
		+ ", validationFlowId=" + validationFlowId + "]";
    }
    
    
}
