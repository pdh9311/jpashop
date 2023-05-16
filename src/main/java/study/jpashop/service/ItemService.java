package study.jpashop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.jpashop.controller.UpdateItemDto;
import study.jpashop.domain.item.Book;
import study.jpashop.domain.item.Item;
import study.jpashop.repository.ItemRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void updateItemV1(Long itemId, Book book) {
        Book findBook = (Book)itemRepository.findOne(itemId);
        findBook.setName(book.getName());
        findBook.setPrice(book.getPrice());
        findBook.setStockQuantity(book.getStockQuantity());
        findBook.setAuthor(book.getAuthor());
        findBook.setIsbn(book.getIsbn());
    }

    @Transactional
    public void updateItemV2(Long itemId, String name, int price, int stockQuantity) {
        Book findBook = (Book)itemRepository.findOne(itemId);
        findBook.setName(name);
        findBook.setPrice(price);
        findBook.setStockQuantity(stockQuantity);
    }

    @Transactional
    public void updateItemV3(Long itemId, UpdateItemDto dto) {
        Book findBook = (Book)itemRepository.findOne(itemId);
        findBook.setName(dto.getName());
        findBook.setPrice(dto.getPrice());
        findBook.setStockQuantity(dto.getStockQuantity());
    }


    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

}
