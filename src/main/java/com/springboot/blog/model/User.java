package com.springboot.blog.model;

import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.Set;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import javax.persistence.Entity;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = { "username" }),
    @UniqueConstraint(columnNames = { "email" }) })
public class User {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String name;

  private String username;

  private String email;

  private String password;

  @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
  @JoinTable(name = "user_roles", joinColumns = {
      @JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = {
          @JoinColumn(name = "role_id", referencedColumnName = "id") })
  private Set<Role> roles;

}
