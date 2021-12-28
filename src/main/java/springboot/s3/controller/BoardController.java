package springboot.s3.controller;

import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springboot.s3.dto.BoardDTO;
import springboot.s3.file.AwsS3Upload;
import springboot.s3.service.BoardService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RequiredArgsConstructor
@RestController
public class BoardController {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final BoardService boardService;
    private final AmazonS3 amazonS3;
    private final AwsS3Upload awsS3Upload;

    @PostMapping("/store")
    public String test(BoardDTO boardDTO) throws IOException {
        boardService.save(boardDTO);

        return "s3 저장 완료";
    }

    @GetMapping("/img/{storeFileName}")
    public ResponseEntity<Resource> downloadImage(@RequestParam("folderPath") String folderPath,
                                                  @PathVariable("storeFileName") String storeFileName) throws IOException, URISyntaxException {
        String storeFullPath = awsS3Upload.getStoreFullPath(folderPath, storeFileName);
        URL url = amazonS3.getUrl(bucket, storeFullPath);

        //MIME 타입
        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", Files.probeContentType(Path.of(url.getPath())));

        return new ResponseEntity<>(new UrlResource(url), header, HttpStatus.OK);
    }



}
