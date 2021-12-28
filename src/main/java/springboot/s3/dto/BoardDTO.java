package springboot.s3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import springboot.s3.domain.Board;

import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoardDTO {

    private Long boardId;

    private String writer;

    private String password;

    private String title;

    private String text;

    private MultipartFile imageFile;

    public Board toEntity(Map<String,String> pathMap){
        Board board = Board.builder()
                .writer(this.writer)
                .password(this.password)
                .title(this.title)
                .text(this.text)
                .folderPath(pathMap.get("folderPath"))
                .filename(pathMap.get("storeFilename"))
                .build();

        return board;
    }
}
