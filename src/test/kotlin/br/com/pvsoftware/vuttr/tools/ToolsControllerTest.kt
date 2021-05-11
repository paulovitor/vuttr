package br.com.pvsoftware.vuttr.tools

import br.com.pvsoftware.vuttr.TestHelper.Companion.URI
import br.com.pvsoftware.vuttr.TestHelper.Companion.any
import br.com.pvsoftware.vuttr.TestHelper.Companion.getTool
import br.com.pvsoftware.vuttr.TestHelper.Companion.getToolBody
import br.com.pvsoftware.vuttr.TestHelper.Companion.getToolEntity
import br.com.pvsoftware.vuttr.config.TestConfig
import br.com.pvsoftware.vuttr.config.ValidationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux

@ActiveProfiles("test")
@Import(ValidationConfig::class, TestConfig::class)
@ExperimentalCoroutinesApi
@WebFluxTest(controllers = [ToolsController::class])
class ToolsControllerTest(@Autowired private val client: WebTestClient) {

    @MockBean
    private lateinit var service: ToolsService

    @MockBean
    private lateinit var converter: ToolsConverter

    private val testDispatcher = TestCoroutineDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun cleanUp() {
        Dispatchers.resetMain()

        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    @WithMockUser(username = "admin", authorities = ["write:tools"])
    fun createToolShouldReturnCreated() = testDispatcher.runBlockingTest {
        val toolEntity = getToolEntity()
        Mockito.`when`(converter.convertToEntity(any())).thenReturn(toolEntity)

        Mockito.`when`(service.create(any())).thenReturn(toolEntity)

        Mockito.`when`(converter.convertToDto(any())).thenReturn(getTool())

        client.mutateWith(csrf())
            .mutateWith(mockJwt())
            .post()
            .uri(URI)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(getToolBody())
            .exchange()
            .expectStatus().isCreated
    }

    @Test
    @WithMockUser
    fun listToolsShouldReturnOk() {
        Mockito.`when`(service.findAll(any(), any())).thenReturn(Flux.just(getToolEntity()))

        Mockito.`when`(converter.convertToDto(any())).thenReturn(getTool())

        client.get()
            .uri(URI)
            .exchange()
            .expectStatus()
            .is2xxSuccessful
    }

    @Test
    @WithMockUser(username = "admin", authorities = ["write:tools"])
    fun deleteToolByIdSholdReturnNoContent() = testDispatcher.runBlockingTest {
        Mockito.`when`(service.delete(Mockito.anyString())).thenReturn(true)

        client.mutateWith(csrf())
            .mutateWith(mockJwt())
            .delete()
            .uri("$URI/{id}", 1)
            .exchange()
            .expectStatus()
            .isNoContent
    }

    @Test
    @WithMockUser(username = "admin", authorities = ["write:tools"])
    fun deleteToolByIdSholdReturnNotFound() = testDispatcher.runBlockingTest {
        Mockito.`when`(service.delete(Mockito.anyString())).thenReturn(false)

        client.mutateWith(csrf())
            .mutateWith(mockJwt())
            .delete()
            .uri("$URI/{id}", 1)
            .exchange()
            .expectStatus()
            .isNotFound
    }

    @Test
    @WithMockUser
    fun showToolByIdShouldReturnOk() = testDispatcher.runBlockingTest {
        Mockito.`when`(service.findById(Mockito.anyString())).thenReturn(getToolEntity())

        Mockito.`when`(converter.convertToDto(any())).thenReturn(getTool())

        client.get()
            .uri("$URI/{id}", 1)
            .exchange()
            .expectStatus()
            .is2xxSuccessful
    }

    @Test
    @WithMockUser
    fun showToolByIdShouldReturnNotFound() = testDispatcher.runBlockingTest {
        Mockito.`when`(service.findById(Mockito.anyString())).thenReturn(null)

        client.get()
            .uri("$URI/{id}", 1)
            .exchange()
            .expectStatus()
            .isNotFound
    }
}
