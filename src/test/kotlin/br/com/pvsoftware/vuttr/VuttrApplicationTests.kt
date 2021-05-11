package br.com.pvsoftware.vuttr

import br.com.pvsoftware.vuttr.config.TestConfig
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@Import(TestConfig::class)
@SpringBootTest
class VuttrApplicationTests {

    @Test
    fun contextLoads() {
    }
}
