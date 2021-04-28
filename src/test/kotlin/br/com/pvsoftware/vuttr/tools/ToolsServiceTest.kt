package br.com.pvsoftware.vuttr.tools

import br.com.pvsoftware.model.ToolBody
import br.com.pvsoftware.vuttr.tools.TestHelper.Companion.getTool
import br.com.pvsoftware.vuttr.tools.TestHelper.Companion.getToolBody
import br.com.pvsoftware.vuttr.tools.TestHelper.Companion.getToolEntity
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@SpringBootTest
class ToolsServiceTest {

    @Autowired
    private lateinit var service: ToolsService

    @MockBean
    private lateinit var repository: ToolsRepository

    @MockBean
    private lateinit var converter: ToolsConverter

    // FIXME: Remove supend
    @Test
    suspend fun createShouldReturnTool() {
        Mockito.`when`(converter.convertToEntity(Mockito.any(ToolBody::class.java))).thenReturn(getToolEntity())

        Mockito.`when`(repository.save(Mockito.any(ToolEntity::class.java))).thenReturn(Mono.just(getToolEntity()!!))

        val tool = service.create(getToolBody())

        Assertions.assertNotNull(tool)
    }

    @Test
    fun findAllShouldReturnFlowOfTool() {
        Mockito.`when`(repository.findAll()).thenReturn(Flux.just(getToolEntity()))

        Mockito.`when`(converter.convertToDto(Mockito.any(ToolEntity::class.java))).thenReturn(getTool())

        val tools = service.findAll(1, "")

        Assertions.assertNotNull(tools)
    }
}