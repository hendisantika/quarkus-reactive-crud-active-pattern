package com.hendisantika.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.common.constraint.NotNull;
import io.smallrye.mutiny.Multi;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * Project : quarkus-reactive-crud-active-pattern
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 12/17/23
 * Time: 20:06
 * To change this template use File | Settings | File Templates.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Cacheable
@Getter
@Table(indexes = {
        @Index(name = "shopping_cart_item_cart_product_index", columnList = "cart_id, product_id"),
})
public class ShoppingCartItem extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonbTransient
    public Long id;

    @Column(name = "total_price", precision = 21, scale = 2)
    public BigDecimal totalPrice;

    @Column(name = "quantity")
    public Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JsonbTransient
    public ShoppingCart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Product product;

    public static Multi<ShoppingCartItem> findByCartIdByProductId(Long cartId, Long productId) {
        return stream("cart.id = ?1 and product.id = ?2", cartId, productId);
    }

}
