package com.example.client.api;


import android.graphics.pdf.PdfDocument;

import org.threeten.bp.LocalDateTime;

import java.util.List;

public class MappingClass {

    public class EmptyResponse{};
    public static class BusinessRequest {
        private String businessTitle;
        public String getBusinessTitle() { return businessTitle; }
        public void setBusinessTitle(String businessTitle) { this.businessTitle = businessTitle; }
    }

    public class BusinessResponse {
        private long businessId;
        private String businessTitle;
        private String createdAt;
        private String deletedAt;
        private List<LocationResponse> locations;
        private List<BusinessScoreResponse> businessScores;

        public long getBusinessId() {
            return businessId;
        }

        public void setBusinessId(long businessId) {
            this.businessId = businessId;
        }

        public String getBusinessTitle() {
            return businessTitle;
        }

        public void setBusinessTitle(String businessTitle) {
            this.businessTitle = businessTitle;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDeletedAt() {
            return deletedAt;
        }

        public void setDeletedAt(String deletedAt) {
            this.deletedAt = deletedAt;
        }

        public List<LocationResponse> getLocations() {
            return locations;
        }

        public void setLocations(List<LocationResponse> locations) {
            this.locations = locations;
        }

        public List<BusinessScoreResponse> getBusinessScores() {
            return businessScores;
        }

        public void setBusinessScores(List<BusinessScoreResponse> businessScores) {
            this.businessScores = businessScores;
        }
    }

    public class BusinessResponse2 {
        private Data data; // data 필드 추가

        public Data getData() {
            return data;
        }

        public class Data {
            private long businessId;
            private String businessTitle;
            private String createdAt;
            private Object deletedAt;
            private List<Object> locations;
            private Object businessScores;

            public long getBusinessId() {
                return businessId;
            }

            public String getBusinessTitle() {
                return businessTitle;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public Object getDeletedAt() {
                return deletedAt;
            }

            public List<Object> getLocations() {
                return locations;
            }

            public Object getBusinessScores() {
                return businessScores;
            }

            // Getter 메소드들
        }
    }

    public class TurbineResponse {
    }

    public static class BusinessScorePost {
        private long businessId;
        private String businessScoreTitle;
        private String observerName;
        private int scoreList1;
        private int scoreList2;
        private int scoreList3;
        private int scoreList4;

        public BusinessScorePost(long businessId, String businessScoreTitle, String observerName, int scoreList1, int scoreList2, int scoreList3, int scoreList4) {
            this.businessId = businessId;
            this.businessScoreTitle = businessScoreTitle;
            this.observerName = observerName;
            this.scoreList1 = scoreList1;
            this.scoreList2 = scoreList2;
            this.scoreList3 = scoreList3;
            this.scoreList4 = scoreList4;
        }

        public long getBusinessId() {
            return businessId;
        }

        public void setBusinessId(long businessId) {
            this.businessId = businessId;
        }

        public String getBusinessScoreTitle() {
            return businessScoreTitle;
        }

        public void setBusinessScoreTitle(String businessScoreTitle) {
            this.businessScoreTitle = businessScoreTitle;
        }

        public int getScoreList1() {
            return scoreList1;
        }

        public void setScoreList1(int scoreList1) {
            this.scoreList1 = scoreList1;
        }

        public int getScoreList2() {
            return scoreList2;
        }

        public void setScoreList2(int scoreList2) {
            this.scoreList2 = scoreList2;
        }

        public int getScoreList3() {
            return scoreList3;
        }

        public void setScoreList3(int scoreList3) {
            this.scoreList3 = scoreList3;
        }

        public int getScoreList4() {
            return scoreList4;
        }

        public void setScoreList4(int scoreList4) {
            this.scoreList4 = scoreList4;
        }

        public String getObserverName() {
            return observerName;
        }

        public void setObserverName(String observerName) {
            this.observerName = observerName;
        }
    }

    public class BusinessScoreResponsePage {
        private List<BusinessScoreResponse> data;
        private PageInfo pageInfo;

        public List<BusinessScoreResponse> getData() {
            return data;
        }

        public PageInfo getPageInfo() {
            return pageInfo;
        }
    }

    public static class PageInfo {
        int page;
        int size;
        int totalElements;
        int totalPages;

        public void setPageInfo(PageInfo pageInfo) {
            this.page = pageInfo.getPage();
            this.size = pageInfo.getSize();
            this.totalElements = pageInfo.getTotalElements();
            this.totalPages = pageInfo.getTotalPages();
        }

        public void setPageInfo(int page, int size, int totalElements, int totalPages) {
            this.page = page;
            this.size = size;
            this.totalElements = totalElements;
            this.totalPages = totalPages;
        }

        public int getPage() {
            return page;
        }

        public int getSize() {
            return size;
        }

        public int getTotalElements() {
            return totalElements;
        }

        public int getTotalPages() {
            return totalPages;
        }
    }

    public class BusinessScoreResponse  {
        private long businessId;
        private long businessScoreId;
        private String businessScoreTitle;
        private int scoreList1;
        private int scoreList2;
        private int scoreList3;
        private int scoreList4;
        private String observerName;
        private String createdAt;
        private String modifiedAt;

        public long getBusinessId() {
            return businessId;
        }

        public void setBusinessId(long businessId) {
            this.businessId = businessId;
        }

        public long getBusinessScoreId() {
            return businessScoreId;
        }

        public void setBusinessScoreId(long businessScoreId) {
            this.businessScoreId = businessScoreId;
        }

        public String getBusinessScoreTitle() {
            return businessScoreTitle;
        }

        public void setBusinessScoreTitle(String businessScoreTitle) {
            this.businessScoreTitle = businessScoreTitle;
        }

        public int getScoreList1() {
            return scoreList1;
        }

        public void setScoreList1(int scoreList1) {
            this.scoreList1 = scoreList1;
        }

        public int getScoreList2() {
            return scoreList2;
        }

        public void setScoreList2(int scoreList2) {
            this.scoreList2 = scoreList2;
        }

        public int getScoreList3() {
            return scoreList3;
        }

        public void setScoreList3(int scoreList3) {
            this.scoreList3 = scoreList3;
        }

        public int getScoreList4() {
            return scoreList4;
        }

        public void setScoreList4(int scoreList4) {
            this.scoreList4 = scoreList4;
        }

        public String getObserverName() {
            return observerName;
        }

        public void setObserverName(String observerName) {
            this.observerName = observerName;
        }

        public String getCreatedAt() {
            return createdAt;
        }
        public String getModifiedAt() {
            return modifiedAt;
        }
    }

    public static class LocationPostRequest {
        private long businessId;
        private long turbineId;
        private String latitude;
        private String longitude;

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }



        // Getters and Setters
        public long getBusinessId() {
            return businessId;
        }

        public void setBusinessId(long businessId) {
            this.businessId = businessId;
        }

        public long getTurbineId() {
            return turbineId;
        }

        public void setTurbineId(long turbineId) {
            this.turbineId = turbineId;
        }


    }

    public static class LocationPatchRequest {
        private long locationId;
        private long turbineId;
        private long businessId;
        private String latitude;
        private String longitude;
        private String city;
        private String island;

        public long getLocationId() {
            return locationId;
        }

        public void setLocationId(long locationId) {
            this.locationId = locationId;
        }

        public long getTurbineId() {
            return turbineId;
        }

        public void setTurbineId(long turbineId) {
            this.turbineId = turbineId;
        }

        public long getBusinessId() {
            return businessId;
        }

        public void setBusinessId(long businessId) {
            this.businessId = businessId;
        }


        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getIsland() {
            return island;
        }

        public void setIsland(String island) {
            this.island = island;
        }

    }


    public static class LocationResponse {
        private long businessId;
        private long turbineId;
        private long locationId;
        private String businessTitle;
        private String modelName;
        private String latitude;
        private String longitude;
        private String city;
        private String island;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;


        public long getBusinessId() {
            return businessId;
        }

        public void setBusinessId(long businessId) {
            this.businessId = businessId;
        }

        public long getTurbineId() {
            return turbineId;
        }

        public void setTurbineId(long turbineId) {
            this.turbineId = turbineId;
        }

        public long getLocationId() {
            return locationId;
        }

        public void setLocationId(long locationId) {
            this.locationId = locationId;
        }

        public String getBusinessTitle() {
            return businessTitle;
        }

        public void setBusinessTitle(String businessTitle) {
            this.businessTitle = businessTitle;
        }

        public String getModelName() {
            return modelName;
        }

        public void setModelName(String modelName) {
            this.modelName = modelName;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getIsland() {
            return island;
        }

        public void setIsland(String island) {
            this.island = island;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public LocalDateTime getModifiedAt() {
            return modifiedAt;
        }

        public void setModifiedAt(LocalDateTime modifiedAt) {
            this.modifiedAt = modifiedAt;
        }
    }

    public static class ElevationResponse {
        private double elevation;
        public double getElevation() {
            return elevation;
        }
    }

    public static class DeleteBusiness{
        private long businessId;

        public long getBusinessId() {
            return businessId;
        }

        public void setBusinessId(long businessId) {
            this.businessId = businessId;
        }
    }

    public static class DeleteLocationsByBusinessId{
        private long businessId;

        public long getBusinessId() {
            return businessId;
        }

        public void setBusinessId(long businessId) {
            this.businessId = businessId;
        }
    }

    public static class GetDD{
        private String latitude;
        private String longitude;

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }
    }

    public class DdResponse {
        private Data data;

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public class Data {
            private long locationId;
            private long businessId;

            public long getLocationId() {
                return locationId;
            }

            public void setLocationId(long locationId) {
                this.locationId = locationId;
            }

            public long getBusinessId() {
                return businessId;
            }

            public void setBusinessId(long businessId) {
                this.businessId = businessId;
            }
        }
    }
    public static class PatchLocation{
        private long locationId;

        private long turbineId;

        private long businessId;

        private String latitude;

        private String longitude;

        private String city;

        private String island;

        public long getLocationId() {
            return locationId;
        }

        public void setLocationId(long locationId) {
            this.locationId = locationId;
        }

        public long getTurbineId() {
            return turbineId;
        }

        public void setTurbineId(long turbineId) {
            this.turbineId = turbineId;
        }

        public long getBusinessId() {
            return businessId;
        }

        public void setBusinessId(long businessId) {
            this.businessId = businessId;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getIsland() {
            return island;
        }

        public void setIsland(String island) {
            this.island = island;
        }
    }

    public static class ErrorResponse {
        private int status;
        private String message;
        private List<Object> fieldErrors;
        private List<Object> violationErrors;


        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }
}
