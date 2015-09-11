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

import ro.zg.util.bootstrap.GenericBootstrap;

public class NetcellNodeBootstrap {
    public static void main(String[] args) throws Exception {
	String os = System.getProperty("os.name");
	String boostrapFile = "bootstrap.properties";
	if (os.toLowerCase().contains("windows")) {
	    boostrapFile = "win-" + boostrapFile;
	}
//	String filePath = ClassLoader.getSystemClassLoader()
//		.getResource(boostrapFile).getFile();
	
	String filePath = "/media/netcell-node/bootstrap.properties";
	
	
//	if (os.toLowerCase().contains("windows")) {
//	    boostrapFile = "win-" + boostrapFile;
//	    URL fileUrl = ClassLoader.getSystemClassLoader().getResource(
//		    boostrapFile);
//	    filePath = fileUrl.getPath().substring(1);
//	    URI uri = new URI("file:///" + filePath);
//	    GenericBootstrap.start(uri);

//	} 
//	else {
	    String[] grr = new String[] { filePath };
	    GenericBootstrap.start(grr);
//	}
    }
}
