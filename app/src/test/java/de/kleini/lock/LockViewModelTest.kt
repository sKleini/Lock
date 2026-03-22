package de.kleini.lock

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class LockViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var application: Application

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        application = ApplicationProvider.getApplicationContext()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial shouldExit state is false`() = runTest(testDispatcher) {
        val viewModel = LockViewModel(application)
        advanceUntilIdle()
        assertFalse(viewModel.shouldExit.value)
    }

    @Test
    fun `setShouldExit true updates state to true`() = runTest(testDispatcher) {
        val viewModel = LockViewModel(application)
        advanceUntilIdle()
        viewModel.setShouldExit(true)
        advanceUntilIdle()
        assertTrue(viewModel.shouldExit.value)
    }

    @Test
    fun `setShouldExit false resets state to false`() = runTest(testDispatcher) {
        val viewModel = LockViewModel(application)
        advanceUntilIdle()
        viewModel.setShouldExit(true)
        advanceUntilIdle()
        viewModel.setShouldExit(false)
        advanceUntilIdle()
        assertFalse(viewModel.shouldExit.value)
    }

    // Lifecycle-Sequenz: onCreate setzt shouldExit immer auf false
    @Test
    fun `onCreate lifecycle pattern resets shouldExit to false`() = runTest(testDispatcher) {
        val viewModel = LockViewModel(application)
        advanceUntilIdle()
        viewModel.setShouldExit(true)
        advanceUntilIdle()

        viewModel.setShouldExit(false) // simuliert App.onCreate
        advanceUntilIdle()

        assertFalse(viewModel.shouldExit.value)
    }

    // Lifecycle-Sequenz: onResume setzt shouldExit auf true (normaler Lauf)
    @Test
    fun `onResume normal flow sets shouldExit to true`() = runTest(testDispatcher) {
        val viewModel = LockViewModel(application)
        advanceUntilIdle()

        assertFalse(viewModel.shouldExit.value) // nach onCreate: false
        viewModel.setShouldExit(true)           // simuliert onResume "else"-Zweig
        advanceUntilIdle()

        assertTrue(viewModel.shouldExit.value)
    }

    // Lifecycle-Sequenz: onResume mit shouldExit=true signalisiert Exit
    @Test
    fun `exit condition detected when shouldExit is true on resume`() = runTest(testDispatcher) {
        val viewModel = LockViewModel(application)
        advanceUntilIdle()

        viewModel.setShouldExit(true)
        advanceUntilIdle()

        assertTrue(viewModel.shouldExit.value) // onResume würde Exit auslösen
    }

    // Lifecycle-Sequenz: onRestart verhindert ungewollten Exit beim nächsten onResume
    @Test
    fun `onRestart resets shouldExit so onResume does not trigger exit`() = runTest(testDispatcher) {
        val viewModel = LockViewModel(application)
        advanceUntilIdle()

        viewModel.setShouldExit(true)  // normaler Zustand während Lauf
        advanceUntilIdle()
        viewModel.setShouldExit(false) // simuliert onRestart
        advanceUntilIdle()

        assertFalse(viewModel.shouldExit.value) // kein Exit beim nächsten onResume
    }
}
