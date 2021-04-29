package br.com.pvsoftware.vuttr.tools

import br.com.pvsoftware.model.Tool
import br.com.pvsoftware.model.ToolBody
import kotlinx.coroutines.flow.Flow
import reactor.core.publisher.Mono

interface ToolsService {

    suspend fun create(toolBody: ToolBody): Tool?

    suspend fun delete(id: String): Boolean

    fun findAll(limit: Int?, tag: String?): Flow<Tool>

    suspend fun findById(id: String): Tool?
}
