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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vanilladb.bench.benchmarks.as2.As2BenchConstants;

import org.vanilladb.bench.remote.SutResultSet;
import org.vanilladb.bench.remote.jdbc.VanillaDbJdbcResultSet;
import org.vanilladb.bench.rte.jdbc.JdbcJob;

public class UpdatePriceJdbcJob implements JdbcJob{
	private static Logger logger = Logger.getLogger(ReadItemTxnJdbcJob.class
			.getName());
	
	@Override
	public SutResultSet execute(Connection conn, Object[] pars) throws SQLException {
		// Parse parameters
		int readCount = (Integer) pars[0];
		int[] itemIds = new int[readCount];
		int[] addValues = new int[readCount];
		
		for (int i = 0; i < readCount; i++)
			itemIds[i] = (Integer) pars[i + 1];
		
		for (int j = 0; j < readCount; j++)
			addValues[j] = (Integer) pars[readCount + j + 1];
			
		// Output message
		StringBuilder outputMsg = new StringBuilder("[");
		
		// Execute logic
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = null;
			for (int i = 0; i < 10; i++) {
				int price;
				String updateSql;
				String sql = "SELECT i_name, i_price FROM item WHERE i_id = " + itemIds[i];
				rs = statement.executeQuery(sql);
				rs.beforeFirst();
				if (rs.next()) {
					price = rs.getInt("i_price");
				} else
					throw new RuntimeException("cannot find the record with i_id = " + itemIds[i]);
				rs.close();
				if (price > As2BenchConstants.MAX_PRICE){
					updateSql = "UPDATE item SET i_price = " + As2BenchConstants.MIN_PRICE  + " WHERE i_id = " + itemIds[i];
				}else{	
					updateSql = "UPDATE item SET i_price = ADD(i_price, " + addValues[i] + " ) WHERE i_id = " + itemIds[i] ;
				}
				int rs2 = statement.executeUpdate(updateSql);
			}
			conn.commit();
			
			outputMsg.append("]");
			
			return new VanillaDbJdbcResultSet(true, outputMsg.toString());
		} catch (Exception e) {
			if (logger.isLoggable(Level.WARNING))
				logger.warning(e.toString());
			return new VanillaDbJdbcResultSet(false, "");
		}
	}
}
