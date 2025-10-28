package com.example.learningtracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass // このクラスのフィールドを継承先のテーブルにマッピングする
@EntityListeners(AuditingEntityListener.class) // タイムスタンプの自動更新を有効化
public abstract class BaseEntity {

    @CreatedDate
    @Column(nullable = false, updatable = false) // 作成後は更新されない
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
