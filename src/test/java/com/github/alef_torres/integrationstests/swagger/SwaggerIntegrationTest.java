package com.github.alef_torres.integrationstests.swagger;

import com.github.alef_torres.config.TestConfigs;
import com.github.alef_torres.integrationstests.testcontainers.AbstractIntegrationsTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertTrue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerIntegrationTest extends AbstractIntegrationsTest {

    @Test
    void sholdDisplaySwagger() {
        var content = given()
                    .baseUri("/swagger-ui/index.html")
                    .baseUri("http://localhost") // O host base
                    .port(TestConfigs.SERVER_PORT)
                    .basePath("/swagger-ui/index.html")
                .when()
                    .get()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .asString();

        assertTrue(content.contains("Swagger UI"));
    }
}
