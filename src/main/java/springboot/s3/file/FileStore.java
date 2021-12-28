package springboot.s3.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@Component
public class FileStore {

    private final AwsS3Upload awsS3Upload;

    public Map<String,String> storeFile(MultipartFile multipartFile) throws IOException {

        //파일이름 - 확장자 포함
        String originalFilename = multipartFile.getOriginalFilename();

        //파일 저장 이름
        String storeFilename = createStoreFilename(originalFilename);

        File uploadFile = convert(multipartFile, storeFilename)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));

        Map<String, String> pathMap = awsS3Upload.S3Upload(uploadFile, storeFilename);

        return pathMap;
    }

    private Optional<File> convert(MultipartFile multipartFile, String storeFilename) throws IOException {

        File convertFile = new File(storeFilename);

        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(multipartFile.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }

    private String createStoreFilename(String originalFilename) {

        String uuid = UUID.randomUUID().toString(); //파일명 고유성 유지

        return uuid + "_" + originalFilename;
    }

}
