/*******************************************************************************
 * Copyright 2016, 2018 vanilladb.org contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.vanilladb.bench;

import org.vanilladb.bench.util.BenchProperties;

public class BenchmarkerParameters {
	
	public static final long WARM_UP_INTERVAL;
	public static final long BENCHMARK_INTERVAL;
	public static final int NUM_RTES;
	public static final double READ_WRITE_TX_RATE;
	
	public static final String SERVER_IP;
	
	// JDBC = 1, SP = 2
	public static enum ConnectionMode { JDBC, SP };
	public static final ConnectionMode CONNECTION_MODE;

	static {
		WARM_UP_INTERVAL = BenchProperties.getLoader().getPropertyAsLong(
				BenchmarkerParameters.class.getName() + ".WARM_UP_INTERVAL", 60000);

		BENCHMARK_INTERVAL = BenchProperties.getLoader().getPropertyAsLong(
				BenchmarkerParameters.class.getName() + ".BENCHMARK_INTERVAL",
				60000);

		NUM_RTES = BenchProperties.getLoader().getPropertyAsInteger(
				BenchmarkerParameters.class.getName() + ".NUM_RTES", 1);
		
		SERVER_IP = BenchProperties.getLoader().getPropertyAsString(
				BenchmarkerParameters.class.getName() + ".SERVER_IP", "127.0.0.1");
		
		READ_WRITE_TX_RATE = BenchProperties.getLoader().getPropertyAsDouble(
				BenchmarkerParameters.class.getName() + ".READ_WRITE_TX_RATE", 0.3);
		
		int conMode = BenchProperties.getLoader().getPropertyAsInteger(
				BenchmarkerParameters.class.getName() + ".CONNECTION_MODE", 1);
		switch (conMode) {
		case 1:
			CONNECTION_MODE = ConnectionMode.JDBC;
			break;
		case 2:
			CONNECTION_MODE = ConnectionMode.SP;
			break;
		default:
			throw new IllegalArgumentException("The connection mode should be 1 (JDBC) or 2 (SP)");
		}
	}
}