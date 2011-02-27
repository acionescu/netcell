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



public class HbaseTest {
    public static void main(String[]  args) throws Exception {
//	HBaseConfiguration c = new HBaseConfiguration();
//	Scan s = new Scan();
//	
//	BinaryComparator bp = new BinaryComparator(Bytes.toBytes("req87"));
//	SingleColumnValueFilter vf = new SingleColumnValueFilter(Bytes.toBytes("input"),Bytes.toBytes("req"), CompareOp.EQUAL,bp);
//	SkipFilter sf = new SkipFilter(vf);
//	s.setFilter(sf);
//	
//	HTable table = new HTable(c,"requests");
//	
//	ResultScanner scanner = table.getScanner(s);
//	
//	try {
//	    // Scanners return Result instances.
//	    // Now, for the actual iteration. One way is to use a while loop like so:
//	    for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {
//		// print out the row we found and the columns we were looking for
//		System.out.println("Found row: " + rr);
//		System.out.println(Bytes.toString(rr.getFamilyMap(Bytes.toBytes("input")).get(Bytes.toBytes("req"))));
//		System.out.println(rr.getFamilyMap(Bytes.toBytes("output")));
//	    }
//
//	    // The other approach is to use a foreach loop. Scanners are iterable!
//	    // for (Result rr : scanner) {
//	    // System.out.println("Found row: " + rr);
//	    // }
//	} finally {
//	    // Make sure you close your scanners when you are done!
//	    // Thats why we have it inside a try/finally clause
//	    scanner.close();
//	}
    }
}
