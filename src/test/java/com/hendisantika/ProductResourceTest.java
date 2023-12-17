package com.hendisantika;

import com.hendisantika.entity.Product;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

/**
 * Created by IntelliJ IDEA.
 * Project : quarkus-reactive-crud-active-pattern
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 12/17/23
 * Time: 20:19
 * To change this template use File | Settings | File Templates.
 */
@QuarkusTest
public class ProductResourceTest {
    static final Jsonb jsonb = JsonbBuilder.create(new JsonbConfig());

    @Test
    @Order(1)
    public void testCreateProduct() {
        Product product = Product
                .builder()
                .title("product_title")
                .description("product_description")
                .build();
        given()
                .when()
                .header("Content-Type", "application/json")
                .body(jsonb.toJson(product))
                .post("/v1/products")
                .then()
                .statusCode(201);
    }
}
