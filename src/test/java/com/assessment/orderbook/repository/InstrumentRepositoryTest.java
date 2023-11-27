package com.assessment.orderbook.repository;

import com.assessment.orderbook.entity.Instrument;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class InstrumentRepositoryTest {

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Test
    public void testGetById() {
        Optional<Instrument> instrumentOptional = instrumentRepository.getById(1L);
        assertThat(instrumentOptional.isPresent()).isTrue();

        Instrument instrument = instrumentOptional.get();
        assertThat(instrument.getId()).isEqualTo(1L);
        assertThat(instrument.getName()).isEqualTo("Instrument-1");
    }

    @Test
    public void getAll() {
        List<Instrument> instruments = instrumentRepository.getAll();
        assertThat(instruments.size()).isEqualTo(10);
    }
}
