package haennihaesseo.sandoll.domain.deco.controller;

import haennihaesseo.sandoll.domain.deco.dto.response.TemplateImageResponse;
import haennihaesseo.sandoll.domain.deco.dto.response.TemplatesResponse;
import haennihaesseo.sandoll.domain.deco.service.TemplateService;
import haennihaesseo.sandoll.domain.deco.status.DecoSuccessStatus;
import haennihaesseo.sandoll.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/deco/template")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService templateService;

    @GetMapping
    ResponseEntity<ApiResponse<TemplatesResponse>> getTemplates(){
        TemplatesResponse responses = templateService.getAllTemplates();
        return ApiResponse.success(DecoSuccessStatus.SUCCESS_401, responses);
    }

    @PostMapping
    ResponseEntity<ApiResponse<TemplateImageResponse>> setTemplate(
        @RequestHeader String letterKey,
        @RequestParam(name = "templateId") Long templateId
    ){
        TemplateImageResponse response = templateService.setTemplateToLetter(letterKey, templateId);
        return ApiResponse.success(DecoSuccessStatus.SUCCESS_402, response);
    }
}
