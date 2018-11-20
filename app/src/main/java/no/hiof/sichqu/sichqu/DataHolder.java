package no.hiof.sichqu.sichqu;

import java.util.ArrayList;
import java.util.List;

import no.hiof.sichqu.sichqu.Products.Products;

public class DataHolder {
    final List<Products> currentProducts = new ArrayList<>();

    private DataHolder() {}

    static DataHolder getInstance() {
        if( instance == null ) {
            instance = new DataHolder();
        }
        return instance;
    }

    private static DataHolder instance;
}
