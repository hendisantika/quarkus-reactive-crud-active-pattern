package com.hendisantika;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

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
}
