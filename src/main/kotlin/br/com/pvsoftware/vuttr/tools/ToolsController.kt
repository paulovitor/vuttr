package br.com.pvsoftware.vuttr.tools

import br.com.pvsoftware.api.ToolsApi
import br.com.pvsoftware.model.Tool
import br.com.pvsoftware.model.ToolBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class ToolsController(private val service: ToolsService, private val converter: ToolsConverter) : ToolsApi {

    @PreAuthorize("hasAuthority('write:tools')")
    override suspend fun createTool(toolBody: ToolBody): ResponseEntity<Tool> =
        service.create(converter.convertToEntity(toolBody))
            .let {
                ResponseEntity.created(URI.create("/tools/${it?.id}"))
                    .body(converter.convertToDto(it))
            }

    @PreAuthorize("hasAuthority('read:tools')")
    override fun listTools(limit: Int?, tag: String?): ResponseEntity<Flow<Tool>> =
        ResponseEntity.ok(service.findAll(tag, limit).asFlow().map(converter::convertToDto))

    @PreAuthorize("hasAuthority('write:tools')")
    override suspend fun deleteToolById(id: String): ResponseEntity<Unit> =
        if (service.delete(id)) ResponseEntity.noContent().build()
        else ResponseEntity.notFound().build()

    @PreAuthorize("hasAuthority('read:tools')")
    override suspend fun showToolById(id: String): ResponseEntity<Tool> =
        service.findById(id)
            .let {
                if (it == null) ResponseEntity.notFound().build()
                else ResponseEntity.ok(converter.convertToDto(it))
            }
}
