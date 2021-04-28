package br.com.pvsoftware.vuttr.tools

import br.com.pvsoftware.model.ToolBody
import br.com.pvsoftware.vuttr.config.ValidationConfig
import br.com.pvsoftware.vuttr.tools.TestHelper.Companion.URI
import br.com.pvsoftware.vuttr.tools.TestHelper.Companion.getTool
import br.com.pvsoftware.vuttr.tools.TestHelper.Companion.getToolBody
import kotlinx.coroutines.flow.emptyFlow
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@WebFluxTest(controllers = [ToolsController::class])
@Import(ValidationConfig::class)
class ToolsControllerTest {

    @Autowired
    private lateinit var client: WebTestClient

    @MockBean
    private lateinit var service: ToolsService

    // FIXME: Remove supend
    @Test
    suspend fun createToolShouldReturnCreated() {
        val toolBody = getToolBody()

        Mockito.`when`(service.create(Mockito.any(ToolBody::class.java))).thenReturn(getTool())

        client.post()
                .uri(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(toolBody)
                .exchange()
                .expectStatus().isCreated
    }

    @Test
    fun listToolsShouldReturnOk() {
        Mockito.`when`(service.findAll(Mockito.anyInt(), Mockito.anyString())).thenReturn(emptyFlow())

        client.get()
                .uri(URI)
                .exchange()
                .expectStatus()
                .is2xxSuccessful
    }

    @Test
    fun deleteToolByIdSholdReturnNoContent() {
        client.delete()
                .uri("$URI/{id}", 1)
                .exchange()
                .expectStatus()
                .isNoContent
    }

    @Test
    fun showToolByIdShouldReturnOk() {
        client.delete()
                .uri("$URI/{id}", 1)
                .exchange()
                .expectStatus()
                .is2xxSuccessful
    }
}