package study.jpashop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import study.jpashop.domain.item.Book;
import study.jpashop.domain.item.Item;
import study.jpashop.service.ItemService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form) {
        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/";
    }

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        Book item = (Book) itemService.findOne(itemId);
        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    @PostMapping("/items/{itemId}/edit")
    public String updateItem(@PathVariable("itemId") Long itemId,
                             @ModelAttribute("form") BookForm form) {
        /**
         * controller에서 어설프게 엔티티를 생성한 방법 (Bad)
         */
        Book book = new Book();
        book.setId(itemId);
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book); // merge 사용
//        또는
//        itemService.updateItemV1(itemId, book);   // 변경 감지 사용

        /**
         * 방법1 - 식별자(id)와 변경할 데이터를 명확하게 전달 (Good)
         */
//        itemService.updateItemV2(itemId, form.getName(), form.getPrice(), form.getStockQuantity());

        /**
         * 방법2 (파라미터가 많을 경우 dto에 담아서 service 계층으로 넘긴다 (Good)
         */
//        UpdateItemDto updateItemDto = new UpdateItemDto();
//        updateItemDto.setName(form.getName());
//        updateItemDto.setPrice(form.getPrice());
//        updateItemDto.setStockQuantity(form.getStockQuantity());
//        itemService.updateItemV3(itemId, updateItemDto);

        return "redirect:/items";
    }


}
