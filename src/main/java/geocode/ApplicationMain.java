package geocode;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

import geocode.gaode.ReverseGeoCode;
import geocode.gaode.GeoName;

/**
 * @author ChunqiangFan
 * @description:
 * @create 2019-12-30 17:27
 */

public class ApplicationMain {

    public static void main(String[] args) throws IOException {
        InputStream fileInputStream = ApplicationMain.class.getResourceAsStream("/result_format.zip");
        ReverseGeoCode reverseGeoCode = new ReverseGeoCode(new ZipInputStream(fileInputStream));
        GeoName address = reverseGeoCode.nearestPlace(31.0292542558,121.5570688248);
        System.out.println(address.country);
        System.out.println(address.citycode);
        System.out.println(address.street);
//        System.out.println("Nearest to 24.22085, 97.78682 is " + reverseGeoCode.nearestPlace(24.22085, 97.78682));
    }
}
