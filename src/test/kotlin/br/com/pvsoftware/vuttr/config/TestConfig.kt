package br.com.pvsoftware.vuttr.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder

@TestConfiguration
internal class TestConfig {

    @Bean
    fun jwtDecoder(): NimbusReactiveJwtDecoder {
        return NimbusReactiveJwtDecoder("https://s")
    }
}
