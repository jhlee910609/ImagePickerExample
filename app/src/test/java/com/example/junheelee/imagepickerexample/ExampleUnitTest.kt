package com.example.junheelee.imagepickerexample

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    lateinit var calculator: Calculator
    val a = 30
    val b = 15

    @Before
    fun init(){
        calculator = Calculator(a, b)
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(45, calculator.add())
    }

    @Test
    fun minus_isCorrect(){
        assertEquals(15, calculator.minus())
    }

    @Test
    fun divide_isCorrect(){
        assertEquals(2, calculator.divide())
    }
}
