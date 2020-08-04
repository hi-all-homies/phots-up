package main.services.image;

import org.springframework.http.codec.multipart.FilePart;

public interface ImageService {
	
	public String storeImage(FilePart image, String... folders);
	
	public byte[] retrieveImageByKey(String imageKey, String... folders);
	
	public void deleteImage(String imageKey, String... folders);
}
