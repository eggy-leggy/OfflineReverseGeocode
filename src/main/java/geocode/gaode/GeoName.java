/*
The MIT License (MIT)
[OSI Approved License]
The MIT License (MIT)

Copyright (c) 2014 Daniel Glasson

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package geocode.gaode;

import geocode.kdtree.KDNodeComparator;

import java.util.Comparator;

import static java.lang.Math.*;

/**
 * Created by Daniel Glasson on 18/05/2014.
 * This class works with a placenames files from http://download.geonames.org/export/dump/
 */

public class GeoName extends KDNodeComparator<GeoName> {
    public String province;
    public String city;
    public String citycode;
    public String district;
    public String street;
    public double latitude; // 纬度
    public double longitude; // 经度
    public double point[] = new double[3]; // The 3D coordinates of the point
    public String country;

    /**
     * @param data 北京市东城区朝阳门办事处,中国,北京市,北京市,010,东城区,116.430444,39.921000
     * @return : null
     * @author : ChunqiangFan
     * @date : 2020/1/4 14:36
     */
    GeoName(String data) {
        String[] names = data.split(",");
        street = names[0];
        country = names[1];
        province = names[2];
        city = names[3];
        citycode = names[4];
        district = names[5];
        longitude = Double.parseDouble(names[6]);
        latitude = Double.parseDouble(names[7]);
        setPoint();
    }

    GeoName(Double latitude, Double longitude) {
        street = country = "Search";
        this.latitude = latitude;
        this.longitude = longitude;
        setPoint();
    }

    private void setPoint() {
        point[0] = cos(toRadians(latitude)) * cos(toRadians(longitude));
        point[1] = cos(toRadians(latitude)) * sin(toRadians(longitude));
        point[2] = sin(toRadians(latitude));
    }

    @Override
    public String toString() {
        return street;
    }

    @Override
    protected double squaredDistance(GeoName other) {
        double x = this.point[0] - other.point[0];
        double y = this.point[1] - other.point[1];
        double z = this.point[2] - other.point[2];
        return (x * x) + (y * y) + (z * z);
    }

    @Override
    protected double axisSquaredDistance(GeoName other, int axis) {
        double distance = point[axis] - other.point[axis];
        return distance * distance;
    }

    @Override
    protected Comparator<GeoName> getComparator(int axis) {
        return GeoNameComparator.values()[axis];
    }

    protected static enum GeoNameComparator implements Comparator<GeoName> {
        x {
            @Override
            public int compare(GeoName a, GeoName b) {
                return Double.compare(a.point[0], b.point[0]);
            }
        },
        y {
            @Override
            public int compare(GeoName a, GeoName b) {
                return Double.compare(a.point[1], b.point[1]);
            }
        },
        z {
            @Override
            public int compare(GeoName a, GeoName b) {
                return Double.compare(a.point[2], b.point[2]);
            }
        };
    }
}
