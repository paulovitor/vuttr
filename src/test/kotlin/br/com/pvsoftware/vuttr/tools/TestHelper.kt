package br.com.pvsoftware.vuttr.tools

import br.com.pvsoftware.model.Tool
import br.com.pvsoftware.model.ToolBody

class TestHelper {

    companion object {
        const val URI = "/v1/tools"

        fun getToolBody() = ToolBody("title", "link", "description", emptyList())

        fun getToolEntity(): ToolEntity? = ToolEntity("abc", "title", "link", "description", emptyList())

        fun getTool() = Tool("abc", "title", "link", "description")
    }
}