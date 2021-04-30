package br.com.pvsoftware.vuttr.tools

import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class ToolsServiceImpl(private val repository: ToolsRepository) : ToolsService {

    override suspend fun create(toolEntity: ToolEntity): ToolEntity? = repository.save(toolEntity)
            .awaitFirst()

    override suspend fun delete(id: String): Boolean = repository.findById(id)
            .awaitFirstOrNull()
            .let {
                if (it == null) false
                else repository.delete(it).awaitFirstOrNull().let { true }
            }

    override fun findAll(tag: String?, limit: Int?): Flux<ToolEntity> {
        val pageable = if (limit == null) Pageable.unpaged() else PageRequest.of(0, limit)
        return if (tag == null) repository.findByIdNotNull(pageable)
        else repository.findAllByTagsIn(tag, pageable)
    }

    override suspend fun findById(id: String): ToolEntity? = repository.findById(id)
            .awaitFirstOrNull()
}
