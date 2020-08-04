package main.services.image;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.util.IOUtils;

@Service
public class ImageServiceImpl implements ImageService{
	private static final String BUCKET_NAME = "phots-up-app-777";
	private final AmazonS3 amazonS3;

	public ImageServiceImpl(AmazonS3 amazonS3) {
		this.amazonS3 = amazonS3;
	}

	@Override
	public String storeImage(FilePart image, String... folders) {
		var prefix = UUID.randomUUID().toString();
		var filename = String.format("%s-%s", prefix, image.filename());
		
		try {
			var file = File.createTempFile(prefix, null);
			image.transferTo(file);
			this.amazonS3.putObject(buildPathToImage(folders), filename, file);
			file.delete();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		return filename;
	}
	
	@Override
	public void deleteImage(String imageKey, String... folders) {
		this.amazonS3.deleteObject(buildPathToImage(folders), imageKey);
	}

	@Override
	public byte[] retrieveImageByKey(String imageKey, String... folders) {
		try {
			var image = this.amazonS3.getObject(buildPathToImage(folders), imageKey);
			return IOUtils.toByteArray(image.getObjectContent());
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	
	private String buildPathToImage(String[] folders) {
		if (folders.length == 0)
			return BUCKET_NAME;
		else
			return String.format("%s/%s", BUCKET_NAME, folders[0]);
	}
}
