package main.services.image;

import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ImageServiceImpl implements ImageService{
	private final WebClient webClient;
	
	@Value("${images.api.key}")
	private String API_KEY;
	
	@Value("${error.image.url}")
	private String ERR_IMG_URL;
	
	private final String BASE_URL = "https://api.imgbb.com/1/upload";
	
	public ImageServiceImpl(WebClient.Builder builder) {
		this.webClient = builder.baseUrl(BASE_URL).build();
	}

	@Override
	public Mono<ResponseImgBB> storeImage(FilePart image) {
		final File file;
		MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
		try {
			file = File.createTempFile(image.filename(), null);
			image.transferTo(file);
	        bodyBuilder.part("image", new FileSystemResource(file));
	        
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		
		return this.webClient.post()
				.uri(uBuild -> uBuild.queryParam("key", API_KEY).build())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.body(BodyInserters.fromMultipartData(bodyBuilder.build()))
				.retrieve()
				.bodyToMono(ResponseImgBB.class)
				.onErrorReturn(new ResponseImgBB(new DataImgBB(null, ERR_IMG_URL, ERR_IMG_URL, 0)))
				.doFinally(signal -> file.delete());
	}
}

