package com.assessment.orderbook.mapper;

import com.assessment.orderbook.repository.InstrumentRepository;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DtoEntityMapperTest {

    @Autowired
    DtoEntityMapper dtoEntityMapper;

    @Mock
    InstrumentRepository instrumentRepository;



}
