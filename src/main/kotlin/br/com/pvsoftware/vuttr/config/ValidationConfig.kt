package br.com.pvsoftware.vuttr.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.StandardReflectionParameterNameDiscoverer
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean

@Configuration
class ValidationConfig {

    companion object {

        @Bean
        @Primary
        fun validator(): LocalValidatorFactoryBean {
            val factoryBean = LocalValidatorFactoryBean()
            factoryBean.setParameterNameDiscoverer(StandardReflectionParameterNameDiscoverer())
            return factoryBean
        }
    }
}
