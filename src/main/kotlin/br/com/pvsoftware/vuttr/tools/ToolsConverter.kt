package br.com.pvsoftware.vuttr.tools

import br.com.pvsoftware.model.Tool
import br.com.pvsoftware.model.ToolBody
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface ToolsConverter {

    fun convertToEntity(toolBody: ToolBody): ToolEntity

    fun convertToDto(entity: ToolEntity?): Tool
}
