package br.com.pvsoftware.vuttr

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VuttrApplication

fun main(args: Array<String>) {
    runApplication<VuttrApplication>(*args)
}
