package utils;



import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.DataProvider;
import pojo.Product;

import java.io.File;
import java.util.List;

public class ProductDataProvider {

    private static Object[][] convertTo2DArray(List<Product> products) {
        Object[][] data = new Object[products.size()][1];
        for (int i = 0; i < products.size(); i++) {
            data[i][0] = products.get(i);
        }
        return data;
    }

    @DataProvider(name = "positiveProductData")
    public Object[][] providePositiveProductData() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<Product> products = mapper.readValue(
                new File("src/test/resources/testData/products_positive.json"),
                new TypeReference<List<Product>>() {}
        );
        return convertTo2DArray(products);
    }

    @DataProvider(name = "negativeProductData")
    public Object[][] provideNegativeProductData() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<Product> products = mapper.readValue(
                new File("src/test/resources/testData/products_negative.json"),
                new TypeReference<List<Product>>() {}
        );
        return convertTo2DArray(products);
    }
}
