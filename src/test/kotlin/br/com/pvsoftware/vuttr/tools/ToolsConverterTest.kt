package br.com.pvsoftware.vuttr.tools

import br.com.pvsoftware.vuttr.tools.TestHelper.Companion.getToolBody
import br.com.pvsoftware.vuttr.tools.TestHelper.Companion.getToolEntity
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ToolsConverterTest {

    @Autowired
    private lateinit var converter: ToolsConverter

    @Test
    fun convertToEntityShouldReturnEntity() {
        val toolBody = getToolBody()
        val entity = converter.convertToEntity(toolBody)

        Assertions.assertNotNull(entity)
        Assertions.assertNull(entity.id)
        Assertions.assertEquals(toolBody.title, entity.title)
        Assertions.assertEquals(toolBody.link, entity.link)
        Assertions.assertEquals(toolBody.description, entity.description)
        Assertions.assertEquals(toolBody.tags, entity.tags)
    }

    @Test
    fun convertToDtoShouldReturnDto() {
        val entity = getToolEntity()
        val dto = converter.convertToDto(entity)

        Assertions.assertNotNull(entity)
        entity.run {
            Assertions.assertEquals(id, dto.id)
            Assertions.assertEquals(title, dto.title)
            Assertions.assertEquals(link, dto.link)
            Assertions.assertEquals(description, dto.description)
            Assertions.assertEquals(tags, dto.tags)
        }
    }
}
