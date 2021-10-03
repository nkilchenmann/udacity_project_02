package com.example.pricingservice;

import com.example.pricingservice.entities.Price;
import com.example.pricingservice.repositories.PriceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class PricingServiceApplicationTests {

    @Autowired
    PriceRepository priceRepository;


    @Autowired
    MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    @Test
    void testPriceReturnedForExistingId() throws Exception {

        // Create new price or overwrite existing one for provided id
        Long priceId = 1L;
        String expectedCurrency = "CHF";
        BigDecimal expectedPrice = new BigDecimal(9999);
        priceRepository.save(new Price(priceId, expectedPrice, expectedCurrency));

        // Check that created/overwritten price is found
        mockMvc.perform(get("http://localhost:8082/services/prices/" + priceId)).andExpect(status().isOk());
    }


    @Test
    void testPriceNotFoundIfInexistentId() throws Exception {

        // Check and delete price if already existent
        Long priceId = 1L;
        if (priceRepository.findById(priceId).isPresent())
            priceRepository.deleteById(priceId);

        // Check that no price is found for deleted id
        mockMvc.perform(get("http://localhost:8082/services/prices/" + priceId)).andExpect(status().isNotFound());

    }


}


