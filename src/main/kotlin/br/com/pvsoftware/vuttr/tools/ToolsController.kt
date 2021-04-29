package br.com.pvsoftware.vuttr.tools

import br.com.pvsoftware.api.ToolsApi
import br.com.pvsoftware.model.Tool
import br.com.pvsoftware.model.ToolBody
import kotlinx.coroutines.flow.Flow
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class ToolsController(private val service: ToolsService) : ToolsApi {

    override suspend fun createTool(toolBody: ToolBody): ResponseEntity<Tool> =
            service.create(toolBody)
                    .let { ResponseEntity.created(URI.create("/tools/${it?.id}")).body(it) }

    override fun listTools(limit: Int?, tag: String?): ResponseEntity<Flow<Tool>> =
            ResponseEntity.ok(service.findAll(limit, tag))

    override suspend fun deleteToolById(id: String): ResponseEntity<Unit> =
            if (service.delete(id)) ResponseEntity.noContent().build()
            else ResponseEntity.notFound().build()

    override suspend fun showToolById(id: String): ResponseEntity<Tool> =
            service.findById(id)
                    .let {
                        if (it == null) ResponseEntity.notFound().build()
                        else ResponseEntity.ok(it)
                    }
}