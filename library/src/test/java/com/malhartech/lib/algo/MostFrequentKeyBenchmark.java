/**
 * Copyright (c) 2012-2012 Malhar, Inc. All rights reserved.
 */
package com.malhartech.lib.algo;

import com.malhartech.api.Context.OperatorContext;
import com.malhartech.dag.TestCountAndLastTupleSink;
import com.malhartech.dag.TestSink;
import java.util.ArrayList;
import java.util.HashMap;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Performance tests for {@link com.malhartech.lib.algo.MostFrequentKey}<p>
 *
 */
public class MostFrequentKeyBenchmark
{
  private static Logger log = LoggerFactory.getLogger(MostFrequentKeyBenchmark.class);

  /**
   * Test node logic emits correct results
   */
  @Test
  @SuppressWarnings("SleepWhileInLoop")
  @Category(com.malhartech.annotation.PerformanceTestCategory.class)
  public void testNodeProcessing() throws Exception
  {
    MostFrequentKey<String> oper = new MostFrequentKey<String>();
    TestCountAndLastTupleSink matchSink = new TestCountAndLastTupleSink();
    TestCountAndLastTupleSink listSink = new TestCountAndLastTupleSink();
    oper.most.setSink(matchSink);
    oper.list.setSink(listSink);

    oper.beginWindow();
    int numTuples = 10000000;
    int atot = 5*numTuples;
    int btot = 6*numTuples;
    int ctot = 3*numTuples;
    for (int i = 0; i < atot; i++) {
      oper.data.process("a");
    }
    for (int i = 0; i < btot; i++) {
      oper.data.process("b");
    }
    for (int i = 0; i < ctot; i++) {
      oper.data.process("c");
    }
    oper.endWindow();
    Assert.assertEquals("number emitted tuples", 1, matchSink.count);
    HashMap<String, Integer> tuple = (HashMap<String, Integer>) matchSink.tuple;
    Integer val = tuple.get("b");
    Assert.assertEquals("Count of b was ", btot, val.intValue());
    Assert.assertEquals("number emitted tuples", 1, listSink.count);
    ArrayList<HashMap<String,Integer>> list = (ArrayList<HashMap<String,Integer>>) listSink.tuple;
    val = list.get(0).get("b");
    Assert.assertEquals("Count of b was ", btot, val.intValue());
    log.debug(String.format("\nBenchmarked %d tuples", atot+btot+ctot));
  }
}