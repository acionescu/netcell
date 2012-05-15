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
import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Enumeration;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;

import ro.zg.util.data.reflection.ReflectionUtility;

public class Dummy {
    public static void main(String[] args) throws Exception {
	// URI uri = new URI("http://dear_raed.blogspot.com/");
	// System.out.println(uri.getHost());
	// HttpHost hh = new
	// HttpHost(uri.getHost(),uri.getPort(),uri.getScheme());
	//

	// System.out.println(StringEscapeUtils.unescapeHtml("measure)."));
	// System.out.println(StringUtils.strip(" \t\r\n bla\t \r\n ",
	// " \t\r\n"));
	//
	// String s="<a href=\"newreply.php?p=4669260&amp;noquote=1\" \n" +
	// "			class=\"newcontent_textcontrol\" id=\"newreplylink_bottom\"\n" +
	// "			><span>+</span> Reply to Thread</a>\n" +
	// "";
	// System.out.println(s.contains("\t"));

//	Enumeration<NetworkInterface> nets = NetworkInterface
//		.getNetworkInterfaces();
//
//	for (NetworkInterface netIf : Collections.list(nets)) {
//	    System.out.printf("Display name: %s\n", netIf.getDisplayName());
//	    System.out.printf("Name: %s\n", netIf.getName());
//
//	    Enumeration<InetAddress> inetAddresses = netIf.getInetAddresses();
//	    for (InetAddress ia : Collections.list(inetAddresses)) {
//		System.out.println(ia.getHostAddress());
//	    }
//
//	    Enumeration<NetworkInterface> subIfs = netIf.getSubInterfaces();
//	    for (NetworkInterface subIf : Collections.list(subIfs)) {
//		System.out.printf("\tSub Interface Display name: %s\n",
//			subIf.getDisplayName());
//		System.out
//			.printf("\tSub Interface Name: %s\n", subIf.getName());
//	    }
//	    System.out.printf("\n");
//	}

	File f = new File("/D:/Adrian Ionescu/work/dev/eclipse-workspace/netcell/target/test-classes/");
	System.out.println(f.exists());
	for(String name : f.list()){
	    System.out.println(name);
	}
    }
}
