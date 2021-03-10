package me.dk.addressbook.service;

import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import me.dk.addressbook.entity.AddressBook;
import me.dk.addressbook.entity.Person;
import me.dk.addressbook.exceptions.NotFoundException;
import me.dk.addressbook.repository.AddressBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AddressBookService {


    @Autowired
    private AddressBookRepository addressBookRepository;

    public AddressBook save(AddressBook addressBook) {
        return addressBookRepository.save(addressBook);
    }

    public AddressBook update(AddressBook addressBook) {
        Optional<AddressBook> addressBookMaybe = findByName(addressBook.getUsername());
        if (!addressBookMaybe.isPresent())
            throw new NotFoundException("User :" + addressBook.getUsername() + " not found");

        AddressBook entityFromDB = addressBookMaybe.get();
        entityFromDB.getFriends().clear();
        entityFromDB.setFriends(addressBook.getFriends());
        return addressBookRepository.save(entityFromDB);
    }

    public Optional<AddressBook> findByName(String name) {
        return addressBookRepository.findByUsername(name);
    }

    public Set<String> getCommonFriends(String user1, String user2) {
        Optional<AddressBook> addressBook1 = addressBookRepository.findByUsername(user1);
        Optional<AddressBook> addressBook2 = addressBookRepository.findByUsername(user2);

        Optional<Set<String>> commonFriends = addressBook1.flatMap(a1 -> addressBook2.map(a2 -> getCommonFriends(a1, a2)));

        return commonFriends.orElse(Collections.emptySet());

    }

    private Set<String> getCommonFriends(AddressBook addressBook1, AddressBook addressBook2) {
        List<Person> friends1 = addressBook1.getFriends();
        List<Person> friends2 = addressBook2.getFriends();
        return Sets.intersection(friends1.stream().map(f -> f.getName()).collect(Collectors.toSet()),
                friends2.stream().map(f -> f.getName()).collect(Collectors.toSet()));
    }
}
