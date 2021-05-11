package io.smooth.use_cases

import io.mockk.*
import io.mockk.junit5.MockKExtension
import io.smooth.constraint.BaseConstraint
import io.smooth.use_cases.callback.CallbackWork
import io.smooth.use_cases.work.WorkStatus
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import strikt.api.expect
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
internal class UseCaseTest {

    lateinit var myCallback: CallbackWork.WorkCallback<Int>

    lateinit var useCaseImpl: UseCaseImpl
    lateinit var work: CallbackWork<Int?, Int>

    @BeforeEach
    fun setup() {
        useCaseImpl = spyk()
        myCallback = spyk()

        work = useCaseImpl.getWork().apply {
            callback = myCallback
        }
    }


    @DisplayName("Given any value")
    @Nested
    inner class AnyValue {

        @Test
        internal fun `When executed Twice, should throw exception on the second execute`() {

            expect {
                assertDoesNotThrow {
                    work.execute(null) {
                        callback = myCallback
                    }
                }
            }

            expectThat(work.workStatus)
                .isEqualTo(WorkStatus.Completed)

            expectThrows<IllegalArgumentException> {
                work.execute(null)
            }
        }


        @Test
        internal fun `When cancelled, it shouldn't execute again`() {
            work.execute(null)
            expectThrows<IllegalArgumentException> {
                work.cancel()
            }
            expectThrows<IllegalArgumentException> {
                work.execute(null) {
                    callback = myCallback
                }
            }
        }

        @Test
        internal fun `When executed successfully, should call executing then success`() {
            work.execute(1) {
                callback = myCallback
            }
            verifySequence {
                myCallback.onExecuting()
                myCallback.onResult(1)
                myCallback.onCompleted()
            }
        }


        @Test
        internal fun `When error in execution, should call executing then error`() {
            work.execute(null) {
                callback = myCallback
            }
            verifySequence {
                myCallback.onExecuting()
                myCallback.onFailed(any())
                myCallback.onCompleted()
            }
        }

        @Test
        internal fun `When calling cancel before completion or execution finishes, should cancel and not execute`() {
            work.cancel()
            expectThrows<IllegalArgumentException> {
                work.execute(1)
            }
            verifySequence {
                myCallback.onCancelled(any())
            }
        }

        @Test
        internal fun `When calling cancel after completion, should cancel`() {
            work.execute(1) {
                callback = myCallback
            }
            expectThrows<IllegalArgumentException> {
                work.cancel()
            }
            verifySequence {
                myCallback.onExecuting()
                myCallback.onResult(any())
                myCallback.onCompleted()
            }
        }

        @Test
        internal fun `When calling execute after completion, should throw error`() {
            work.execute(1) {
                callback = myCallback
            }
            verifySequence {
                myCallback.onExecuting()
                myCallback.onResult(any())
                myCallback.onCompleted()
            }
            expectThrows<IllegalArgumentException> {
                work.execute(1)
            }
        }

    }

    @DisplayName("Given constraints")
    @Nested
    inner class Constraints {

        @Test
        internal fun `When constraints are not met, shouldn't execute`() {
            val alwaysFalseConstraint = spyk<BaseConstraint>()
            every { alwaysFalseConstraint.check() } returns false

            work.execute(null, alwaysFalseConstraint) {
                callback = myCallback
            }

            verify {
                myCallback.onConstraintsNotMet(any())
            }
        }

        @Test
        internal fun `When failing and success constraints, shouldn't execute`() {
            val alwaysFalseConstraint = spyk<BaseConstraint>()
            every { alwaysFalseConstraint.check() } returns false
            val alwaysTrueConstraint = spyk<BaseConstraint>()
            every { alwaysTrueConstraint.check() } returns true

            work.execute(null, alwaysFalseConstraint, alwaysTrueConstraint) {
                callback = myCallback
            }

            verify {
                myCallback.onConstraintsNotMet(
                    arrayListOf(alwaysFalseConstraint)
                )
            }
        }

        @Test
        internal fun `When success constraints, should call executing and execute normally`() {
            val alwaysTrueConstraint = spyk<BaseConstraint>()
            every { alwaysTrueConstraint.check() } returns true

            work.execute(1, alwaysTrueConstraint) {
                callback = myCallback
            }

            verifySequence {
                myCallback.onExecuting()
                myCallback.onResult(any())
                myCallback.onCompleted()
            }
        }


    }


}