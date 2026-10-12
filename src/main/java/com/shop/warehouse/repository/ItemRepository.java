package com.shop.warehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shop.warehouse.modal.Item;

@Repository 
public interface ItemRepository extends JpaRepository<Item, Long>{

    List<Item> findByNameContainingIgnoreCase(String name);
    List<Item> findBySize(String size);
    List<Item> findByColor(String color);
}
