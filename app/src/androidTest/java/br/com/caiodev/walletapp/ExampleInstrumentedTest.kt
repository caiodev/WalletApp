package br.com.caiodev.walletapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    @Throws(Exception::class)
    fun useAppContext() {
        // Context of the app under test
        val appContext =
            androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("br.com.caiodev.walletapp", appContext.packageName)
    }
}