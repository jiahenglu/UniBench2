package Unibench;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class Mapping_Vendor_Product {

    private static final String RepresentativesOfCompanies_filename = "/resources/dictionaries/RepresentativesOfCompanies";
    public static Map<String, String> Product_Vendor = new LinkedHashMap<>();
    public static LinkedList<Vendor> Vendors = new LinkedList<Vendor>();

    public static void CreateMapping() {
        try {
            BufferedReader dicreader = new BufferedReader(
                    new InputStreamReader(Dictionary.class.getResourceAsStream(RepresentativesOfCompanies_filename), "UTF-8"));
            String line = null;
            int cnt = 0;
            System.out.println(RepresentativesOfCompanies_filename);
            while ((line = dicreader.readLine()) != null) {
                String data[] = line.split("\\t");

                Vendor vd = new Vendor();
                vd.CompanyId = Long.parseLong(data[0]);
                vd.degree = Integer.parseInt(data[1]);
                vd.personId = Long.parseLong(data[2]);
                cnt += vd.degree;
                Vendors.add(vd);
            }
            dicreader.close();

            System.out.println("The size of PRODUCT_ID_OBJECT is " + Dictionary.PRODUCT_ID_OBJECT.keySet().size());
            System.out.println("The size of Vendors is" + Vendors.size());

            int venSize = Vendors.size();
            int proSize = Dictionary.PRODUCT_ID_OBJECT.keySet().size();

            // Create the mapping

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
