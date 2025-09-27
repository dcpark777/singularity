package com.singularity.analytics

import munit.FunSuite

class AnalyticsEngineTest extends FunSuite {
  
  test("processData should add prefix and process numbers") {
    val data = List(1, 2, 3, 4, 5)
    val result = AnalyticsEngine.processData(data)
    assert(result.length == 5)
    // Check that each result contains the processed value (original + 100)
    assert(result(0).contains("101")) // 1 + 100 = 101
    assert(result(1).contains("102")) // 2 + 100 = 102
    assert(result(2).contains("103")) // 3 + 100 = 103
  }
  
  test("analyzeTrends should calculate statistics correctly") {
    val data = List(1.0, 2.0, 3.0, 4.0, 5.0)
    val result = AnalyticsEngine.analyzeTrends(data)
    
    assert(result("mean") == 3.0)
    assert(result("max") == 5.0)
    assert(result("min") == 1.0)
  }
  
  test("analyzeTrends should handle empty data") {
    val result = AnalyticsEngine.analyzeTrends(List.empty)
    assert(result("mean") == 0.0)
    assert(result("max") == 0.0)
    assert(result("min") == 0.0)
  }
}
