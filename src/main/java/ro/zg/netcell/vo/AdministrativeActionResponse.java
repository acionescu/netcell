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
package ro.zg.netcell.vo;

import java.io.Serializable;

public class AdministrativeActionResponse implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = -4461431557777612153L;
    /* a general purpose identificator for this response */
    private String id;
    /* the actual content of the response*/
    private Serializable content;
    
    private Exception error;
    
    
    public AdministrativeActionResponse(String id, Serializable content) {
	super();
	this.id = id;
	this.content = content;
    }
    
    
    public AdministrativeActionResponse(String id, Serializable content, Exception error) {
	super();
	this.id = id;
	this.content = content;
	this.error = error;
    }


    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    /**
     * @return the content
     */
    public Serializable getContent() {
        return content;
    }
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * @param content the content to set
     */
    public void setContent(Serializable content) {
        this.content = content;
    }
    
    
}
