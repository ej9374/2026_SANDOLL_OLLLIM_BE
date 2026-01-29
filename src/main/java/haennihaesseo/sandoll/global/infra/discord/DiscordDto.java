package haennihaesseo.sandoll.global.infra.discord;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class DiscordDto {
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  public static class MessageDto {
    @JsonProperty("content")
    private String content;

    @JsonProperty("embeds")
    private List<EmbedDto> embeds;
  }

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  public static class EmbedDto {
    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;
  }
}
