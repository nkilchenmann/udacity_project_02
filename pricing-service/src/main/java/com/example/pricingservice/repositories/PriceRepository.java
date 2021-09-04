package com.example.pricingservice.repositories;

import org.springframework.data.repository.CrudRepository;
import com.example.pricingservice.entities.Price;
import org.springframework.stereotype.Repository;

public interface PriceRepository extends CrudRepository<Price, Long> {
}
