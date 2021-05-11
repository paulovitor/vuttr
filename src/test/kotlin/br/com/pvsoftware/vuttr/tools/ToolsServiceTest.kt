package br.com.pvsoftware.vuttr.tools

import br.com.pvsoftware.vuttr.TestHelper.Companion.any
import br.com.pvsoftware.vuttr.TestHelper.Companion.getToolEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@ContextConfiguration(classes = [ToolsServiceImpl::class])
@ExperimentalCoroutinesApi
@SpringBootTest
class ToolsServiceTest(@Autowired private val service: ToolsService) {

    @MockBean
    private lateinit var repository: ToolsRepository

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
    fun createShouldReturnTool() = testDispatcher.runBlockingTest {
        val toolEntity = getToolEntity()

        Mockito.`when`(repository.save(any())).thenReturn(Mono.just(toolEntity))

        val tool = service.create(toolEntity)

        Assertions.assertNotNull(tool)
    }

    @Test
    fun deleteShouldReturnTrue() = testDispatcher.runBlockingTest {
        Mockito.`when`(repository.findById(Mockito.anyString())).thenReturn(Mono.just(getToolEntity()))

        Mockito.`when`(repository.delete(any())).thenReturn(Mono.empty())

        val deleted = service.delete("abc")

        Assertions.assertTrue(deleted)
    }

    @Test
    fun deleteShouldReturnFalse() = testDispatcher.runBlockingTest {
        Mockito.`when`(repository.findById(Mockito.anyString())).thenReturn(Mono.empty())

        val deleted = service.delete("abc")

        Assertions.assertFalse(deleted)
    }

    @Test
    fun findAllWithEmptyLimitShouldReturnFlowOfTool() = testDispatcher.runBlockingTest {
        Mockito.`when`(repository.findAllByTagsIn(Mockito.anyString(), any())).thenReturn(Flux.just(getToolEntity()))

        val tools = service.findAll("organization", null)

        Assertions.assertNotNull(tools)
        tools.awaitFirstOrNull()?.let { Assertions.assertNotNull(it) }
    }

    @Test
    fun findAllWithEmptyTagShouldReturnFlowOfTool() = testDispatcher.runBlockingTest {
        Mockito.`when`(repository.findByIdNotNull(any())).thenReturn(Flux.just(getToolEntity()))

        val tools = service.findAll(null, 10)

        Assertions.assertNotNull(tools)
        tools.awaitFirstOrNull()?.let { Assertions.assertNotNull(it) }
    }

    @Test
    fun findAllShouldReturnFlowOfTool() = testDispatcher.runBlockingTest {
        Mockito.`when`(repository.findAllByTagsIn(Mockito.anyString(), any())).thenReturn(Flux.just(getToolEntity()))

        val tools = service.findAll("organization", 10)

        Assertions.assertNotNull(tools)
        tools.awaitFirstOrNull()?.let { Assertions.assertNotNull(it) }
    }

    @Test
    fun findByIdShouldReturnTool() = testDispatcher.runBlockingTest {
        Mockito.`when`(repository.findById(Mockito.anyString())).thenReturn(Mono.just(getToolEntity()))

        val tool = service.findById("abc")

        Assertions.assertNotNull(tool)
    }
}
