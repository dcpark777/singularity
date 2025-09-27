package com.singularity.utils

import com.singularity.core.CoreLibrary

/**
 * Utility functions that depend on core functionality
 */
object Utils {
  
  /**
   * Enhanced greeting with additional formatting
   */
  def enhancedGreet(name: String, title: Option[String] = None): String = {
    val baseGreeting = CoreLibrary.greet(name)
    title match {
      case Some(t) => s"$baseGreeting You are a $t."
      case None => baseGreeting
    }
  }
  
  /**
   * Calculate the sum of a list of numbers using core add function
   */
  def sum(numbers: List[Int]): Int = {
    numbers.foldLeft(0)(CoreLibrary.add)
  }
  
  /**
   * Format a list of items as a string
   */
  def formatList[T](items: List[T]): String = {
    items.mkString("[", ", ", "]")
  }
}
