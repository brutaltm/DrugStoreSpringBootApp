package edu.uph.ii.springbootprj.utils;

import edu.uph.ii.springbootprj.domain.Drug;
import edu.uph.ii.springbootprj.domain.ProductType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DBDump {
    public static List<Drug> drugList;
    public static List<ProductType> productTypeList;

    static {
        productTypeList = new ArrayList<>(5);
        productTypeList.add(new ProductType(1,"Lek"));
        productTypeList.add(new ProductType(2,"Suplement diety"));
        productTypeList.add(new ProductType(3,"Wyrób medyczny"));
        productTypeList.add(new ProductType(4,"Dietetyczny środek spożywczy"));


        drugList = new ArrayList<>();
        var lek = new Drug(1l, "APAP", "US Pharmacia", LocalDate.of(2020,01,01) , 9.99f, 8.99f, true);
        lek.setProductType(productTypeList.get(0));
        drugList.add(lek);
        lek = new Drug(2l, "Ibuprom", "USP Zdrowie", LocalDate.of(2019,04,21) , 15.99f, 12.99f, true);
        lek.setProductType(productTypeList.get(0));
        drugList.add(lek);
        lek = new Drug(3l, "Voltaren", "NOVARTIs", LocalDate.of(2018,11,11) , 21.99f, 15.99f, false);
        lek.setProductType(productTypeList.get(1));
        drugList.add(lek);
        lek = new Drug(4l, "Gripex", "USP Zdrowie", LocalDate.of(2011,11,11) , 16.99f, 15.99f, false);
        lek.setProductType(productTypeList.get(0));
        drugList.add(lek);
        lek = new Drug(5l, "Theraflu Extra Grip", "GLAXOSMiTHKLINE", LocalDate.of(2016,3,11) , 6.99f, 15.99f, false);
        lek.setProductType(productTypeList.get(1));
        drugList.add(lek);
        lek = new Drug(6l, "Cholinex", "GLAXOSMiTHKLINE", LocalDate.of(2016,3,11) , 13.99f, 15.99f, false);
        lek.setProductType(productTypeList.get(1));
        drugList.add(lek);
        lek = new Drug(7l, "Nurofen Forte", "RECKITt BENCKISER", LocalDate.of(2015,7,1) , 21.99f, 15.99f, false);
        lek.setProductType(productTypeList.get(2));
        drugList.add(lek);




    }
}
