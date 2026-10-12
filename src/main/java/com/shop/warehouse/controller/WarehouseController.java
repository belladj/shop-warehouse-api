package com.shop.warehouse.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.shop.warehouse.exception.InvalidItemException;
import com.shop.warehouse.modal.Item;
import com.shop.warehouse.modal.ItemCheckoutDTO;
import com.shop.warehouse.modal.ItemCheckoutResponse;
import com.shop.warehouse.service.WarehouseService;

@RestController
@RequestMapping ("/items")
public class WarehouseController {

    @Autowired 
    private WarehouseService warehouseService;

    // get all items
    @GetMapping
    public List<Item> getAllItems() {
        return warehouseService.getAllItems();
    }
    // get item by id
    @GetMapping("/{id}")
    public Item getItemById(@PathVariable Long id) {
        return warehouseService.getItemById(id);
    }
    // get item by name
    @GetMapping("/search/{name}")
    public List<Item> getItemsByName(@PathVariable String name) {
        return warehouseService.getItemsByName(name);
    }
    // get item by size
    @GetMapping("/size/{size}")
    public List<Item> getItemsBySize(@PathVariable String size) {
        return warehouseService.getItemsBySize(size);
    }
    // get item by color
    @GetMapping("/color/{color}")
    public List<Item> getItemsByColor(@PathVariable String color) {
        return warehouseService.getItemsByColor(color);
    }
    // add new item
    @PostMapping
    public Item createItem(@RequestBody Item item) {
        return warehouseService.createItem(item);
    }
    // add multiple new items
    @PostMapping("/batch")
    public List<Item> createItems(@RequestBody List<Item> items) {
        return warehouseService.createItems(items);
    }
    // update item
    @PutMapping("/{id}")
    public Item updateItem(@RequestBody Item item, @PathVariable Long id) {
        return warehouseService.updateItem(id, item);
    }
    // add multiple item stocks
    @PostMapping("/stock")
    public ResponseEntity<List<ItemCheckoutDTO>> stock(@RequestBody List<ItemCheckoutDTO> items) {
       warehouseService.stock(items);
       return ResponseEntity.ok().body(items);
    }
    // checkout item
    @PostMapping("/checkout")
    public ResponseEntity<ItemCheckoutResponse> checkout(@RequestBody List<ItemCheckoutDTO> items) {
       ItemCheckoutResponse resp = warehouseService.checkout(items);
       if (resp != null) {
        return ResponseEntity.ok().body(resp);
       } else {
        return ResponseEntity.internalServerError().body(null);
       }
    }
    // delete item
    @DeleteMapping("/{id}")
    public ResponseEntity<Item> deleteItem(@PathVariable Long id) {
        Item deletedItem = warehouseService.getItemById(id);
        warehouseService.deleteItem(id);
        return ResponseEntity.ok().body(deletedItem);
    }
    // handle exception when invalid item
    @ExceptionHandler(InvalidItemException.class)
    public ResponseEntity<String> handleInvalidItemException(InvalidItemException e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }

}
