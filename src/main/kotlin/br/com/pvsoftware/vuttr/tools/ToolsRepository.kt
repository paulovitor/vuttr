package br.com.pvsoftware.vuttr.tools

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface ToolsRepository : ReactiveCrudRepository<ToolEntity, String> {

    fun findAllByTagsIn(tag: String?, page: Pageable): Flux<ToolEntity>

    // Spring data not support findAll(pageable: Pageable)
    fun findByIdNotNull(pageable: Pageable): Flux<ToolEntity>
}
