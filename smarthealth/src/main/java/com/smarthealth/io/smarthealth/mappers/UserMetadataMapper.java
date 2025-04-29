package com.smarthealth.io.smarthealth.mappers;

import com.smarthealth.io.smarthealth.dtos.UserMetadataDto;
import com.smarthealth.io.smarthealth.models.UserMetadata;
import com.smarthealth.io.smarthealth.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMetadataMapper {

    public static UserMetadata fromDto(UserMetadataDto dto, User user) {
        UserMetadata metadata = new UserMetadata();
        metadata.setUser(user);
        metadata.setKey(dto.getKey());
        metadata.setValue(dto.getValue());
        return metadata;
    }

    public static UserMetadataDto toDto(UserMetadata metadata) {
      
       UserMetadataDto dto =  new UserMetadataDto();

            metadata.getUser().getUserId();
            metadata.getKey();
            metadata.getValue();

            return dto;
        
    }
}