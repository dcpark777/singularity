package com.singularity.utils

import munit.FunSuite

class UtilsTest extends FunSuite {
  
  test("enhancedGreet should work with title") {
    val result = Utils.enhancedGreet("Alice", Some("Engineer"))
    assert(result == "Hello, Alice! Welcome to Singularity. You are a Engineer.")
  }
  
  test("enhancedGreet should work without title") {
    val result = Utils.enhancedGreet("Bob")
    assert(result == "Hello, Bob! Welcome to Singularity.")
  }
  
  test("sum should calculate the sum of numbers") {
    assert(Utils.sum(List(1, 2, 3, 4)) == 10)
    assert(Utils.sum(List()) == 0)
  }
  
  test("formatList should format a list properly") {
    assert(Utils.formatList(List(1, 2, 3)) == "[1, 2, 3]")
    assert(Utils.formatList(List("a", "b")) == "[a, b]")
  }
}
