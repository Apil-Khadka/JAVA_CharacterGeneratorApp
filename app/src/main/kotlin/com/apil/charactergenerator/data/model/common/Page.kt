package com.apil.charactergenerator.data.model.common

data class Page<T>(
    val content: List<T>,
    val pageable: Pageable,
    val totalPages: Int,
    val totalElements: Long,
    val last: Boolean,
    val size: Int,
    val number: Int,
    val sort: Sort,
    val numberOfElements: Int,
    val first: Boolean,
    val empty: Boolean
)

data class Pageable(
    val sort: Sort,
    val offset: Int,
    val pageNumber: Int,
    val pageSize: Int,
    val paged: Boolean,
    val unpaged: Boolean
)

data class Sort(
    val empty: Boolean,
    val sorted: Boolean,
    val unsorted: Boolean
)
