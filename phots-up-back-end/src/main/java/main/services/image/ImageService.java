package main.services.image;

import org.springframework.http.codec.multipart.FilePart;

public interface ImageService {
	
	public String storeImage(FilePart image);
	
	public byte[] retrieveImageByKey(String imageKey);
}
