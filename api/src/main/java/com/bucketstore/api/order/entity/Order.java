package com.bucketstore.api.order.entity;

import com.bucketstore.api.auth.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate;

    // OrderItem들과의 양방향 연관관계 (Cascade 설정으로 Order 저장 시 Item도 자동 저장)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    // 주문 생성 메서드
    public static Order createOrder(Member member) {
        Order order = new Order();
        order.member = member;
        order.orderDate = LocalDateTime.now();
        return order;
    }
}