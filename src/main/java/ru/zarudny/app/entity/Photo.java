package ru.zarudny.app.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.DynamicUpdate;

@Data
@Entity
@DynamicUpdate
@Builder
@Table(name = "photo")
@AllArgsConstructor
@NoArgsConstructor
public class Photo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(nullable = false)
  @NonNull
  private String previewLink;

  @Column(nullable = false)
  @NonNull
  private String basicLink;
}
