package me.dk.addressbook.model;

import lombok.Data;

import java.util.List;


@Data
public class AddressBookModel {

    private String username;

    private List<PersonModel> friends;
}
