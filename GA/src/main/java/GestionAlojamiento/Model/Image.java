package GestionAlojamiento.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alt")
    private String alt;

    @Column(name = "url", length = 255)
    private String url;

    @Column(name ="public_id")
    private String publicId;

    @ManyToOne
    @JoinColumn(name = "id_gallery")
    private Gallery gallery;
}
