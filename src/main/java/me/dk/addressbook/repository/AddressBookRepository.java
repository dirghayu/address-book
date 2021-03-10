package me.dk.addressbook.repository;

import me.dk.addressbook.entity.AddressBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface  AddressBookRepository extends JpaRepository<AddressBook, Long> {

    Optional<AddressBook> findByUsername(String username);

}
