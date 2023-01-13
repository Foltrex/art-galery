package com.scnsoft.art.dto.mapper;

public interface Mapper<T, V> {

    V mapToDto(T t);

    T mapToEntity(V v);

}
