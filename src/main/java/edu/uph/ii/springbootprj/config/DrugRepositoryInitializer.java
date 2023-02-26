package edu.uph.ii.springbootprj.config;

import edu.uph.ii.springbootprj.domain.*;
import edu.uph.ii.springbootprj.repositories.*;
import edu.uph.ii.springbootprj.services.OrderService;
import edu.uph.ii.springbootprj.utils.DBDump;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.*;

@Configuration
public class DrugRepositoryInitializer {
    private DrugRepository drugRepository;
    private ProductTypeRepository productTypeRepository;
    private DrugFormRepository drugFormRepository;
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private OrderRepository orderRepository;
    private OrderedProductRepository orderedProductRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ReviewRepository reviewRepository;
    Random r = new Random();

    ///*
    @Autowired
    public void setDrugRepository(DrugRepository drugRepository) {
        this.drugRepository = drugRepository;
    }
    @Autowired
    public void setProductTypeRepository(ProductTypeRepository productTypeRepository) { this.productTypeRepository = productTypeRepository; }
    @Autowired
    public void setDrugFormRepository(DrugFormRepository drugFormRepository) { this.drugFormRepository = drugFormRepository; }
    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) { this.roleRepository = roleRepository; }
    @Autowired
    public void setUserRepository(UserRepository userRepository) { this.userRepository = userRepository; }
    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) { this.orderRepository = orderRepository; }
    @Autowired
    public void setOrderedProductRepository(OrderedProductRepository orderedProductRepository) { this.orderedProductRepository = orderedProductRepository; }
    // */

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    InitializingBean initializeDatabase() {
        return () -> {

            if (productTypeRepository.findAll().isEmpty()) {
                for (var dt: DBDump.productTypeList) {
                    dt.setId(null);
                    productTypeRepository.save(dt);
                }

                drugFormRepository.save(new DrugForm("tabletki"));
                drugFormRepository.save(new DrugForm("kapsułki"));
                drugFormRepository.save(new DrugForm("maść"));
                drugFormRepository.save(new DrugForm("krem"));
                drugFormRepository.save(new DrugForm("żel"));
                drugFormRepository.save(new DrugForm("syrop"));
                drugFormRepository.save(new DrugForm("krople"));

                var formyLekow = drugFormRepository.findAll();
                for (int i=0; i<20; i++) {
                    for (var drug: DBDump.drugList) {
                        drug.setId(null);
                        drug.setForms(new HashSet<>());
                        for (int j=0, n=r.nextInt(2)+1; j<n; j++) {
                            int idx = r.nextInt(7);
                            drug.getForms().add(formyLekow.get(idx));
                        }
                        drugRepository.save(drug);
                    }
                }
            }

            if (roleRepository.findAll().isEmpty()) {

                PasswordEncoder passwordEncoder = passwordEncoder();

                Role roleUser = roleRepository.save(new Role(Role.Types.ROLE_USER));
                Role roleAdmin = roleRepository.save(new Role(Role.Types.ROLE_ADMIN));

                User user = new User("user", true);
                user.setRoles(new HashSet<>(Arrays.asList(roleUser)));
                user.setPassword(passwordEncoder.encode("user"));
                user.setEmail("testjd@o2.pl");

                User user2 = new User("jankowalski", true);
                user.setRoles(new HashSet<>(Arrays.asList(roleUser)));
                user.setPassword(passwordEncoder.encode("user"));
                user.setEmail("testjd@o2.pl");

                User user3 = new User("tomaszlis", true);
                user.setRoles(new HashSet<>(Arrays.asList(roleUser)));
                user.setPassword(passwordEncoder.encode("user"));
                user.setEmail("testjd@o2.pl");

                User admin = new User("admin", true);
                admin.setRoles(new HashSet<>(Arrays.asList(roleAdmin)));
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setEmail("testjd@o2.pl");

                User test = new User("superuser", true);
                test.setRoles(new HashSet<>(Arrays.asList(roleAdmin, roleUser)));
                test.setPassword(passwordEncoder.encode("super"));
                test.setEmail("testjd@o2.pl");

                userRepository.save(user);
                userRepository.save(user2);
                userRepository.save(user3);
                userRepository.save(admin);
                userRepository.save(test);
            }

            if (orderRepository.findAll().isEmpty()) {
                String username = "user";
                Order.Status status = Order.Status.NIEOPLACONY;
                Order a = new Order(null, userRepository.findUserByUsername(username), null, new ArrayList<>(), 0.0, Order.Status.KOSZYK);
                orderRepository.save(a);
                for (int i=0; i<4; i++) {
                    Order o = new Order(null, userRepository.findUserByUsername(username), null, new ArrayList<>(), 0.0, status);
                    var zam_prod = new OrderedProduct(null, o, drugRepository.findById((long) r.nextInt(120) + 1).get(), r.nextInt(30) + 1);
                    var zam_prod2 = new OrderedProduct(null, o, drugRepository.findById((long) r.nextInt(120) + 1).get(), r.nextInt(5) + 1);
                    var zam_prod3 = new OrderedProduct(null, o, drugRepository.findById((long) r.nextInt(120) + 1).get(), r.nextInt(15) + 1);
                    o.getZamowione_produkty().add(zam_prod);
                    o.getZamowione_produkty().add(zam_prod2);
                    o.getZamowione_produkty().add(zam_prod3);
                    orderRepository.save(o);
                    orderedProductRepository.save(zam_prod);
                    orderedProductRepository.save(zam_prod2);
                    orderedProductRepository.save(zam_prod3);
                    //if (i==0)
                     //   orderService.generatePdfFromHtml(orderService.parseThymeleafTemplate(o), "C:/ruta/faktura.pdf");

                }
                Drug d1 = drugRepository.findById(1l).get();
                Drug d2 = drugRepository.findById(10l).get();
                Drug d3 = drugRepository.findById(30l).get();
                Review re = new Review(null, userRepository.findUserByUsername("user"), d1 , (byte) 4, "Przykładowa opinia", null);
                Review re1 = new Review(null, userRepository.findUserByUsername("jankowalski"), d2, (byte) 5, "Przykładowa opinia", null);
                Review re2 = new Review(null, userRepository.findUserByUsername("tomaszlis"), d3, (byte) 2, "Przykładowa opinia", null);
                reviewRepository.save(re);reviewRepository.save(re1);reviewRepository.save(re2);
                d1.updateRating((byte) 4);
                d2.updateRating((byte) 5);
                d3.updateRating((byte) 2);
                drugRepository.save(d1);drugRepository.save(d2);drugRepository.save(d3);


            }


        };
    }
}
