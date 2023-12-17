package com.hendisantika.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.mutiny.Uni;
import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

/**
 * Created by IntelliJ IDEA.
 * Project : quarkus-reactive-crud-active-pattern
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 12/17/23
 * Time: 20:01
 * To change this template use File | Settings | File Templates.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Cacheable
public class Product extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String title;
    public String description;

    @JsonbDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    @CreationTimestamp
    public ZonedDateTime createdAt;

    @JsonbDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    @UpdateTimestamp
    public ZonedDateTime updatedAt;

    public static Uni<Product> findByProductId(Long id) {
        return findById(id);
    }

}
