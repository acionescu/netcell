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

import org.mindrot.jbcrypt.BCrypt;

public class CryptUtil {
    
    public static String hashBCrypt(String target, Integer logRounds) {
	if(logRounds == null || logRounds < 10) {
	    return BCrypt.hashpw(target, BCrypt.gensalt());
	}
	return BCrypt.hashpw(target, BCrypt.gensalt(logRounds));
    }
    
    public static String hashBCrypt(String target, String salt) {
	
	return BCrypt.hashpw(target, salt);
    }

    
    public static boolean checkBCrypt(String plainText, String hashed) {
	return BCrypt.checkpw(plainText, hashed);
    }
    
    
    public static void main(String[] args) {
	String pass = "heloo*$HHG";
	String salt = BCrypt.gensalt(15);
	String hashed = hashBCrypt(pass,(Integer)null);
	System.out.println(salt);
	System.out.println(hashed);
	System.out.println(checkBCrypt(pass, hashed));
    }
    
}
