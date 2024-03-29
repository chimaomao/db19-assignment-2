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
package org.vanilladb.bench.server.procedure.as2;

import org.vanilladb.bench.server.param.as2.UpdatePriceProcParamHelper;
import org.vanilladb.bench.server.procedure.BasicStoredProcedure;
import org.vanilladb.bench.benchmarks.as2.As2BenchConstants;
import org.vanilladb.core.query.algebra.Plan;
import org.vanilladb.core.query.algebra.Scan;
import org.vanilladb.core.server.VanillaDb;

public class UpdatePriceProc extends BasicStoredProcedure<UpdatePriceProcParamHelper> {

	public UpdatePriceProc() {
		super(new UpdatePriceProcParamHelper());
	}

	@Override
	protected void executeSql() {
		for (int idx = 0; idx < paramHelper.getReadCount(); idx++) {
			int iid = paramHelper.getReadItemId(idx);
			Plan p = VanillaDb.newPlanner().createQueryPlan(
					"SELECT i_name, i_price FROM item WHERE i_id = " + iid, tx);
			Scan s = p.open();
			s.beforeFirst();
			if (s.next()) {
				String name = (String) s.getVal("i_name").asJavaVal();
				double price = (Double) s.getVal("i_price").asJavaVal();
				
				String updateSql;
				if(price > As2BenchConstants.MAX_PRICE){
					updateSql = "UPDATE item SET i_price = " + As2BenchConstants.MIN_PRICE  + " WHERE i_id = " + paramHelper.getReadItemId(idx); 
					if(VanillaDb.newPlanner().executeUpdate(updateSql, tx) > 0)
						paramHelper.setItemPrice(As2BenchConstants.MAX_PRICE, idx);
				}else {
					updateSql = "UPDATE item SET i_price = ADD(i_price," + paramHelper.getAddVal(idx) + " ) WHERE i_id = " + paramHelper.getReadItemId(idx) ;
					if(VanillaDb.newPlanner().executeUpdate(updateSql, tx) > 0)
						paramHelper.setItemPrice(price+paramHelper.getAddVal(idx), idx);
				}
				
				paramHelper.setItemName(name, idx);
				
			} else
				throw new RuntimeException("Cloud not find item record with i_id = " + iid);

			s.close();
		}
	}
}