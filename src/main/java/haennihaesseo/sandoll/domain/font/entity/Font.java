package haennihaesseo.sandoll.domain.font.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Font {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "font_id", nullable = false)
    private Long fontId;

    @Column(name = "font_url", nullable = false)
    private String fontUrl;

    @Column(name = "image_url", nullable = false)
    private String image_url;

    @Enumerated(EnumType.STRING)
    @Column(name = "keyword", nullable = false)
    private Keyword keyword;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "bone", nullable = false)
    private Bone bone;


}
