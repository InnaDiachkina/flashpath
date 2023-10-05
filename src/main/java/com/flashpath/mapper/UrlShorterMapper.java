package com.flashpath.mapper;

import com.flashpath.config.MapperConfig;
import com.flashpath.dto.UrlShorterRequestDto;
import com.flashpath.dto.UrlShorterResponseDto;
import com.flashpath.model.UrlShorter;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UrlShorterMapper {
    UrlShorter toModel(UrlShorterRequestDto requestDto);

    UrlShorterResponseDto toDto(UrlShorter urlShorter);
}
