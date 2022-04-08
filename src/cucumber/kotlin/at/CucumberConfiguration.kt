package at

import com.johncooper.reactiveKotlin.ReactiveKotlinApplication
import io.cucumber.spring.CucumberContextConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MariaDBContainer
import org.testcontainers.junit.jupiter.Container

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration(classes = [ReactiveKotlinApplication::class])
@ActiveProfiles("test")
class CucumberConfiguration {
    private constructor()

    companion object {
        @Container
        private val mariaDBContainer = MariaDBContainer<Nothing>("mariadb:latest")

        @JvmStatic
        @DynamicPropertySource
        fun databaseProperties(registry: DynamicPropertyRegistry) {
            mariaDBContainer.withPassword("root")
            mariaDBContainer.withDatabaseName("mydb")
            mariaDBContainer.start()
            registry.add("spring.liquibase.url", mariaDBContainer::getJdbcUrl)
            registry.add("spring.liquibase.user")  {"root"}
            registry.add("spring.liquibase.password") {"root"}
            registry.add("spring.datasource.url", mariaDBContainer::getJdbcUrl)
            registry.add("spring.r2dbc.url") { mariaDBContainer.jdbcUrl.replace("jdbc", "r2dbc") }
            registry.add("spring.r2dbc.username", mariaDBContainer::getUsername)
            registry.add("spring.r2dbc.password", mariaDBContainer::getPassword)
            registry.add("spring.datasource.driver-class-name", mariaDBContainer::getDriverClassName)
        }
    }
}
