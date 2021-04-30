package br.com.pvsoftware.vuttr.tools

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(value = "tools")
data class ToolEntity(
        @Id var id: String?,
        var title: String?,
        var link: String?,
        var description: String?,
        var tags: List<String>?
)
