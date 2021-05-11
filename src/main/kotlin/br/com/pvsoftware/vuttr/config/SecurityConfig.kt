package br.com.pvsoftware.vuttr.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.core.convert.converter.Converter
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtValidators
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import reactor.core.publisher.Mono

@Profile("!test")
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfig {

    @Value("\${auth0.audience}")
    private lateinit var audience: String

    @Value("\${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private lateinit var issuer: String

    @Bean
    fun securitygWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http {
            authorizeExchange {
                authorize("/", permitAll)
                authorize("/swagger-ui/**", permitAll)
                authorize("/api/**", permitAll)
                authorize("/swagger-resources/**", permitAll)
                authorize("/v3/**", permitAll)
                authorize("/actuator/**", permitAll)
                authorize(anyExchange, authenticated)
            }
            cors {
                configurationSource = corsConfigurationSource()
            }
            oauth2ResourceServer {
                jwt {
                    jwtDecoder = jwtDecoder()
                    jwtAuthenticationConverter = grantedAuthoritiesExtractor()
                }
            }
        }
    }

    fun grantedAuthoritiesExtractor(): Converter<Jwt, Mono<AbstractAuthenticationToken>> {
        val extractor = CustomAuthenticationConverter()
        return ReactiveJwtAuthenticationConverterAdapter(extractor)
    }

    private fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedMethods = listOf(
            HttpMethod.GET.name,
            HttpMethod.PUT.name,
            HttpMethod.POST.name,
            HttpMethod.DELETE.name
        )
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration.applyPermitDefaultValues())
        return source
    }

    @Bean
    fun jwtDecoder(): NimbusReactiveJwtDecoder {
        val jwtDecoder = ReactiveJwtDecoders.fromIssuerLocation(issuer) as NimbusReactiveJwtDecoder
        val withAudience: OAuth2TokenValidator<Jwt> = AudienceValidator(audience)
        val withIssuer: OAuth2TokenValidator<Jwt> = JwtValidators.createDefaultWithIssuer(issuer)
        val validator: OAuth2TokenValidator<Jwt> = DelegatingOAuth2TokenValidator(withAudience, withIssuer)
        jwtDecoder.setJwtValidator(validator)
        return jwtDecoder
    }
}

class CustomAuthenticationConverter : Converter<Jwt, AbstractAuthenticationToken> {

    override fun convert(source: Jwt): AbstractAuthenticationToken? {
        val converter = JwtGrantedAuthoritiesConverter()
        converter.setAuthoritiesClaimName("permissions")
        converter.setAuthorityPrefix("")

        val jwtConverter = JwtAuthenticationConverter()
        jwtConverter.setJwtGrantedAuthoritiesConverter(converter)

        return jwtConverter.convert(source)
    }
}
