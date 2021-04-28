package br.com.pvsoftware.vuttr.tools

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface ToolsRepository : ReactiveCrudRepository<ToolEntity, Integer>
