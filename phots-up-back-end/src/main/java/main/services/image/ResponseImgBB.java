package main.services.image;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class ResponseImgBB {
    private final DataImgBB data;

    @JsonCreator()
    public ResponseImgBB(@JsonProperty("data") DataImgBB data) {
        this.data = data;
    }

    public DataImgBB getData() {
        return data;
    }
}
