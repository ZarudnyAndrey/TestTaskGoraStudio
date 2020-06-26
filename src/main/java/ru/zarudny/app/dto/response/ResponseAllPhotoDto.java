package ru.zarudny.app.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zarudny.app.entity.Photo;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAllPhotoDto {

  long count;
  List<Photo> photos;
}
