package com.shop.warehouse.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.warehouse.exception.InvalidItemException;
import com.shop.warehouse.modal.Item;
import com.shop.warehouse.modal.ItemCheckoutDTO;
import com.shop.warehouse.modal.ItemCheckoutResponse;
import com.shop.warehouse.repository.ItemRepository;

import io.micrometer.common.util.StringUtils;

@Service 
public class WarehouseService {
    
    @Autowired 
    private ItemRepository itemRepository;

    // get all items
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }
    // get item by id
    public Item getItemById(Long id) {
        return itemRepository.findById(id).orElse(null);
    }
    // get item by name
    public List<Item> getItemsByName(String name) {
        return itemRepository.findByNameContainingIgnoreCase(name);
    }
    // get item by size
    public List<Item> getItemsBySize(String size) {
        return itemRepository.findBySize(size);
    }
    // get item by color
    public List<Item> getItemsByColor(String color) {
        return itemRepository.findByColor(color);
    }
    // add new item
    public Item createItem(Item item) {
        return itemRepository.save(item);
    }
    // add multiple new items at once
    public List<Item> createItems(List<Item> items) {
        return itemRepository.saveAll(items);
    }
    // update item
    public Item updateItem(Long id, Item newItem) {
        Item item = getItemById(id);
        if (item != null) {
            item.setName(!StringUtils.isBlank(newItem.getName()) ? newItem.getName() : item.getName());
            item.setSize(!StringUtils.isBlank(newItem.getSize()) ? newItem.getSize() : item.getSize());
            item.setColor(!StringUtils.isBlank(newItem.getColor()) ? newItem.getColor() : item.getColor());
            item.setPrice(!StringUtils.isBlank(newItem.getPrice()) ? newItem.getPrice() : item.getPrice());
            item.setStock(newItem.getStock());
            return itemRepository.save(item);
        } else {
            throw new InvalidItemException("Item not found");
        }
    }
    // add multiple item stock
    public void stock(List<ItemCheckoutDTO> items) {
        List<Item> newItems = new ArrayList<>();
        for (ItemCheckoutDTO item : items) {
            Item stockedItem = getItemById(item.getId());
            // check if item is available or enough
            if (stockedItem != null) {
                stockedItem.setStock(stockedItem.getStock() + item.getAmount());
                newItems.add(stockedItem);
            }
            else {
                throw new InvalidItemException("Item not found");
            }
        }
        if (!newItems.isEmpty()) {
            itemRepository.saveAll(newItems);
        }
    }
    // checkout item
    public ItemCheckoutResponse checkout(List<ItemCheckoutDTO> items) {
        List<Item> purchasedItems = new ArrayList<>();
        // initialize checkout item
        List<Item> itemList = new ArrayList<>();
        int totalAmount = 0;
        Long totalPrice = (long) 0;
        for (ItemCheckoutDTO item : items) {
            Item purchasedItem = getItemById(item.getId());
            // check if item is available or enough
            if (purchasedItem != null) {
                if (purchasedItem.getStock() >= item.getAmount()) {
                    itemList.add(purchasedItem);
                    totalAmount += item.getAmount();
                    totalPrice += Long.valueOf(purchasedItem.getPrice()) * item.getAmount();
                    purchasedItem.setStock(purchasedItem.getStock() - item.getAmount());
                } else {
                    throw new InvalidItemException("Invalid amount of items");
                }
                purchasedItems.add(purchasedItem);
            }
            else {
                throw new InvalidItemException("Item not found");
            }
        }
        if (!purchasedItems.isEmpty()) {
            itemRepository.saveAll(purchasedItems);
        }
        // create checkout response
        ItemCheckoutResponse resp = new ItemCheckoutResponse();
        resp.setItems(itemList);
        resp.setAmount(totalAmount);
        resp.setTotalPrice(String.valueOf(totalPrice));
        return resp;
    }
    // delete item
    public void deleteItem(Long id) {
        Item item = getItemById(id);
        if (item != null) {
            itemRepository.deleteById(id);
        } else {
            throw new InvalidItemException("Item not found");
        }
    }
}
