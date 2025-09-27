package com.singularity.core

import munit.FunSuite

class CoreLibraryTest extends FunSuite {
  
  test("greet should return a proper greeting") {
    val result = CoreLibrary.greet("World")
    assert(result == "Hello, World! Welcome to Singularity.")
  }
  
  test("add should correctly add two numbers") {
    assert(CoreLibrary.add(2, 3) == 5)
    assert(CoreLibrary.add(-1, 1) == 0)
  }
  
  test("processItems should process a list of items") {
    val processor: Int => String = _.toString
    val items = List(1, 2, 3)
    val result = CoreLibrary.processItems(items)(using processor)
    assert(result == List("1", "2", "3"))
  }
}
