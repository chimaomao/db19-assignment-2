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
package org.vanilladb.bench.benchmarks.as2;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.vanilladb.bench.Benchmarker;
import org.vanilladb.bench.StatisticMgr;
import org.vanilladb.bench.TransactionType;
import org.vanilladb.bench.benchmarks.as2.rte.As2BenchRte;
import org.vanilladb.bench.benchmarks.as2.rte.As2BenchTxExecutor;
import org.vanilladb.bench.benchmarks.as2.rte.TestbedLoaderParamGen;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.SutDriver;
import org.vanilladb.bench.rte.RemoteTerminalEmulator;

public class As2Benchmarker extends Benchmarker {
	
	public As2Benchmarker(SutDriver sutDriver) {
		super(sutDriver, "as2bench");
	}
	
	public As2Benchmarker(SutDriver sutDriver, String reportPostfix) {
		super(sutDriver, "as2bench-" + reportPostfix);
	}
	
	public Set<TransactionType> getBenchmarkingTxTypes() {
		Set<TransactionType> txTypes = new HashSet<TransactionType>();
		for (TransactionType txType : As2BenchTxnType.values()) {
			if (txType.isBenchmarkingTx())
				txTypes.add(txType);
		}
		return txTypes;
	}

	protected void executeLoadingProcedure(SutConnection conn) throws SQLException {
		As2BenchTxExecutor loader = new As2BenchTxExecutor(new TestbedLoaderParamGen());
		loader.execute(conn);
	}
	
	protected RemoteTerminalEmulator<As2BenchTxnType> createRte(SutConnection conn, StatisticMgr statMgr) {
		return new As2BenchRte(conn, statMgr);
	}
}
