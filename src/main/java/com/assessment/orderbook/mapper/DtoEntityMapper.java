package com.assessment.orderbook.mapper;

import com.assessment.orderbook.dto.ExecutionDto;
import com.assessment.orderbook.dto.OrderDto;
import com.assessment.orderbook.entity.Execution;
import com.assessment.orderbook.entity.Order;
import com.assessment.orderbook.repository.InstrumentRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class DtoEntityMapper {

    @Autowired
    InstrumentRepository instrumentRepository;

    @Mapping(target="instrument", expression = "java(instrumentRepository.getById(orderDto.instrumentId()).get())")
    @Mapping(target = "id", ignore = true)
    public abstract Order orderDtoToOrder(OrderDto orderDto);

    @Mapping(target="instrument", expression = "java(instrumentRepository.getById(executionDto.instrumentId()).get())")
    @Mapping(target = "id", ignore = true)
    public abstract Execution executionDtoToExecution(ExecutionDto executionDto);
}
