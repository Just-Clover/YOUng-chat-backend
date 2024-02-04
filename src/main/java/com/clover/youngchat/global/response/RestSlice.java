package com.clover.youngchat.global.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;

@JsonIgnoreProperties(ignoreUnknown = true, value = {"pageable"})
public class RestSlice<T> extends SliceImpl<T> {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public RestSlice(@JsonProperty("content") List<T> content,
        @JsonProperty("number") int page,
        @JsonProperty("size") int size,
        @JsonProperty("hasNext") boolean hasNext) {
        super(content, PageRequest.of(page, size), hasNext);
    }
}