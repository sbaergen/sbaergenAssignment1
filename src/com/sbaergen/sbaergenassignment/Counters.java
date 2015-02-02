/*Copyright 2015 Sean Baergen
*Licensed under the Apache License, Version 2.0 (the "License");
*you may not use this file except in compliance with the License.
*You may obtain a copy of the License at
*http://www.apache.org/licenses/LICENSE-2.0
*Unless required by applicable law or agreed to in writing, software
*distributed under the License is distributed on an "AS IS" BASIS,
*WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*See the License for the specific language governing permissions and
*limitations under the License.
*/
package com.sbaergen.sbaergenassignment;

public class Counters {
	static int claimCount = 0;
	static int expCount = 0;

	public int getCount() {
		return claimCount;
	}

	public void setCount(int count) {
		Counters.claimCount = count;
	}

	public static int getExpCount() {
		return expCount;
	}

	public static void setExpCount(int expCount) {
		Counters.expCount = expCount;
	}
	
}
