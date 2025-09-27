package com.singularity.core

/**
 * Core functionality for the Singularity project
 */
object CoreLibrary {
  
  /**
   * A simple greeting function
   */
  def greet(name: String): String = {
    s"Hello, $name! Welcome to Singularity."
  }
  
  /**
   * A basic mathematical operation
   */
  def add(a: Int, b: Int): Int = a + b
  
  /**
   * A function that demonstrates Scala 3 features
   */
  def processItems[T](items: List[T])(using processor: T => String): List[String] = {
    items.map(processor)
  }
}
