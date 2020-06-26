package ru.zarudny.app.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.IllegalClassFormatException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import javax.imageio.ImageIO;
import javax.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.zarudny.app.config.StorageConfig;
import ru.zarudny.app.dto.response.ResponseAllPhotoDto;
import ru.zarudny.app.entity.Photo;
import ru.zarudny.app.exception.StorageException;
import ru.zarudny.app.repository.PhotoRepository;
import ru.zarudny.app.utils.RandomStringGenerator;

@Service
@AllArgsConstructor
public class PhotoService {

  PhotoRepository photoRepository;
  StorageConfig storageConfig;

  public String uploadPhoto(MultipartFile uploadFile, String location)
      throws IllegalClassFormatException {
    StringBuilder sbPreview = new StringBuilder();
    StringBuilder sbBasic = new StringBuilder();

    String expansion = uploadFile.getOriginalFilename().split("\\.")[1];

    if (!expansion.equals("jpg") &&
        !expansion.equals("jpeg") &&
        !expansion.equals("png")) {
      throw new IllegalClassFormatException("Illegal file format.");
    }

    sbPreview
        .append(RandomStringGenerator.randomString(8))
        .append(".")
        .append(expansion);

    sbBasic
        .append(RandomStringGenerator.randomString(10))
        .append(".")
        .append(expansion);

    Path previewPath = Paths.get(location).resolve(sbPreview.toString());
    Path basicPath = Paths.get(location).resolve(sbBasic.toString());

    try {

      try (InputStream inputStream = uploadFile.getInputStream()) {
        Files.copy(inputStream, basicPath, StandardCopyOption.REPLACE_EXISTING);
      }

      try (InputStream inputStream = uploadFile.getInputStream()) {
        ImageIO.write(resizePhoto(inputStream), expansion, previewPath.toFile());
      }

      Photo photo = Photo.builder()
          .previewLink(previewPath.toString())
          .basicLink(basicPath.toString())
          .build();

      photoRepository.save(photo);
      return previewPath.toString();

    } catch (IOException e) {
      throw new StorageException("Failed to store file. ", e);
    }
  }

  public ResponseAllPhotoDto viewPhotos() {
    long count = photoRepository.count();

    if (count == 0) {
      throw new EntityNotFoundException("No files.");
    }

    List<Photo> photos = photoRepository.findAll();

    return ResponseAllPhotoDto.builder()
        .count(count)
        .photos(photos)
        .build();
  }

  public boolean deletePhoto(int photoId) {
    Optional<Photo> optional = photoRepository.findById(photoId);

    if (optional.isEmpty()) {
      throw new EntityNotFoundException("No file with this id.");
    } else {
      Photo photo = optional.get();

      Path pathPreview = Paths.get(photo.getPreviewLink());
      Path pathBasic = Paths.get(photo.getBasicLink());

      try {
        Files.delete(pathBasic);
        Files.delete(pathPreview);
      } catch (IOException e) {
        e.printStackTrace();
      }

      if (!Files.exists(pathBasic) && !Files.exists(pathPreview)) {
        System.out.println("Files successfully deleted.");
        photoRepository.deleteById(photoId);
      }
    }

    return true;
  }

  public void init() {
    storageConfig.getLocation().values().forEach(l -> {
      try {
        Files.createDirectories(Paths.get(l));
      } catch (IOException e) {
        throw new StorageException("Could not initialize storage.", e);
      }
    });
  }

  private BufferedImage resizePhoto(InputStream inputStream) throws IOException {
    return Scalr.resize(ImageIO.read(inputStream), Method.AUTOMATIC, Mode.AUTOMATIC, 200,
        Scalr.OP_ANTIALIAS);
  }
}
