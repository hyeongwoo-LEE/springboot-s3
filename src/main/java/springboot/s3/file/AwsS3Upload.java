package springboot.s3.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
@Component
public class AwsS3Upload {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;
    
    public Map<String,String> S3Upload(File uploadFile, String storeFilename){

        //String folderPath = makeFolder();

        String folderPath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String storeFullPath = getStoreFullPath(folderPath, storeFilename);

        putS3(uploadFile, storeFullPath);

        //File 로 전환되면서 로컬에 파일 생성된것을 삭제합니다.
        removeNewFile(uploadFile);

        Map<String,String> pathMap = new HashMap<>();
        pathMap.put("folderPath", folderPath);
        pathMap.put("storeFilename", storeFilename);

        return pathMap;
    }

    private String putS3(File uploadFile, String storeFullPath){

        amazonS3.putObject(new PutObjectRequest(bucket, storeFullPath, uploadFile)
                // 읽기 public 권한 부여
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return amazonS3.getUrl(bucket, storeFullPath).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }


    public String getStoreFullPath(String folderPath, String storeFilename){
        return folderPath + "/" + storeFilename;
    }
}
