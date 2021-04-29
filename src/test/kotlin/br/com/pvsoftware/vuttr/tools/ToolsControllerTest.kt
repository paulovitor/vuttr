package br.com.pvsoftware.vuttr.tools

import br.com.pvsoftware.vuttr.config.ValidationConfig
import br.com.pvsoftware.vuttr.tools.TestHelper.Companion.URI
import br.com.pvsoftware.vuttr.tools.TestHelper.Companion.any
import br.com.pvsoftware.vuttr.tools.TestHelper.Companion.getTool
import br.com.pvsoftware.vuttr.tools.TestHelper.Companion.getToolBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
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

    private val testDispatcher = TestCoroutineDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        MockitoAnnotations.openMocks(this)
    }

    @AfterEach
    fun cleanUp() {
        Dispatchers.resetMain()

        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun createToolShouldReturnCreated() = testDispatcher.runBlockingTest {
        val toolBody = getToolBody()

        Mockito.`when`(service.create(any())).thenReturn(getTool())

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
    fun deleteToolByIdSholdReturnNoContent() = testDispatcher.runBlockingTest {
        Mockito.`when`(service.delete(Mockito.anyString())).thenReturn(true)

        client.delete()
                .uri("$URI/{id}", 1)
                .exchange()
                .expectStatus()
                .isNoContent
    }

    @Test
    fun deleteToolByIdSholdReturnNotFound() = testDispatcher.runBlockingTest {
        Mockito.`when`(service.delete(Mockito.anyString())).thenReturn(false)

        client.delete()
                .uri("$URI/{id}", 1)
                .exchange()
                .expectStatus()
                .isNotFound
    }

    @Test
    fun showToolByIdShouldReturnOk() = testDispatcher.runBlockingTest {
        Mockito.`when`(service.findById(Mockito.anyString())).thenReturn(getTool())

        client.get()
                .uri("$URI/{id}", 1)
                .exchange()
                .expectStatus()
                .is2xxSuccessful
    }

    @Test
    fun showToolByIdShouldReturnNotFound() = testDispatcher.runBlockingTest {
        Mockito.`when`(service.findById(Mockito.anyString())).thenReturn(null)

        client.get()
                .uri("$URI/{id}", 1)
                .exchange()
                .expectStatus()
                .isNotFound
    }
}
