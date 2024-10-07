package com.unicorn.api.domain.greeting

import org.junit.jupiter.api.Test

class GreetingTest {

    @Test
    fun `should create greeting`() {
        val greeting = Greeting.create("Hello, World!")

        assert(greeting.message == "Hello, World!")
    }

    @Test
    fun `should not create greeting with empty message`() {
        try {
            Greeting.create("")
            assert(false)
        } catch (e: IllegalStateException) {
            assert(e.message == "message is empty")
        }
    }

    @Test
    fun `should change message`() {
        val greeting = Greeting.create("Hello, World!")

        val newGreeting = greeting.changeMessage("Hello, Universe!")

        assert(newGreeting.message == "Hello, Universe!")
    }

    @Test
    fun `should not change message to empty`() {
        val greeting = Greeting.create("Hello, World!")

        try {
            greeting.changeMessage("")
            assert(false)
        } catch (e: IllegalStateException) {
            assert(e.message == "message is empty")
        }
    }
}