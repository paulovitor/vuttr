package br.com.pvsoftware.vuttr.tools

import br.com.pvsoftware.model.Tool
import br.com.pvsoftware.model.ToolBody
import kotlinx.coroutines.flow.Flow

interface ToolsService {

    suspend fun create(toolBody: ToolBody): Tool?

    fun findAll(limit: Int?, tag: String?): Flow<Tool>
}
