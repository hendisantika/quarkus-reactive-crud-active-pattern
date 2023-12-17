package com.hendisantika.controller;

import com.hendisantika.entity.ShoppingCart;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;

import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

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

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getSingleCart(@PathParam("id") Long id) {
        return ShoppingCart.findByShoppingCartId(id)
                .onItem().ifNotNull().transform(cart -> Response.ok(cart).build())
                .onItem().ifNull().continueWith(Response.ok().status(NOT_FOUND)::build);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> createShoppingCart(ShoppingCart shoppingCart) {
        if (shoppingCart == null || shoppingCart.name == null) {
            throw new WebApplicationException("ShoppingCart name was not set on request.", 422);
        }
        return ShoppingCart.createShoppingCart(shoppingCart)
                .onItem().transform(id -> URI.create("/v1/carts/" + id.id))
                .onItem().transform(uri -> Response.created(uri))
                .onItem().transform(Response.ResponseBuilder::build);
    }
}
