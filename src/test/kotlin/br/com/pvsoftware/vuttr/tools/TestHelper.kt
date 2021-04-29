package br.com.pvsoftware.vuttr.tools

import br.com.pvsoftware.model.Tool
import br.com.pvsoftware.model.ToolBody
import org.mockito.Mockito

class TestHelper {

    companion object {
        const val URI = "/v1/tools"

        fun <T> any(): T {
            Mockito.any<T>()
            return uninitialized()
        }

        fun <T> uninitialized(): T = null as T

        fun getToolBody() = ToolBody("title", "link", "description", emptyList())

        fun getToolEntity(): ToolEntity = ToolEntity("abc", "title", "link", "description", listOf("organization"))

        fun getTool() = Tool("abc", "title", "link", "description")
    }
}