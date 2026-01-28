package haennihaesseo.sandoll.domain.letter.controller;

import haennihaesseo.sandoll.domain.letter.dto.response.VoiceSaveResponse;
import haennihaesseo.sandoll.domain.letter.service.LetterService;
import haennihaesseo.sandoll.domain.letter.status.LetterSuccessStatus;
import haennihaesseo.sandoll.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/letter")
@RequiredArgsConstructor
@Slf4j
public class LetterController {

  private final LetterService letterService;

  @PostMapping("/voice")
  public ResponseEntity<ApiResponse<VoiceSaveResponse>> saveVoiceFile(
      @RequestPart(value = "voice") MultipartFile voiceFile
  ) {
    VoiceSaveResponse responses = letterService.saveVoiceFile(voiceFile);
    return ApiResponse.success(LetterSuccessStatus.SUCCESS_301, responses);
  }

}
