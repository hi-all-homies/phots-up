package main.services.image;

import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface ImageService {
	
	public Mono<ResponseImgBB> storeImage(FilePart image);
}
