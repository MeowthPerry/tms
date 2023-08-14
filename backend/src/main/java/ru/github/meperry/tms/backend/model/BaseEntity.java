package ru.github.meperry.tms.backend.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@MappedSuperclass
@Data
public abstract class BaseEntity {

  @CreatedDate
  @Column(name = "created")
  @CreationTimestamp
  @JsonIgnore
  private Date created;

  @LastModifiedDate
  @Column(name = "updated")
  @UpdateTimestamp
  @JsonIgnore
  private Date updated;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  @JsonIgnore
  private Status status;
}
