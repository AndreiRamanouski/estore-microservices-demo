package com.appsdev.estore.product.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_lookup")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductLookupEntity {

    @Id
    private String productId;
    @Column(unique = true)
    private String title;
}
