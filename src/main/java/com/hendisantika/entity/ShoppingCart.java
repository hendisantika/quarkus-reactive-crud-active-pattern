package com.hendisantika.entity;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple4;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.reactive.mutiny.Mutiny;

import java.time.Duration;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * Project : quarkus-reactive-crud-active-pattern
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 12/17/23
 * Time: 20:04
 * To change this template use File | Settings | File Templates.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NamedQueries(value = {@NamedQuery(name = "ShoppingCart.findAll",
        query = "SELECT c FROM ShoppingCart c LEFT JOIN FETCH c.cartItems item LEFT JOIN FETCH item.product"),
        @NamedQuery(name = "ShoppingCart.getById",
                query = "SELECT c FROM ShoppingCart c LEFT JOIN FETCH c.cartItems item LEFT JOIN FETCH item.product where c.id = ?1")})
public class ShoppingCart extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public int cartTotal;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn
    public Set<ShoppingCartItem> cartItems;

    public String name;

    public void calculateCartTotal() {
        cartTotal = cartItems.stream().mapToInt(ShoppingCartItem::getQuantity).sum();
    }

    public static Uni<ShoppingCart> findByShoppingCartId(Long id) {
        return find("#ShoppingCart.getById", id).firstResult();
    }

    public static Uni<List<ShoppingCart>> getAllShoppingCarts() {
        return find("#ShoppingCart.findAll").list();
    }

    public static Multi<ShoppingCart> findAllWithJoinFetch() {
        return stream("SELECT c FROM ShoppingCart c LEFT JOIN FETCH c.cartItems");
    }

    public static Uni<ShoppingCart> createShoppingCart(ShoppingCart shoppingCart) {
        return Panache
                .withTransaction(shoppingCart::persist)
                .replaceWith(shoppingCart)
                .ifNoItem()
                .after(Duration.ofMillis(10000))
                .fail()
                .onFailure()
                .transform(t -> new IllegalStateException(t));
    }

    public static Uni<ShoppingCart> deleteProductFromShoppingCart(Long shoppingCartId, Long productId) {

        Uni<ShoppingCart> cart = findById(shoppingCartId);
        Uni<Set<ShoppingCartItem>> cartItemsUni = cart
                .chain(shoppingCart -> Mutiny.fetch(shoppingCart.cartItems)).onFailure().recoverWithNull();

        Uni<Product> productUni = Product.findByProductId(productId);
        Uni<ShoppingCartItem> item = ShoppingCartItem.findByCartIdByProductId(shoppingCartId, productId).toUni();

        Uni<Tuple4<ShoppingCart, Set<ShoppingCartItem>, ShoppingCartItem, Product>> responses = Uni.combine()
                .all().unis(cart, cartItemsUni, item, productUni).asTuple();

        return Panache
                .withTransaction(() -> responses
                        .onItem().ifNotNull()
                        .transform(entity -> {
                            if (entity.getItem1() == null || entity.getItem4() == null
                                    || entity.getItem3() == null) {
                                return null;
                            }
                            entity.getItem3().quantity--;
                            if (entity.getItem3().quantity == 0) {
                                entity.getItem2().remove(entity.getItem3());
                            }
                            entity.getItem1().calculateCartTotal();
                            return entity.getItem1();
                        }));

    }

    public String toString() {
        return this.getClass().getSimpleName() + "<" + this.id + ">";
    }
}
