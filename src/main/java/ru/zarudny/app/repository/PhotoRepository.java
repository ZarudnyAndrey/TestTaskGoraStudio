package ru.zarudny.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.zarudny.app.entity.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer>,
    JpaSpecificationExecutor<Photo> {

  List<Photo> findAll();

  Optional<Photo> findById(int photoId);

  long count();

  void deleteById(Integer integer);
}
