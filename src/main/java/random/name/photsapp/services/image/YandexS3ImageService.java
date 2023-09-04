package random.name.photsapp.services.image;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class YandexS3ImageService implements ImageService {

    @Value("${s3.bucket.name:phots-up}")
    private String BUCKET_NAME;

    private final S3Client s3Client;


    @Override
    public String upload(MultipartFile image) {
        try {
            var key = UUID.randomUUID() + image.getOriginalFilename();

            var putRequest = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(key)
                    .contentType(image.getContentType())
                    .build();

            this.s3Client.putObject(
                    putRequest, RequestBody.fromBytes(image.getBytes()));

            return this.s3Client.utilities()
                    .getUrl(builder -> builder.bucket(BUCKET_NAME)
                            .key(key).build())
                    .toExternalForm();

        } catch (IOException ex){
            throw new RuntimeException(ex.getMessage());
        }
    }


    @Override
    public void delete(String path) {
        if (Objects.nonNull(path)){
            int ind = path.lastIndexOf("/");
            var key = path.substring(ind+1);

            this.s3Client.deleteObject(builder -> builder
                    .bucket(BUCKET_NAME)
                    .key(key).build());
        }
    }
}