package com.smarthealth.io.smarthealth.mappers;

import com.smarthealth.io.smarthealth.dtos.UserMetadataDto;
import com.smarthealth.io.smarthealth.models.UserMetadata;
import com.smarthealth.io.smarthealth.models.User;

import org.springframework.stereotype.Component;

/**
 * Mapper para conversão entre entidades UserMetadata e DTOs.
 */
@Component
public class UserMetadataMapper {

    /**
     * Converte um DTO para entidade UserMetadata.
     */
    public UserMetadata fromDto(UserMetadataDto dto, User user) {
        if (dto == null) {
            return null;
        }

        UserMetadata metadata = new UserMetadata();
        metadata.setUser(user);
        metadata.setKey(dto.getKey());
        metadata.setValue(dto.getValue());
        return metadata;
    }

    /**
     * Converte uma entidade UserMetadata para DTO.
     */
    public UserMetadataDto toDto(UserMetadata metadata) {
        if (metadata == null) {
            return null;
        }

        UserMetadataDto dto = new UserMetadataDto();
        dto.setUserId(metadata.getUser().getUserId());
        dto.setKey(metadata.getKey());
        dto.setValue(metadata.getValue());

        return dto;
    }
}

