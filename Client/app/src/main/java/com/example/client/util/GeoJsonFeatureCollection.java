package com.example.client.util;

import java.util.List;

public class GeoJsonFeatureCollection {
    public String type;
    public String name;
    public CRS crs;
    public List<GeoJsonFeature> features;

    public static class CRS {
        public String type;
        public Properties properties;

        public static class Properties {
            public String name;
        }
    }

    public static class GeoJsonFeature {
        public String type;
        public Properties properties;
        public GeoJsonGeometry geometry;

        public static class Properties {
            public int gid;
            public String lvb_nm;
            public String lvb_ennm;
            public String rm_cn;
        }
    }

    public static class GeoJsonGeometry {
        public String type;
        public List<List<List<List<Double>>>> coordinates; // MultiPolygon의 경우
    }
}

