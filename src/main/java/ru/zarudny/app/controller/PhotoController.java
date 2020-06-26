package ru.zarudny.app.controller;

import java.lang.instrument.IllegalClassFormatException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.zarudny.app.config.StorageConfig;
import ru.zarudny.app.dto.response.ResponseAllPhotoDto;
import ru.zarudny.app.service.PhotoService;

@RestController
@RequestMapping("/photo")
@AllArgsConstructor
public class PhotoController {

  PhotoService photoService;
  StorageConfig storageConfig;

  @PostMapping(value = "/", consumes = "multipart/form-data")
  @ResponseStatus(HttpStatus.OK)
  public String uploadPhoto(@RequestParam(value = "photo") MultipartFile uploadFile)
      throws IllegalClassFormatException {
    return photoService.uploadPhoto(uploadFile, storageConfig.getLocation().get("UPLOAD"));
  }

  @GetMapping("/")
  @ResponseStatus(HttpStatus.OK)
  public ResponseAllPhotoDto viewPhotos() {
    return photoService.viewPhotos();
  }

  @DeleteMapping("/{photoId}/")
  @ResponseStatus(HttpStatus.OK)
  public boolean deletePhoto(@PathVariable int photoId) {
    return photoService.deletePhoto(photoId);
  }
}
