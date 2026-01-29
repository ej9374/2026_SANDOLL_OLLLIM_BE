package haennihaesseo.sandoll.domain.font.service;


import haennihaesseo.sandoll.domain.font.entity.Font;
import haennihaesseo.sandoll.domain.font.repository.FontRepository;
import haennihaesseo.sandoll.global.exception.GlobalException;
import haennihaesseo.sandoll.global.infra.AwsS3Client;
import haennihaesseo.sandoll.global.status.ErrorStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class FontUploadService {

  private final FontRepository fontRepository;
  private final AwsS3Client s3Client;

  /**
   * 폰트 파일 업로드 및 DB 저장
   * @param fonts
   * @param fontNames
   * @return 업로드된 폰트 개수
   */
  @Transactional
  public int uploadFonts(List<MultipartFile> fonts, List<String> fontNames, String directory) {
    if (fonts.size() != fontNames.size()) {
      throw new GlobalException(ErrorStatus.BAD_REQUEST);
    }

    int uploadCount = 0;
    for (MultipartFile font : fonts) {
      String fileUrl = s3Client.uploadFile(directory, font);

      // 폰트 저장 -> 키워드 어떻게 할지 고민
      fontRepository.save(Font.builder()
          .fontUrl(fileUrl)
          .name(fontNames.get(uploadCount))
          .price(0)
          .build());

      uploadCount++;
    }
    return uploadCount;
  }

}
