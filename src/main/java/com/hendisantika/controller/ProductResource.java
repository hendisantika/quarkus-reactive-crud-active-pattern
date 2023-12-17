package com.hendisantika.controller;

import com.hendisantika.entity.Product;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

/**
 * Created by IntelliJ IDEA.
 * Project : quarkus-reactive-crud-active-pattern
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 12/17/23
 * Time: 20:13
 * To change this template use File | Settings | File Templates.
 */
@Path("/v1/products")
public class ProductResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getProducts() {
        return Product.getAllProducts()
                .onItem().transform(products -> Response.ok(products))
                .onItem().transform(Response.ResponseBuilder::build);
    }

    @GET
    @Path("{id}")
    public Uni<Response> getSingleProduct(@PathParam("id") Long id) {
        return Product.findByProductId(id)
                .onItem().ifNotNull().transform(product -> Response.ok(product).build())
                .onItem().ifNull().continueWith(Response.ok().status(NOT_FOUND)::build);
    }
}
