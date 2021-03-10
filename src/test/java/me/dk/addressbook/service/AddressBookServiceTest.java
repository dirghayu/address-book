package me.dk.addressbook.service;

import me.dk.addressbook.entity.AddressBook;
import me.dk.addressbook.entity.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
class AddressBookServiceTest {

    @Autowired AddressBookService service;

    @Test
    void save() {
        AddressBook addressBook = AddressBook.builder()
                .username("user1")
                .friends(Arrays.asList(
                        Person.builder().
                                name("DK")
                                .phoneNumber("123").build()
                )).build();
        AddressBook save = service.save(addressBook);

        Optional<AddressBook> user1 = service.findByName("user1");
        assertEquals(user1.isPresent(), true);
        assertEquals(user1.get().getFriends().size(), save.getFriends().size());
        assertEquals(user1.get().getFriends().get(0).getName(), save.getFriends().get(0).getName());

    }

    @Test
    void update() {
    }

    @Test
    void findByNameReturnAddressBookInSortedOrder() {

        String username = "user_for_sorted_Test";

        AddressBook addressBook = AddressBook.builder()
                .username(username)
                .friends(Arrays.asList(
                        Person.builder().
                                name("B")
                                .phoneNumber("111").build(),
                        Person.builder().
                                name("D")
                                .phoneNumber("222").build(),
                        Person.builder().
                                name("A")
                                .phoneNumber("333").build()
                )).build();
       service.save(addressBook);
        Optional<AddressBook> userInSearch = service.findByName(
                username);

        assertEquals(userInSearch.isPresent(), true);
        assertEquals(userInSearch.get().getFriends().size(), 3);
        assertEquals(userInSearch.get().getFriends().get(0).getName(), "A");
        assertEquals(userInSearch.get().getFriends().get(0).getPhoneNumber(), "333");
        assertEquals(userInSearch.get().getFriends().get(1).getName(), "B");
        assertEquals(userInSearch.get().getFriends().get(1).getPhoneNumber(), "111");

        assertEquals(userInSearch.get().getFriends().get(2).getName(), "D");
        assertEquals(userInSearch.get().getFriends().get(2).getPhoneNumber(), "222");

    }

    @Test
    void getCommonFriends() {
        String user1 = "User1";
        String user2 = "User2";
        String user3 = "User3";
        AddressBook addressBook = AddressBook.builder()
                .username(user1)
                .friends(Arrays.asList(
                        Person.builder().
                                name("B")
                                .phoneNumber("111").build(),
                        Person.builder().
                                name("D")
                                .phoneNumber("222").build(),
                        Person.builder().
                                name("A")
                                .phoneNumber("333").build()
                )).build();
        service.save(addressBook);

        AddressBook addressBook2 = AddressBook.builder()
                .username(user2)
                .friends(Arrays.asList(
                        Person.builder().
                                name("B")
                                .phoneNumber("111").build()
                )).build();
        service.save(addressBook2);

        AddressBook addressBook3 = AddressBook.builder()
                .username(user3)
                .friends(Arrays.asList(
                        Person.builder().
                                name("XYZ")
                                .phoneNumber("999").build()
                )).build();
        service.save(addressBook3);

        Set<String> commonFriends = service.getCommonFriends(user1, user2);
        assertEquals(commonFriends.size(), 1);
        assertEquals(commonFriends.toArray()[0], "B");

        Set<String> noCommonFriends = service.getCommonFriends(user1, user3);
        assertEquals(noCommonFriends.size(), 0);

    }
}