package com.clover.youngchat.domain.user.entity;

import com.clover.youngchat.domain.user.dto.request.UserProfileEditReq;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String username;
    private String password;
    private String profileImage;

    @Builder
    private User(Long id, String email, String username, String password, String profileImage) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.profileImage = profileImage;
    }

    public void updateProfile(UserProfileEditReq req) {
        this.username = req.getUsername();
        this.profileImage = req.getProfileImage();
    }
  
   public void updatePassword(String password) {
        this.password = password;
   }
}