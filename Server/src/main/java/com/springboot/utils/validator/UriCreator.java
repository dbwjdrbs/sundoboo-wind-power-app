package com.springboot.utils.validator;

import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class UriCreator {
    public static URI creatorUrl(String defaultUrl, long resourceId){
        return UriComponentsBuilder.newInstance()
                .path(defaultUrl + "{resource-id}")
                .buildAndExpand(resourceId)
                .toUri();
    }
}
