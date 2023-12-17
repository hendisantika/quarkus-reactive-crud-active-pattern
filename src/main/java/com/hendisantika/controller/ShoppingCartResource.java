package com.hendisantika.controller;

import com.hendisantika.entity.ShoppingCart;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Created by IntelliJ IDEA.
 * Project : quarkus-reactive-crud-active-pattern
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 12/17/23
 * Time: 20:15
 * To change this template use File | Settings | File Templates.
 */
@Path("/v1/carts")
public class ShoppingCartResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getCarts() {
        return ShoppingCart.getAllShoppingCarts()
                .onItem().transform(shoppingcarts -> Response.ok(shoppingcarts))
                .onItem().transform(Response.ResponseBuilder::build);
    }
}
