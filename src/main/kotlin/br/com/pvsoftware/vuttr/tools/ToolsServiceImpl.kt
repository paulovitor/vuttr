package br.com.pvsoftware.vuttr.tools

import br.com.pvsoftware.model.Tool
import br.com.pvsoftware.model.ToolBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ToolsServiceImpl(private val repository: ToolsRepository, private val converter: ToolsConverter) : ToolsService {

    override suspend fun create(toolBody: ToolBody): Tool? = repository.save(converter.convertToEntity(toolBody))
            .awaitFirst()
            .let(converter::convertToDto)

    override suspend fun delete(id: String): Boolean = repository.findById(id)
            .awaitFirstOrNull()
            .let {
                if (it == null) false
                else repository.delete(it).awaitFirstOrNull().let { true }
            }

    override fun findAll(limit: Int?, tag: String?): Flow<Tool> {
        val pageable = if (limit == null) Pageable.unpaged() else PageRequest.of(0, limit)
        val result = if (tag == null) repository.findByIdNotNull(pageable) else repository.findAllByTagsIn(tag, pageable)
        return result.asFlow().map(converter::convertToDto)
    }

    override suspend fun findById(id: String): Tool? = repository.findById(id)
            .awaitFirstOrNull()
            .let(converter::convertToDto)
}