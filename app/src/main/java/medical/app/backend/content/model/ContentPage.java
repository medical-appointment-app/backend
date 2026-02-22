package medical.app.backend.content.model;

import jakarta.persistence.*;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public String getLocale() { return locale; }
    public void setLocale(String locale) { this.locale = locale; }
}
