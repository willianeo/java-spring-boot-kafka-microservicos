package com.store.car.service;

import com.store.car.dto.CarPostDTO;
import com.store.car.entity.CarPostEntity;
import com.store.car.message.KafkaConsumerMessage;
import com.store.car.repository.CarPostRepository;
import com.store.car.repository.OwnerPostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CarPostServiceImpl implements CarPostService {

    private final Logger LOG = LoggerFactory.getLogger(KafkaConsumerMessage.class);

    @Autowired
    private CarPostRepository carPostRepository;

    @Autowired
    private OwnerPostRepository ownerPostRepository;

    @Override
    public void newPostDetails(CarPostDTO carPostDTO) {
        CarPostEntity carPostEntity = mapCarDtoToEntity(carPostDTO);
        carPostRepository.save(carPostEntity);
    }

    @Override
    public List<CarPostDTO> getCarSales() {
        List<CarPostDTO> listCarsSales = new ArrayList<>();
        carPostRepository.findAll().forEach(item->{
            listCarsSales.add(mapCarEntityToDTO(item));
        });
        return listCarsSales;
    }

    @Override
    public void changeCarSale(CarPostDTO carPostDTO, Long postId) {
        carPostRepository.findById(postId).ifPresentOrElse(item->{
            item.setDescription(carPostDTO.getDescription());
            item.setContact(carPostDTO.getContact());
            item.setPrice(carPostDTO.getPrice());
            item.setBrand(carPostDTO.getBrand());
            item.setEngineVersion(carPostDTO.getEngineVersion());
            item.setModel(carPostDTO.getModel());

            carPostRepository.save(item);
        }, ()->{
            throw new NoSuchElementException();
        });
    }

    @Override
    public void removeCarSale(Long postId) {
        carPostRepository.deleteById(postId);
    }

    private CarPostDTO mapCarEntityToDTO(CarPostEntity item) {
        return CarPostDTO.builder()
                .brand(item.getBrand())
                .city(item.getCity())
                .model(item.getModel())
                .description(item.getDescription())
                .engineVersion(item.getEngineVersion())
                .createdDate(item.getCreatedDate())
                .ownerName(item.getOwnerPost().getName())
                .price(item.getPrice())
                .build();
    }

    private CarPostEntity mapCarDtoToEntity(CarPostDTO carPostDTO) {
        CarPostEntity carPostEntity = new CarPostEntity();

        ownerPostRepository.findById(carPostDTO.getOwnerId()).ifPresentOrElse(item->{
            LOG.info("Proprietario encontrado no banco: {}", item);
            carPostEntity.setOwnerPost(item);
            carPostEntity.setContact(item.getContactNumber());
        }, ()->{
            LOG.warn("Nenhum proprietario encontrado com ID: {}", carPostDTO.getOwnerId());
            throw new RuntimeException();
        });

        carPostEntity.setModel(carPostDTO.getModel());
        carPostEntity.setBrand(carPostDTO.getBrand());
        carPostEntity.setPrice(carPostDTO.getPrice());
        carPostEntity.setCity(carPostDTO.getCity());
        carPostEntity.setDescription(carPostDTO.getDescription());
        carPostEntity.setEngineVersion(carPostDTO.getEngineVersion());
        carPostEntity.setCreatedDate(String.valueOf(new Date()));

        return carPostEntity;
    }

}
