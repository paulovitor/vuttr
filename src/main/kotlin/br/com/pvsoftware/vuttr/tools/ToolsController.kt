package br.com.pvsoftware.vuttr.tools

import br.com.pvsoftware.api.ToolsApi
import br.com.pvsoftware.model.Tool
import br.com.pvsoftware.model.ToolBody
import kotlinx.coroutines.flow.Flow
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class ToolsController(private val toolsService: ToolsService) : ToolsApi {

    override suspend fun createTool(toolBody: ToolBody): ResponseEntity<Tool> {
        return ResponseEntity.created(URI.create("abc")).body(toolsService.create(toolBody))
    }

    override fun listTools(limit: Int?, tag: String?): ResponseEntity<Flow<Tool>> {
        return ResponseEntity.ok(toolsService.findAll(limit, tag))
    }

    override suspend fun deleteToolById(id: String): ResponseEntity<Unit> {
        return super.deleteToolById(id)
    }

    override suspend fun showToolById(id: String): ResponseEntity<Tool> {
        return super.showToolById(id)
    }
}