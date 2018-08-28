package com.spring.controller;

import com.jcraft.jsch.*;
import com.spring.domain.Role;
import com.spring.domain.User;
import com.spring.repository.RoleRepository;
import com.spring.service.GenericService;
import com.spring.service.NextSequenceService;
import com.spring.utility.FileSystem;
import com.spring.utility.Global;
import com.spring.utility.Utility;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import com.spring.domain.*;
import com.spring.repository.*;
@RestController
@CrossOrigin(origins = "http://localhost:4200",allowedHeaders = "*")
@RequestMapping("/springjwt")
public class ResourceController {
    @Qualifier("genericServiceImpl")
    @Autowired
    private GenericService userService;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    NextSequenceService nextSequenceService;

    @RequestMapping(value ="/users", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('Administrateur')")
    public List<User> getUsers(){
        return userService.findAllUsers();
    }


    @PreAuthorize("hasAuthority('Administrateur')")
    @RequestMapping(value = "/user/", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> createUser(@RequestBody User user) {
        System.out.println("Creating User " + user.getEmail());
        user.setPassword(encoder().encodePassword(user.getPassword(), null));
        user.setId(nextSequenceService.getNextSequence("user"));
        if (userService.isUserExist(user)) {
            System.out.println("A User with the e-mail: " + user.getEmail() + " already exists");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

        userService.updateUser(user);

        return new ResponseEntity<Void>( HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('Administrateur') or hasAuthority('Utilisateur')")
    @RequestMapping(value = "/username/{username}/", method = RequestMethod.GET)
    public User getUser(@PathVariable ("username") String username){
        User user = userService.findByEmail(username);
        user.setPassword("********");
        return user;
    }
    @PreAuthorize("hasAuthority('Administrateur') or hasAuthority('Utilisateur')")
@RequestMapping(value = "/transaction", method = RequestMethod.GET)
    public ResponseEntity<Iterable<Transaction>> getTransactions() {
        return new ResponseEntity<>(transactionRepository.findAll(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('Administrateur')")
    @RequestMapping(value = "/product", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
public ResponseEntity<Product> addProduct(@RequestBody Product product ){
        product.setId(nextSequenceService.getNextSequence("produit"));
        return new ResponseEntity<>(productRepository.save(product), HttpStatus.OK);
}
@RequestMapping(value = "/product")
public ResponseEntity<Iterable<Product>> getProducts() {
        return new ResponseEntity<>(productRepository.findAll(), HttpStatus.OK);
}

    @PreAuthorize("hasAuthority('Administrateur')")
    @RequestMapping(value = "/product", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Product> updateProduct(@RequestBody Product product ){
        return new ResponseEntity<>(productRepository.save(product), HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority('Administrateur')")
    @RequestMapping(value = "/product/{id}/", method = RequestMethod.DELETE, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity deleteProduct(@PathVariable("id") Long product) {
           productRepository.delete(product);
            return new ResponseEntity<>( HttpStatus.OK);

    }

    @RequestMapping(value = "/user/username/{username}/", method = RequestMethod.GET)
    public ResponseEntity<Boolean> checkUsername(@PathVariable("username") String username) {
        List<String> usernames = new ArrayList<>();
        userService.findAllUsers().forEach(e -> {
            usernames.add(e.getEmail());
        });
        if(!usernames.contains(username)) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('Administrateur') or hasAuthority('Utilisateur')")
    @RequestMapping(value = "/user/", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        if (user != null) {
            User currentUser = userService.findByEmail(user.getEmail());

            if (currentUser == null) {
                user.setPassword(encoder().encodePassword(user.getPassword(), null));
                userService.updateUser(user);
                return new ResponseEntity<User>(HttpStatus.CREATED);
            }
            user.setRole(currentUser.getRole());

            if (user.getPassword() == null) {
                user.setPassword(currentUser.getPassword());
            } else {
                user.setPassword(encoder().encodePassword(user.getPassword(), null));
            }
            if (!user.getEmail().equals(""))
            userService.updateUser(user);
            user.setPassword("***********");
            return new ResponseEntity<User>(user, HttpStatus.OK);
        }
            return new ResponseEntity<User>(HttpStatus.NOT_ACCEPTABLE);
    }

    @PreAuthorize("hasAuthority('Administrateur') or hasAuthority('Utilisateur')")
    @RequestMapping(value = "/cashout/{n}/{id}/", method = RequestMethod.POST)
    public ResponseEntity cashOut(@RequestBody Product product, @PathVariable("n") int n, @PathVariable("id") String mail ) {
// todo transactions
        product.setQuantity(product.getQuantity()-n);
        productRepository.save(product);

Transaction transaction = new Transaction();

transaction.setId(nextSequenceService.getNextSequence("transaction"));
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
transaction.setTimestamp(timeStamp);
transaction.setProduct(product);
transaction.setQuantity(n);
transaction.setUser(userRepository.findByEmail(mail));
transactionRepository.save(transaction);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/inscription", method = RequestMethod.POST)
    public ResponseEntity inscription(@RequestBody User user) {
        // todo inscription
        user.setPassword(encoder().encodePassword(user.getPassword(), null));
        user.setId(nextSequenceService.getNextSequence("user"));
        if (userService.isUserExist(user)) {
            System.out.println("A User with the e-mail: " + user.getEmail() + " already exists");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
        roleRepository.findAll().forEach(e->{
            if(e.getRoleName().equals("Utilisateur")){
                user.setRole(e);
            }
        });

        userService.updateUser(user);

       return new ResponseEntity(HttpStatus.OK);
        }


    @PreAuthorize("hasAuthority('Administrateur')")
    @RequestMapping(value = "/user/{id}/", method = RequestMethod.DELETE)
    public ResponseEntity deleteUser(@PathVariable("id") Long id) {
        // todo next to finalize basic func tionalities !!!
        userRepository.delete(id);
        return  new ResponseEntity( HttpStatus.OK);
    }

    public ShaPasswordEncoder encoder() {
        return new ShaPasswordEncoder(256);
    }

}
