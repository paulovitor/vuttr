package br.com.pvsoftware.vuttr.tools

import br.com.pvsoftware.model.Tool
import br.com.pvsoftware.model.ToolBody
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface ToolsConverter {

    @Mapping(target = "id", ignore = true)
    fun convertToEntity(toolBody: ToolBody): ToolEntity

    fun convertToDto(entity: ToolEntity?): Tool
}
