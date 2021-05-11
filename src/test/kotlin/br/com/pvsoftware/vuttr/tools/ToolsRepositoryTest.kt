package br.com.pvsoftware.vuttr.tools

import br.com.pvsoftware.vuttr.TestHelper.Companion.getToolEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

@ExperimentalCoroutinesApi
@DataMongoTest
class ToolsRepositoryTest(@Autowired private val repository: ToolsRepository) {

    private val testDispatcher = TestCoroutineDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        repository.save(getToolEntity())

        val toolEntity = getToolEntity()
        toolEntity.tags = listOf("organization", "webapps", "domain")
        repository.save(toolEntity)
    }

    @AfterEach
    fun cleanUp() {
        repository.deleteAll()

        Dispatchers.resetMain()

        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun findAllByTagsInWithUnpagedShouldReturnFluxOfToolEntity() {
        val tools = repository.findAllByTagsIn("webapps", Pageable.unpaged())

        Assertions.assertNotNull(tools)

        runBlocking {
            withContext(Dispatchers.Main) {
//                FIXME
//                https://github.com/Kotlin/kotlinx.coroutines/issues/1222
//                tools.count().awaitFirstOrNull()?.let { Assertions.assertEquals(1L, it) }
                tools.awaitFirstOrNull()?.let { entity ->

                    Assertions.assertNotNull(entity)
                    entity.tags?.let { Assertions.assertTrue(it.contains("webapps")) }
                }
            }
        }
    }

    @Test
    fun findAllByTagsInWithPageableShouldReturnFluxOfToolEntity() {
        val tools = repository.findAllByTagsIn("organization", PageRequest.of(0, 1))

        Assertions.assertNotNull(tools)

        runBlocking {
            withContext(Dispatchers.Main) {
//                FIXME
//                https://github.com/Kotlin/kotlinx.coroutines/issues/1222
//                tools.count().awaitFirstOrNull()?.let { Assertions.assertEquals(2L, it) }
                tools.awaitFirstOrNull()?.let { entity ->

                    Assertions.assertNotNull(entity)
                    entity.tags?.let { Assertions.assertTrue(it.contains("organization")) }
                }
            }
        }
    }

    @Test
    fun findByIdNotNullWithUnpagedShouldReturnFluxOfToolEntity() {
        val tools = repository.findByIdNotNull(Pageable.unpaged())

        Assertions.assertNotNull(tools)
        runBlocking {
            withContext(Dispatchers.Main) {
//                FIXME
//                https://github.com/Kotlin/kotlinx.coroutines/issues/1222
//                tools.count().awaitFirstOrNull()?.let { Assertions.assertEquals(1L, it) }
                tools.awaitFirstOrNull()?.let { entity ->

                    Assertions.assertNotNull(entity)
                    entity.tags?.let { Assertions.assertTrue(it.contains("webapps")) }
                }
            }
        }
    }
}
