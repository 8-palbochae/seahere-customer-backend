package com.seahere.backend.incoming.entity;

import com.seahere.backend.company.entity.CompanyEntity;
import com.seahere.backend.product.entity.ProductEntity;
import com.seahere.backend.user.domain.UserEntity;
import lombok.*;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "incoming")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class IncomingEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long incomingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private CompanyEntity company;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    private Float quantity;

    private LocalDate incomingDate;

    private int incomingPrice;
    private String memo;
    private String countryDetail;
    private String country;
    private String naturalStatus;
    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public void enroll(UserEntity user, CompanyEntity company) {
        this.user = user;
        this.company = company;
    }

    @Builder
    public IncomingEntity(CompanyEntity company, UserEntity user, LocalDate incomingDate) {
        this.company = company;
        this.user = user;
        this.incomingDate = incomingDate;
    }
    public void edit(int incomingPrice){
        this.incomingPrice = (int) (incomingPrice * quantity);
    }



}
