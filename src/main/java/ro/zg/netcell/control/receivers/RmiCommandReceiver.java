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
package ro.zg.netcell.control.receivers;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.Enumeration;

import ro.zg.netcell.control.NetCell;
import ro.zg.util.io.Receiver;
import ro.zg.util.logging.Logger;
import ro.zg.util.logging.MasterLogManager;

public class RmiCommandReceiver implements Receiver {
    private static Logger logger = MasterLogManager
	    .getLogger("RmiCommandReceiver");
    private String bindName;
    private int bindPort;
    private int registryPort;
    private NetCell engine;
    private Registry registry;

    public void listen() throws Exception {
	Enumeration<NetworkInterface> nie = NetworkInterface
		.getNetworkInterfaces();
	String bindAddress = "localhost";
	// for (NetworkInterface ni = nie.nextElement(); nie.hasMoreElements();
	// ni = nie
	// .nextElement()) {
	// System.out.println(ni.getDisplayName());
	// for (InterfaceAddress ia : ni.getInterfaceAddresses()) {
	// if (ia.getAddress() instanceof Inet4Address) {
	// bindAddress = ia.getAddress().getHostAddress();
	//
	// if (!bindAddress.equals("127.0.0.1")) {
	// break;
	// }
	// logger.info("skipping bind address: "+bindAddress);
	// }
	// }
	// }

	Enumeration<NetworkInterface> nets = NetworkInterface
		.getNetworkInterfaces();

	for (NetworkInterface netIf : Collections.list(nets)) {
	    System.out.printf("Display name: %s\n", netIf.getDisplayName());
	    System.out.printf("Name: %s\n", netIf.getName());

	    if ("lo".equals(netIf.getName())) {
		continue;
	    }
	    Enumeration<InetAddress> inetAddresses = netIf.getInetAddresses();
	    for (InetAddress ia : Collections.list(inetAddresses)) {
		System.out.println(ia.getHostAddress());
		bindAddress = ia.getHostAddress();
	    }

	    Enumeration<NetworkInterface> subIfs = netIf.getSubInterfaces();
	    for (NetworkInterface subIf : Collections.list(subIfs)) {
		System.out.printf("\tSub Interface Display name: %s\n",
			subIf.getDisplayName());
		System.out
			.printf("\tSub Interface Name: %s\n", subIf.getName());
	    }
	    System.out.printf("\n");
	}

	logger.info("Using " + bindAddress + " as rmi hostname and " + bindPort
		+ " as bind port.");
	System.setProperty("java.rmi.server.hostname", bindAddress);

	NetCell stub = (NetCell) UnicastRemoteObject.exportObject(engine,
		bindPort);
	registry = LocateRegistry.createRegistry(registryPort);
	registry.bind(bindName, stub);
    }

    public void shutdown() throws Exception {
	registry.unbind(bindName);
	registry = null;
    }

    public String getBindName() {
	return bindName;
    }

    public int getBindPort() {
	return bindPort;
    }

    public int getRegistryPort() {
	return registryPort;
    }

    public NetCell getEngine() {
	return engine;
    }

    public void setBindName(String bindName) {
	this.bindName = bindName;
    }

    public void setBindPort(int bindPort) {
	this.bindPort = bindPort;
    }

    public void setRegistryPort(int registryPort) {
	this.registryPort = registryPort;
    }

    public void setEngine(NetCell engine) {
	this.engine = engine;
    }

}
