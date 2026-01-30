package haennihaesseo.sandoll.domain.letter.controller;

import haennihaesseo.sandoll.domain.letter.dto.request.LetterSaveRequest;
import haennihaesseo.sandoll.domain.letter.dto.response.SecretLetterKeyResponse;
import haennihaesseo.sandoll.domain.letter.service.LetterSaveService;
import haennihaesseo.sandoll.domain.letter.status.LetterSuccessStatus;
import haennihaesseo.sandoll.global.auth.principal.UserPrincipal;
import haennihaesseo.sandoll.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Letter Save", description = "편지 저장 API")
@RestController
@RequestMapping("/api/letter")
@RequiredArgsConstructor
public class LetterSaveController {

    private final LetterSaveService letterSaveService;

    @Operation(
            summary = "[5.1] 작성한 편지 저장 및 암호화된 편지 아이디 리턴"
    )
    @PostMapping("/share")
    public ResponseEntity<ApiResponse<SecretLetterKeyResponse>> saveLetter(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestHeader("letterId") String letterId,
            @RequestBody LetterSaveRequest request
    ){
        Long userId = userPrincipal.getUser().getUserId();
        SecretLetterKeyResponse response = letterSaveService.saveLetterAndLink(userId, letterId, request.getPassword());
        return ApiResponse.success(LetterSuccessStatus.SUCCESS_501, response);
    }

}
