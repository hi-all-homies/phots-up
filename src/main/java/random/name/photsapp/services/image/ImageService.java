package random.name.photsapp.services.image;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String upload(MultipartFile image);

    void delete(String path);
}