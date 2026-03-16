package com.ecommerce.service;

import com.ecommerce.entity.Category;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.Role;
import com.ecommerce.entity.User;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.RoleRepository;
import com.ecommerce.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class DataInitializerService {

    private static final Logger log = LoggerFactory.getLogger(DataInitializerService.class);

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializerService(RoleRepository roleRepository, UserRepository userRepository,
                                  CategoryRepository categoryRepository, ProductRepository productRepository,
                                  PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void initIfEmpty() {
        fixAdminPasswordIfNotBCrypt();
        if (roleRepository.count() > 0) {
            log.info("Data already present, skipping initializer.");
            return;
        }
        log.info("Initializing sample data...");
        Role roleUser = roleRepository.save(new Role("ROLE_USER"));
        Role roleAdmin = roleRepository.save(new Role("ROLE_ADMIN"));

        User admin = new User();
        admin.setEmail("admin@example.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFullName("Admin User");
        admin.setEnabled(true);
        admin.getRoles().add(roleAdmin);
        admin.getRoles().add(roleUser);
        userRepository.save(admin);

        Category electronics = categoryRepository.save(new Category("Electronics", "electronics"));
        Category clothing = categoryRepository.save(new Category("Clothing", "clothing"));

        Product p1 = new Product();
        p1.setName("Wireless Headphones");
        p1.setDescription("Noise-cancelling wireless headphones.");
        p1.setPrice(new BigDecimal("79.99"));
        p1.setStock(50);
        p1.setCategory(electronics);
        productRepository.save(p1);

        Product p2 = new Product();
        p2.setName("USB-C Cable");
        p2.setDescription("Durable USB-C to USB-A cable, 2m.");
        p2.setPrice(new BigDecimal("12.99"));
        p2.setStock(200);
        p2.setCategory(electronics);
        productRepository.save(p2);

        Product p3 = new Product();
        p3.setName("Cotton T-Shirt");
        p3.setDescription("Plain cotton t-shirt, multiple colors.");
        p3.setPrice(new BigDecimal("19.99"));
        p3.setStock(100);
        p3.setCategory(clothing);
        productRepository.save(p3);
        log.info("Sample data initialized (admin@example.com / admin123).");
    }

    /**
     * If admin user was inserted via SQL with a non-BCrypt or invalid password, update it so login works.
     * BCrypt hashes are exactly 60 characters; wrong length = invalid.
     */
    private void fixAdminPasswordIfNotBCrypt() {
        userRepository.findByEmail("admin@example.com").ifPresent(admin -> {
            String pwd = admin.getPassword();
            boolean invalid = pwd == null
                || (!pwd.startsWith("$2a$") && !pwd.startsWith("$2b$"))
                || pwd.length() != 60;
            if (invalid) {
                admin.setPassword(passwordEncoder.encode("admin123"));
                userRepository.save(admin);
                log.info("Updated admin@example.com password to BCrypt (use admin123 to login).");
            }
        });
    }
}
