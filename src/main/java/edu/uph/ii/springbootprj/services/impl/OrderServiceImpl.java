package edu.uph.ii.springbootprj.services.impl;

import com.lowagie.text.DocumentException;
import edu.uph.ii.springbootprj.domain.*;
import edu.uph.ii.springbootprj.exceptions.OrderNotFoundOrConfirmedException;
import edu.uph.ii.springbootprj.repositories.DrugRepository;
import edu.uph.ii.springbootprj.repositories.OrderRepository;
import edu.uph.ii.springbootprj.repositories.OrderedProductRepository;
import edu.uph.ii.springbootprj.repositories.UserRepository;
import edu.uph.ii.springbootprj.services.EmailService;
import edu.uph.ii.springbootprj.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.mail.MessagingException;
import java.io.*;
import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderedProductRepository orderedProductRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    DrugRepository drugRepository;
    @Autowired
    EmailService emailService;

    @Value("${files.location}")
    private String photoDir;

    public String processForm(Model model, Order order, Principal p, BindingResult result) throws IOException {

        //if (result.hasErrors()) {
        //    model.addAttribute("formValidated", true);
        //    return "drugs/drugForm";
        //}
        User u = userRepository.findUserByUsername(p.getName());
        if (!u.dataFilled())
            return "redirect:../account/data";
        order.setStatus(Order.Status.NIEOPLACONY);
        orderRepository.save(order);
        for (var op : order.getZamowione_produkty())
            orderedProductRepository.save(op);
        return "redirect:../account/orders";
    }

    @Override
    public String delete(long id, Model model) {
        System.out.println("Id: " + id);
        var op = orderedProductRepository.findById(id).get();
        System.out.println(op);

        Order o = op.getOrder();
        o.getZamowione_produkty().remove(op);
        orderedProductRepository.deleteById(id);
        o.updatePrice();
        orderRepository.save(o);
        return "redirect:../cart";
    }

    @Override
    public String addToCart(long id, Model model, Principal p) {
        User u = userRepository.findUserByUsername(p.getName());
        Order koszyk = orderRepository.findByUserAndStatus(u, Order.Status.KOSZYK);
        if (koszyk == null) {
            koszyk = new Order(u, Order.Status.KOSZYK);
            orderRepository.save(koszyk);
        }
        var l = orderedProductRepository.findAllByOrder(koszyk);
        var opp = l.stream().filter(x -> x.getDrug().getId() == id).findAny().orElse(null);
        if (opp == null) {
            OrderedProduct op = new OrderedProduct(null, koszyk, drugRepository.findById(id).get(), 1);
            koszyk.getZamowione_produkty().add(op);
            orderRepository.save(koszyk);
            orderedProductRepository.save(op);
        } else {
            opp.setAmount(opp.getAmount()+1);
            orderedProductRepository.save(opp);
            orderRepository.save(koszyk);
        }

        return "redirect:../../cart";
    }

    @Override
    public String cancel(Model model, long id) {
        Order o = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundOrConfirmedException(id));
        if (o.getStatus() == Order.Status.OPLACONY) throw new OrderNotFoundOrConfirmedException(id);
        if (orderRepository.findByUserAndStatus(o.getUser(), Order.Status.KOSZYK) != null) {
            var lista = orderRepository.findAllByUserAndStatus(o.getUser(), Order.Status.KOSZYK);
            for(var order : lista) {
                for (var op : order.getZamowione_produkty())
                    orderedProductRepository.delete(op);
                orderRepository.delete(order);
            }
        };
        o.setStatus(Order.Status.KOSZYK);
        orderRepository.save(o);

        return "redirect:../../cart";
    }

    public String parseThymeleafTemplate(Order o) {
        //ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        //templateResolver.setSuffix(".html");
        //templateResolver.setTemplateMode(TemplateMode.HTML);

        //templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("order", o);
        context.setVariable("user", o.getUser());
        context.setVariable("logo", fileToBase64String("./src/main/resources/static/logo.png"));
        context.setVariable("background", fileToBase64String("./src/main/resources/static/dimension.png"));

        return templateEngine.process("invoice", context);
    }

    public void generatePdfFromHtml(String html, String path) throws DocumentException, IOException {
        OutputStream outputStream = new FileOutputStream(path);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);

        outputStream.close();
    }

    @Override
    public String showForm(Model model) {
        return null;
    }

    @Override
    public String showCart(Model model, Principal principal) {
        User u = userRepository.findUserByUsername(principal.getName());
        Order o = orderRepository.findByUserAndStatus(u, Order.Status.KOSZYK);
        if (o == null) {
            o = new Order(u, Order.Status.KOSZYK);
            orderRepository.save(o);
        }
        model.addAttribute("order", o);

        return "account/cart";
    }

    public String pay(Model model, long id) {
        var o = orderRepository.findById(id).get();
        o.setStatus(Order.Status.OPLACONY);
        orderRepository.save(o);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYMMdd");
        String fileName = "FV" + o.getOrderedAt().format(formatter) + "-" + o.getId() + ".pdf";
        String path = photoDir + File.separator + o.getUser().getId() + File.separator + "Invoices"
                + File.separator ;
        new File(path).mkdirs();
        path += fileName;
        try {
            generatePdfFromHtml(parseThymeleafTemplate(o),path);
        } catch (DocumentException e) { e.printStackTrace(); }
        catch (IOException e) { e.printStackTrace(); }

        String to = o.getUser().getEmail();
        //emailService.sendSimpleMessage(to,"Aktywacja konta",activationCode);
        var html = createInvoiceMessage(o);

        try {
            emailService.sendMimeMessage(to,"Podsumowanie zamówienia " + o.getId(), html, new File(path));
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return "redirect:../orders";
    }

    private String createInvoiceMessage(Order o) {
        var thymeleafCtx = new Context();
        thymeleafCtx.setVariable("header", "Potwierdzenie złożenia zamówienia");
        thymeleafCtx.setVariable("title", "Zamówienie nr " + o.getId());
        thymeleafCtx.setVariable("description", "Dziękujemy za złożenie zamówienia.");
        thymeleafCtx.setVariable("order",o);

        String html = templateEngine.process("mail-messages/invoiceMessage", thymeleafCtx);
        return html;
    }

    public String fileToBase64String(String path) {
        byte[] bytes = null;
        try {
        File file =  new File(path);
        System.out.println(file.getAbsolutePath());
        FileInputStream fileInputStreamReader = new FileInputStream(file);
        bytes = new byte[(int)file.length()];
        fileInputStreamReader.read(bytes);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(bytes);
    }



}
