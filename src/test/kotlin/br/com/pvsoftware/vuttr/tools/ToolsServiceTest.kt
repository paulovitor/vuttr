package br.com.pvsoftware.vuttr.tools

import br.com.pvsoftware.vuttr.tools.TestHelper.Companion.any
import br.com.pvsoftware.vuttr.tools.TestHelper.Companion.getTool
import br.com.pvsoftware.vuttr.tools.TestHelper.Companion.getToolBody
import br.com.pvsoftware.vuttr.tools.TestHelper.Companion.getToolEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.boot.test.context.SpringBootTest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@SpringBootTest
class ToolsServiceTest {

    private lateinit var service: ToolsService

    @Mock
    private lateinit var repository: ToolsRepository

    @Mock
    private lateinit var converter: ToolsConverter

    private val testDispatcher = TestCoroutineDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        MockitoAnnotations.openMocks(this)

        service = ToolsServiceImpl(repository, converter)
    }

    @AfterEach
    fun cleanUp() {
        Dispatchers.resetMain()

        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun createShouldReturnTool() = testDispatcher.runBlockingTest {
        val toolEntity = getToolEntity()

        Mockito.`when`(converter.convertToEntity(any())).thenReturn(toolEntity)

        Mockito.`when`(repository.save(any())).thenReturn(Mono.just(toolEntity))

        Mockito.`when`(converter.convertToDto(any())).thenReturn(getTool())

        val tool = service.create(getToolBody())

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

        Mockito.`when`(converter.convertToDto(any())).thenReturn(getTool())

        val tools = service.findAll(null, "organization")

        Assertions.assertNotNull(tools)
        tools.first()?.let { Assertions.assertNotNull(it) }
    }

    @Test
    fun findAllWithEmptyTagShouldReturnFlowOfTool() = testDispatcher.runBlockingTest {
        Mockito.`when`(repository.findByIdNotNull(any())).thenReturn(Flux.just(getToolEntity()))

        Mockito.`when`(converter.convertToDto(any())).thenReturn(getTool())

        val tools = service.findAll(10, null)

        Assertions.assertNotNull(tools)
        tools.first()?.let { Assertions.assertNotNull(it) }
    }

    @Test
    fun findAllShouldReturnFlowOfTool() = testDispatcher.runBlockingTest {
        Mockito.`when`(repository.findAllByTagsIn(Mockito.anyString(), any())).thenReturn(Flux.just(getToolEntity()))

        Mockito.`when`(converter.convertToDto(any())).thenReturn(getTool())

        val tools = service.findAll(10, "organization")

        Assertions.assertNotNull(tools)
        tools.first()?.let { Assertions.assertNotNull(it) }
    }

    @Test
    fun findByIdShouldReturnTool() = testDispatcher.runBlockingTest {
        Mockito.`when`(repository.findById(Mockito.anyString())).thenReturn(Mono.just(getToolEntity()))

        Mockito.`when`(converter.convertToDto(any())).thenReturn(getTool())

        val tool = service.findById("abc")

        Assertions.assertNotNull(tool)
    }
}