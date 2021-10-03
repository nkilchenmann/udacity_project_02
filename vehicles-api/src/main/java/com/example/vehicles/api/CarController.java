package com.example.vehicles.api;


import com.example.vehicles.domain.car.Car;
import com.example.vehicles.service.CarNotFoundException;
import com.example.vehicles.service.CarService;
import io.swagger.annotations.ApiOperation;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Implements a REST-based controller for the Vehicles API.
 */

@RestController
@RequestMapping("/cars")
class CarController {

    private final CarService carService;
    private final CarResourceAssembler assembler;

    CarController(CarService carService, CarResourceAssembler assembler) {
        this.carService = carService;
        this.assembler = assembler;
    }

    /**
     * Creates a list to store any vehicles.
     *
     * @return list of vehicles
     */
    @GetMapping
    @ApiOperation("Returns a list of all cars in the database")
    Resources<Resource<Car>> list() {
        List<Resource<Car>> resources = carService.list().stream().map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(resources,
                linkTo(methodOn(CarController.class).list()).withSelfRel());
    }

    /**
     * Gets information of a specific car by ID.
     *
     * @param id the id number of the given vehicle
     * @return all information for the requested vehicle
     */
    @GetMapping("/{id}")
    @ApiOperation("Returns a car by id")
    Resource<Car> get(@PathVariable Long id) {
        /**
         * DONE: Use the `findById` method from the Car Service to get car information.
         * DONE: Use the `assembler` on that car and return the resulting output.
         *   Update the first line as part of the above implementing.
         */
        try {
            Resource<Car> resource = assembler.toResource(carService.findById(id));
            return resource;
        } catch (Exception e) {
            throw new CarNotFoundException("No car exists with id: " + id);
        }

    }

    /**
     * Posts information to create a new vehicle in the system.
     *
     * @param car A new vehicle to add to the system.
     * @return response that the new vehicle was added to the system
     * @throws URISyntaxException if the request contains invalid fields or syntax
     */
    @PostMapping
    @ApiOperation("Adds a new car")
    ResponseEntity<?> post(@Valid @RequestBody Car car) throws URISyntaxException {
        /**
         * DONE: Use the `save` method from the Car Service to save the input car.
         * DONE: Use the `assembler` on that saved car and return as part of the response.
         *   Update the first line as part of the above implementing.
         */

        //my implementation (start)
        Resource<Car> resource = assembler.toResource(carService.findById(carService.save(car).getId()));
        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
        //my implementation (end)
    }

    /**
     * Updates the information of a vehicle in the system.
     *
     * @param id  The ID number for which to update vehicle information.
     * @param car The updated information about the related vehicle.
     * @return response that the vehicle was updated in the system
     */
    @PutMapping("/{id}")
    @ApiOperation("Updates a car by id")
    ResponseEntity<?> put(@PathVariable Long id, @Valid @RequestBody Car car) {
        /**
         * DONE: Set the id of the input car object to the `id` input.
         * DONE: Save the car using the `save` method from the Car service
         * DONE: Use the `assembler` on that updated car and return as part of the response.
         *   Update the first line as part of the above implementing.
         */

        //my implementation (start)
        car.setId(id);
        carService.save(car);
        Resource<Car> resource = assembler.toResource(carService.findById(id));
        return ResponseEntity.ok(resource);
        //my implementation (end)

        //Resource<Car> resource = assembler.toResource(new Car());
        //return ResponseEntity.ok(resource);
    }

    /**
     * Removes a vehicle from the system.
     *
     * @param id The ID number of the vehicle to remove.
     * @return response that the related vehicle is no longer in the system
     */
    @DeleteMapping("/{id}")
    @ApiOperation("Deletes a car by id")
    ResponseEntity<?> delete(@PathVariable Long id) {
        /**
         * DONE: Use the Car Service to delete the requested vehicle.
         */

        //my implementation (start)
        carService.delete(id);
        //my implementation (end)

        return ResponseEntity.noContent().build();
    }
}
