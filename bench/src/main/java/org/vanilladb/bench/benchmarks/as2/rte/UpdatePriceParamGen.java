package org.vanilladb.bench.benchmarks.as2.rte;

import java.util.LinkedList;

import org.vanilladb.bench.benchmarks.as2.As2BenchConstants;
import org.vanilladb.bench.benchmarks.as2.As2BenchTxnType;
import org.vanilladb.bench.rte.TxParamGenerator;
import org.vanilladb.bench.util.RandomValueGenerator;

public class UpdatePriceParamGen implements TxParamGenerator<As2BenchTxnType> {
	
	private static final int READ_COUNT = 10;
	
	@Override
	public As2BenchTxnType getTxnType() {
		return As2BenchTxnType.READ_ITEM;
	}

	@Override
	public Object[] generateParameter() {
		RandomValueGenerator rvg = new RandomValueGenerator();
		LinkedList<Object> paramList = new LinkedList<Object>();
		
		paramList.add(READ_COUNT);
		for (int i = 0; i < READ_COUNT; i++)
			paramList.add(rvg.number(1, As2BenchConstants.NUM_ITEMS));
		
		for (int i = 0; i < READ_COUNT; i++)
			paramList.add(rvg.number(0, 5));

		return paramList.toArray();
	}
}
