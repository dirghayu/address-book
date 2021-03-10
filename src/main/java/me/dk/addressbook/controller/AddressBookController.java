package me.dk.addressbook.controller;

import me.dk.addressbook.entity.AddressBook;
import me.dk.addressbook.exceptions.AlredyExistsException;
import me.dk.addressbook.exceptions.NotFoundException;
import me.dk.addressbook.model.AddressBookModel;
import me.dk.addressbook.model.ExceptionResponse;
import me.dk.addressbook.service.AddressBookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/v1/address-book")
@ControllerAdvice
public class AddressBookController {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AddressBookService addressBookService;

    @PostMapping
    public ResponseEntity<AddressBookModel> createAddressBook(@RequestBody @Valid AddressBookModel addressBookModel) {
        if(addressBookService.findByName(addressBookModel.getUsername()).isPresent())
            throw new AlredyExistsException("User :" + addressBookModel.getUsername() +" already exists.");

        AddressBook savedEntity = addressBookService.save(convertToAddressBookEntity(addressBookModel));
        return ResponseEntity.ok(convertToAddressBookModel(savedEntity));
    }
    @PutMapping("/@{username}")
    public ResponseEntity<AddressBookModel> updateAddressBook(@PathVariable("username") String username, @RequestBody @Valid AddressBookModel addressBookModel) {

        if(!username.equals(addressBookModel.getUsername()))
            throw new IllegalArgumentException("User in path and body do not match!!");

        AddressBook savedEntity = addressBookService.update(convertToAddressBookEntity(addressBookModel));
        return ResponseEntity.ok(convertToAddressBookModel(savedEntity));
    }

    @GetMapping("/@{username}")
    public ResponseEntity<AddressBookModel> getAddressBook(@PathVariable("username") String username) {
        Optional<AddressBook> addressBook = addressBookService.findByName(username);
        if(addressBook.isPresent()){
            return ResponseEntity.ok(convertToAddressBookModel(addressBook.get()));

        } else {
            throw new NotFoundException("Adddress book for " + username +" not found." );
        }

    }

    @GetMapping("/common-friends")
    public ResponseEntity<Object> getCommonFriends(@RequestParam("username1") String username1, @RequestParam("username2") String username2) {
        return ResponseEntity.ok(addressBookService.getCommonFriends(username1, username2));
    }

    private AddressBookModel convertToAddressBookModel(AddressBook addressBookEntity){
        AddressBookModel addressBookModel = modelMapper.map(addressBookEntity, AddressBookModel.class);
        return addressBookModel;
    }
    private AddressBook convertToAddressBookEntity(AddressBookModel addressBookEntity){
        AddressBook addressBook = modelMapper.map(addressBookEntity, AddressBook.class);
        return addressBook;
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public @ResponseBody
    ExceptionResponse handleResourceNotFound(final NotFoundException exception,
                                             final HttpServletRequest request) {

        ExceptionResponse error = new ExceptionResponse();
        error.setErrorMessage(exception.getMessage());

        return error;
    }
    @ExceptionHandler(AlredyExistsException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public @ResponseBody
    ExceptionResponse handleResourceNotFound(final AlredyExistsException exception,
                                             final HttpServletRequest request) {

        ExceptionResponse error = new ExceptionResponse();
        error.setErrorMessage(exception.getMessage());

        return error;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody
    ExceptionResponse handleIllegalArgumentException    (final AlredyExistsException exception,
                                             final HttpServletRequest request) {

        ExceptionResponse error = new ExceptionResponse();
        error.setErrorMessage(exception.getMessage());

        return error;
    }
}
