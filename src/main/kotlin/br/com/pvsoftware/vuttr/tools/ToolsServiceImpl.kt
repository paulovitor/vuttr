package br.com.pvsoftware.vuttr.tools

import br.com.pvsoftware.model.Tool
import br.com.pvsoftware.model.ToolBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.stereotype.Service

@Service
class ToolsServiceImpl(private val repository: ToolsRepository, private val converter: ToolsConverter) : ToolsService {

    override suspend fun create(toolBody: ToolBody): Tool? = repository.save(converter.convertToEntity(toolBody))
            .awaitFirst()
            .let(converter::convertToDto)

    override fun findAll(limit: Int?, tag: String?): Flow<Tool> = repository.findAll()
            .asFlow()
            .map(converter::convertToDto)
}