package com.singularity.examples

import com.singularity.core.CoreLibrary
import com.singularity.utils.Utils

/**
 * Example applications demonstrating the use of core and utils modules
 */
object Examples {
  
  def main(args: Array[String]): Unit = {
    println("=== Singularity Examples ===")
    
    // Core library examples
    println("\n1. Core Library Examples:")
    println(CoreLibrary.greet("Developer"))
    println(s"2 + 3 = ${CoreLibrary.add(2, 3)}")
    
    // Utils examples
    println("\n2. Utils Examples:")
    println(Utils.enhancedGreet("Scala Developer", Some("Senior")))
    println(s"Sum of [1, 2, 3, 4, 5] = ${Utils.sum(List(1, 2, 3, 4, 5))}")
    println(s"Formatted list: ${Utils.formatList(List("Scala", "sbt", "Monorepo"))}")
    
    // Advanced example using Scala 3 features
    println("\n3. Advanced Example:")
    val numbers = List(1, 2, 3, 4, 5)
    val processor: Int => String = n => s"Number: $n"
    val processed = CoreLibrary.processItems(numbers)(using processor)
    println(s"Processed items: ${Utils.formatList(processed)}")
  }
}
