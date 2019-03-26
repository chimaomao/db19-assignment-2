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
package org.vanilladb.bench.benchmarks.as2.rte.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import org.vanilladb.bench.benchmarks.as2.As2BenchTxnType;
import org.vanilladb.bench.remote.SutResultSet;
import org.vanilladb.bench.rte.jdbc.JdbcExecutor;


public class As2BenchJdbcExecutor implements JdbcExecutor<As2BenchTxnType> {

	@Override
	public SutResultSet execute(Connection conn, As2BenchTxnType txType, Object[] pars)
			throws SQLException {
//		System.out.println(txType.toString());
		switch (txType) {
		case TESTBED_LOADER:
			return new TestbedLoaderJdbcJob().execute(conn, pars);
		case READ_ITEM:
//			System.out.println("READ_ITEM\n");
			return new ReadItemTxnJdbcJob().execute(conn, pars);
		case UPDATEPRICE:
//			System.out.println("UPDATEPRICE\n");
			return new UpdatePriceJdbcJob().execute(conn, pars);
		default:
			throw new UnsupportedOperationException(
					String.format("no JDCB implementation for '%s'", txType));
		}
	}

}
