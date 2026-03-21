package medical.app.backend.content.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "content_pages", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"slug", "locale"})
})
public class ContentPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String slug;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    /**
     * BCP 47 locale tag, e.g. "en", "tr".
     * One content page per slug+locale pair.
     */
    @Column(nullable = false, length = 10)
    private String locale;
}
