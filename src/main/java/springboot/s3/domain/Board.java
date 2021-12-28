package springboot.s3.domain;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Entity
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(nullable = false)
    private String writer;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String title;

    @Builder.Default
    private String text = "";

    @Builder.Default
    private String folderPath = "";

    @Builder.Default
    private String filename = "";

}
