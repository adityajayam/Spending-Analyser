package com.example.demo

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val matchString =
            "INR 699.00 spent on ICICI Bank Card XX7012 on 25-May-24 at LIFE STYLE INTE. Avl Lmt: INR 95,027.04. To dispute,call 18002662/SMS BLOCK 7012 to 9215676766"
        val notMatchString =
            "THB 507.98 spent using ICICI Bank Card XX7012 on 16-Oct-24 on WWW ABHIBUS COM. Avl Limit: INR 2,09,249.25. If not you, call 1800 2662/SMS BLOCK 7012 to 9215676766."

        val d = Utils.parseTransactionDetailsUsingRegex("1", notMatchString)
        assertNull(d)
    }
}