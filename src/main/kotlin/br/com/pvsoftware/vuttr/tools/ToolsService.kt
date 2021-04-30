package br.com.pvsoftware.vuttr.tools

import reactor.core.publisher.Flux

interface ToolsService {

    suspend fun create(toolEntity: ToolEntity): ToolEntity?

    suspend fun delete(id: String): Boolean

    fun findAll(tag: String?, limit: Int?): Flux<ToolEntity>

    suspend fun findById(id: String): ToolEntity?
}
