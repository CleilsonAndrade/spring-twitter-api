package br.com.cleilsonandrade.springtwitterapi.controllers.dtos;

import java.util.List;

public record FeedDto(List<FeedItemDto> feedItens, int page, int pageSize, int totalPages, long totalElements) {

}
