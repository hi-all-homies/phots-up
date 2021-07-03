package main.services.image;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class DataImgBB {
    private final String title;
    private final String url;
    private final String display_url;
    private final long time;
    

    @JsonCreator()
    public DataImgBB(@JsonProperty("title") String title,
                     @JsonProperty("url") String url,
                     @JsonProperty("display_url") String display_url,
                     @JsonProperty("time") long time) {
        this.title = title;
        this.url = url;
        this.display_url = display_url;
        this.time = time;
    }

 
}
