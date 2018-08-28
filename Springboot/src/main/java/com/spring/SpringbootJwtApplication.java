package com.spring;

import com.spring.domain.Role;
import com.spring.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.spring.repository.*;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import com.spring.service.NextSequenceService;
import com.spring.domain.CustomSequences;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import com.spring.domain.Product;
import java.util.List;

@SpringBootApplication
@EnableMongoRepositories("com.spring.repository")
public class SpringbootJwtApplication implements CommandLineRunner{

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    NextSequenceService nextSequenceService;

    @Autowired
    SeqRepository seqRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;
	public static void main(String[] args) {
		SpringApplication.run(SpringbootJwtApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {

CustomSequences seqRole = new CustomSequences();
seqRole.setId("role");
seqRole.setSeq(0);
if(seqRepository.findOne("role")==null)
        seqRepository.save(seqRole);

CustomSequences seqUser = new CustomSequences();
seqUser.setId("user");
seqUser.setSeq(0);
            if(seqRepository.findOne("user")==null)
                seqRepository.save(seqUser);

CustomSequences seqProduit = new CustomSequences();
seqProduit.setId("produit");
seqProduit.setSeq(0);
            if(seqRepository.findOne("produit")==null)
                seqRepository.save(seqProduit);

CustomSequences seqTransaction = new CustomSequences();
        seqTransaction.setId("transaction");
        seqTransaction.setSeq(0);
        if(seqRepository.findOne("transaction")==null)
            seqRepository.save(seqTransaction);


        Role internaute = new Role();
        Role utilisateur = new Role();
        Role administrateur = new Role();


        internaute.setId(nextSequenceService.getNextSequence("role"));
        internaute.setDescription("Peut voir le site du vitrine sans connexion");
        internaute.setRoleName("Internaute");



        utilisateur.setId(nextSequenceService.getNextSequence("role"));
        utilisateur.setDescription("Doit obligatoirement s'authentifier afin d'acheter des produits");
        utilisateur.setRoleName("Utilisateur");



        administrateur.setId(nextSequenceService.getNextSequence("role"));
	    administrateur.setDescription("Gére les produits et les utilisateurs");
	    administrateur.setRoleName("Administrateur");

if(roleRepository.count() == 0) {
    roleRepository.save(utilisateur);
    roleRepository.save(administrateur);
    roleRepository.save(internaute);
}


	User user = new User();
	user.setId(nextSequenceService.getNextSequence("user"));
	user.setEmail("admin@angular.fr");
	user.setPassword(encoder().encodePassword("123", null));
	user.setRole(administrateur);

	if(userRepository.findByEmail("admin@angular.fr") == null) {
        userRepository.save(user);
    }
    Product product = new Product();
	product.setId(nextSequenceService.getNextSequence("produit"));
	product.setQuantity(11);
	product.setPrice(693f);
	product.setLabel("Smartphone ASUS ZenFone 3 Laser 64Go 4Go - Silver");
	product.setImage_url("https://www.mytek.tn/52134-large_default/smartphone-asus-zenfone-3-laser-4go-64go-silver.jpg");
if(productRepository.count() == 0) {
    productRepository.save(product);
}
	}
    public ShaPasswordEncoder encoder() {
        return new ShaPasswordEncoder(256);
    }


}
