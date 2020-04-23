package Unibench;

import java.util.LinkedHashMap;
import java.util.Map;

public class Dictionary {

    private static final String meta_Automotive_filename = "/resources/dictionaries/meta_Automotive.csv";
    private static final String meta_Baby_filename = "/resources/dictionaries/meta_Baby.csv";
    private static final String meta_Beauty_filename = "/resources/dictionaries/meta_Beauty.csv";
    private static final String meta_Books_filename = "/resources/dictionaries/meta_Books.csv";
    private static final String meta_Clothing_Shoes_and_Jewelry_filename = "/resources/dictionaries/meta_Clothing_Shoes_and_Jewelry.csv";
    private static final String meta_Digital_Music_filename = "/resources/dictionaries/meta_Digital_Music.csv";
    private static final String meta_Electronics_filename = "/resources/dictionaries/meta_Electronics.csv";
    private static final String meta_Grocery_and_Gourmet_Food_filename = "/resources/dictionaries/meta_Grocery_and_Gourmet_Food.csv";
    private static final String meta_Health_and_Personal_Care_filename = "/resources/dictionaries/meta_Health_and_Personal_Care.csv";
    private static final String meta_Home_and_Kitchen_filename = "/resources/dictionaries/meta_Home_and_Kitchen.csv";
    private static final String meta_Musical_Instruments_filename = "/resources/dictionaries/meta_Musical_Instruments.csv";
    private static final String meta_Office_Products_filename = "/resources/dictionaries/meta_Office_Products.csv";
    private static final String meta_Patio_Lawn_and_Garden_filename = "/resources/dictionaries/meta_Patio_Lawn_and_Garden.csv";
    private static final String meta_Pet_Supplies_filename = "/resources/dictionaries/meta_Pet_Supplies.csv";
    private static final String meta_Sports_and_Outdoors_filename = "/resources/dictionaries/meta_Sports_and_Outdoors.csv";
    private static final String meta_Tools_and_Home_Improvement_filename = "/resources/dictionaries/meta_Tools_and_Home_Improvement.csv";


    public static Map<String, Product> PRODUCT_ID_OBJECT = new LinkedHashMap<>();

    public static void loadDictionaries() {
        try {
            Product_Dictionary.loadDictionaries(meta_Automotive_filename, "Automotive");
            Product_Dictionary.loadDictionaries(meta_Baby_filename, "Baby");
            Product_Dictionary.loadDictionaries(meta_Beauty_filename, "Beauty");
            Product_Dictionary.loadDictionaries(meta_Books_filename, "Books");
            Product_Dictionary.loadDictionaries(meta_Clothing_Shoes_and_Jewelry_filename, "Clothing_Shoes_and_Jewelry");
            Product_Dictionary.loadDictionaries(meta_Digital_Music_filename, "music");
            Product_Dictionary.loadDictionaries(meta_Electronics_filename, "Electronics");
            Product_Dictionary.loadDictionaries(meta_Grocery_and_Gourmet_Food_filename, "Grocery_and_Gourmet_Food");
            Product_Dictionary.loadDictionaries(meta_Health_and_Personal_Care_filename, "Health_and_Personal_Care");
            Product_Dictionary.loadDictionaries(meta_Home_and_Kitchen_filename, "Home_and_Kitchen");
            Product_Dictionary.loadDictionaries(meta_Musical_Instruments_filename, "Musical_Instruments");
            Product_Dictionary.loadDictionaries(meta_Office_Products_filename, "Office_Products");
            Product_Dictionary.loadDictionaries(meta_Patio_Lawn_and_Garden_filename, "Patio_Lawn_and_Garden");
            Product_Dictionary.loadDictionaries(meta_Pet_Supplies_filename, "Pet_Supplies");
            Product_Dictionary.loadDictionaries(meta_Sports_and_Outdoors_filename, "Sports_and_Outdoors");
            Product_Dictionary.loadDictionaries(meta_Tools_and_Home_Improvement_filename, "Tools_and_Home_Improvement");
            //meta_Books_Dic.loadDictionaries(meta_Books_filename);
            String tag = null;
            for (Map.Entry<String, Product> entry : PRODUCT_ID_OBJECT.entrySet()) {
                String key = entry.getKey();
                Product value = entry.getValue();

                if (tag != value.tag) {
                    tag = value.tag;
                    //System.out.println(value.tag);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
	

