package haennihaesseo.sandoll.domain.letter.service;

import haennihaesseo.sandoll.domain.letter.dto.response.LetterDetailResponse;
import haennihaesseo.sandoll.domain.letter.dto.response.SecretLetterKeyResponse;
import haennihaesseo.sandoll.domain.letter.entity.Letter;
import haennihaesseo.sandoll.domain.letter.exception.LetterException;
import haennihaesseo.sandoll.domain.letter.repository.LetterRepository;
import haennihaesseo.sandoll.domain.letter.status.LetterErrorStatus;
import haennihaesseo.sandoll.domain.letter.util.AESUtil;
import haennihaesseo.sandoll.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LetterShareService {

    private final LetterDetailService letterDetailService;
    private final AESUtil aesUtil;
    private final LetterRepository letterRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 편지 아이디를 통해 암호화된 편지아이디를 발급
     * @param letterId
     * @return
     */
    public SecretLetterKeyResponse getLetterSecretKeyByLetterId(Long letterId) {
        try{
            String secretLetterKey = aesUtil.encrypt(letterId);
            log.info("secretLetterKey: {}", secretLetterKey);
            return new SecretLetterKeyResponse(secretLetterKey);
        } catch(Exception e){
            log.warn("암호화 실패");
            throw new RuntimeException(e);
        }
    }


    /**
     * 암호화된 편지 아이디와 비밀번호를 이용해 링크로 편지 조회
     * @param secretLetterKey
     * @param password
     * @return
     */
    public LetterDetailResponse getLetterDetailsByLink(String secretLetterKey, String password) {
        Long letterId = aesUtil.decrypt(secretLetterKey);
        log.info("letterId: {}", letterId);
        Letter letter = letterRepository.findById(letterId)
                .orElseThrow(() -> new LetterException(LetterErrorStatus.LETTER_NOT_FOUND));

        if (letter.getPassword() != null) {
            if (password == null)
                throw new GlobalException(LetterErrorStatus.LETTER_NEED_PASSWORD);
            if (!password.equals(letter.getPassword()))
                //!passwordEncoder.matches(password, letter.getPassword())
                throw new GlobalException(LetterErrorStatus.LETTER_WRONG_PASSWORD);
        }
        return letterDetailService.getLetterDetails(letterId);
    }
}
