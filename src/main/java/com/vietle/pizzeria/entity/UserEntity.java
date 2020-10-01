package com.vietle.pizzeria.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "user", schema = "pizzeria")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "PASSWORD")
    private String password;
    @Type(type = "com.vladmihalcea.hibernate.type.array.ListArrayType")
    @Column(name = "ROLES", columnDefinition = "text[]")
    private List<String> roles;
    @Column(name = "NICK_NAME")
    private String nickName;
    @Column(name = "CREATED_TIMESTAMP")
    private Date createdTimestamp;
}
