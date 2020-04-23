package Unibench;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Product_Dictionary {

    public static void loadDictionaries(String filename, String tag) {
        try {
            BufferedReader dicreader = new BufferedReader(
                    new InputStreamReader(java.util.Dictionary.class.getResourceAsStream(filename), "UTF-8"));
            dicreader.readLine(); // this will read the first line
            String line = null;
            int cnt = 0;
            System.out.println(filename);
            while ((line = dicreader.readLine()) != null) {
                // Change to Guava's Splitter later
                //System.out.println(++cnt);
                String data[] = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                String id = data[0];
                String title = data[1];
                double price = Double.parseDouble(data[2]);
                String imUrl = data[3];
                String salesRank = data[4];
                String categories = data[5];
                String description = data[6];
                String brand = data[7];
                String current_tag = tag;
                Product dm = new Product(id, title, price, imUrl, salesRank, categories, description, brand, current_tag);
                Unibench.Dictionary.PRODUCT_ID_OBJECT.put(id, dm);
            }
            dicreader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
