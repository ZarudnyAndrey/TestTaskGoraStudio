package ru.zarudny.app.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import ru.zarudny.app.dto.response.ResponseAllPhotoDto;
import ru.zarudny.app.utils.IntegrationTest;

@IntegrationTest
@TestMethodOrder(OrderAnnotation.class)
public class PhotoControllerIntegrationTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  TestRestTemplate testRestTemplate;

  @Test
  @Order(3)
  public void uploadPhoto_resultOk() throws Exception {
    MockMultipartFile file = new MockMultipartFile(
        "photo",
        "testPic.jpg",
        String.valueOf(MediaType.MULTIPART_FORM_DATA),
        Files.readAllBytes(Paths.get("testPic.jpg"))
    );

    mockMvc.perform(multipart("/photo/")
        .file(file)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @Order(1)
  void viewPhotos_resultBadRequest() {
    HttpEntity<String> entity = new HttpEntity<>("body", null);
    ResponseEntity<Object> response = testRestTemplate
        .exchange("/photo/", HttpMethod.GET, entity, Object.class);

    Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  @Order(4)
  void viewPhotos_resultOk() {
    HttpEntity<String> entity = new HttpEntity<>("body", null);
    ResponseEntity<ResponseAllPhotoDto> response = testRestTemplate
        .exchange("/photo/", HttpMethod.GET, entity, ResponseAllPhotoDto.class);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(1, response.getBody().getCount());
  }

  @Test
  @Order(5)
  void deletePhoto_resultOk() {
    HttpEntity<String> entity = new HttpEntity<>("body", null);
    ResponseEntity<Object> response = testRestTemplate
        .exchange("/photo/1/", HttpMethod.DELETE, entity, Object.class);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(true, response.getBody());
  }

  @Test
  @Order(2)
  void deletePhoto_resultBadRequest() {
    HttpEntity<String> entity = new HttpEntity<>("body", null);
    ResponseEntity<Object> response = testRestTemplate
        .exchange("/photo/1/", HttpMethod.DELETE, entity, Object.class);

    Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
}
