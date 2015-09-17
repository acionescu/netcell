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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class EngineClient {

    public static void main(String[] args) throws Exception{
	SocketAddress sockaddr = new InetSocketAddress("localhost", 9000);
        Socket client = new Socket();
        int timeoutMs = 2000;   // 2 seconds
        client.connect(sockaddr, timeoutMs);
        String input ="";
        BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter consoleOut = new BufferedWriter(new OutputStreamWriter(System.out));
        BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        do{
//            System.out.println("Enter command:");
            input = consoleIn.readLine();
            writer.write(input);
            writer.newLine();
            writer.flush();
            System.out.println("Response:");
//            consoleOut.write(reader.r);
            System.out.println(reader.readLine());
        }
        while(!input.equals("exit"));
        client.close();
    }
    
    private static String readFromSocket(InputStream is) throws Exception{
	 byte[] buffer = new byte[1024];
	 int numRead;
	 long numWritten = 0;
	 StringBuilder out = new StringBuilder();
	 while ((numRead = is.read(buffer)) != -1) {
	 out.append(new String(buffer, 0, numRead));
	 numWritten += numRead;
	 }
	 return out.toString();
    }
}
