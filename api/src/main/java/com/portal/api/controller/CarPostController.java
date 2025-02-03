package com.portal.api.controller;

import com.portal.api.dto.CarPostDTO;
import com.portal.api.message.KafkaProducerMessage;
import com.portal.api.service.CarPostStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/car")
public class CarPostController {

    private final Logger LOG = LoggerFactory.getLogger(OwnerPostController.class);

    @Autowired
    private CarPostStoreService carPostStoreService;

    @Autowired
    private KafkaProducerMessage kafkaProducerMessage;

    @PostMapping("/post")
    public ResponseEntity postCarForSale(@RequestBody CarPostDTO carPostDTO) {
        LOG.info("API REST - Producer Car Post Information: {}", carPostDTO);
        kafkaProducerMessage.sendMessage(carPostDTO);
        return new ResponseEntity<>(OK);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<CarPostDTO>> getCarSales(){
        LOG.info("API REST - GetAll Car Posts Information.");
        return ResponseEntity.status(FOUND).body(carPostStoreService.getCarForSales());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity changeCarSale(@RequestBody CarPostDTO carPostDTO, @PathVariable("id") String id){
        LOG.info("API REST - Put Car Post Information By Id: {}", id);
        carPostStoreService.changeCarForSale(carPostDTO, id);
        return new ResponseEntity<>(OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCarForSale(@PathVariable("id") String id){
        LOG.info("API REST - Delete Car Post Information By Id: {}", id);
        carPostStoreService.removeCarForSale(id);
        return new ResponseEntity<>(OK);
    }
}
