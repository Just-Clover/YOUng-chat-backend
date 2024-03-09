package com.clover.youngchat.domain.model;

import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@MappedSuperclass
public abstract class MongoBaseEntity implements Serializable {

    @CreatedDate
    @Field(name = "create_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field(name = "modified_at")
    private LocalDateTime modifiedAt;
}