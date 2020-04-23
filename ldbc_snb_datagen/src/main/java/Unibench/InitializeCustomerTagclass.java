package Unibench;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by chzhang on 04/03/2017.
 */
public class InitializeCustomerTagclass {
    //Input: person_has_interest_tag,tag_hasType_tagclass
    public ArrayList<Integer> SocialCommerce_TagClass = new ArrayList<Integer>();

    public void run() {
        Integer[] subClass = new Integer[]{19, 41, 45, 64, 101, 145, 168, 171, 179, 180, 182, 197, 231, 246, 346, 348, 313, 246};
        SocialCommerce_TagClass.addAll(Arrays.asList(subClass));
    }
}
