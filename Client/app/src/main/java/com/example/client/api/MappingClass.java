package com.example.client.api;

import java.time.LocalDateTime;

public class MappingClass {
    public class BusinessRequest {
        private String businessName;
    }


    public class BusinessResponse {
        private long businessId;
        private String businessName;



        public BusinessResponse(long businessId, String businessName) {
            this.businessId = businessId;
            this.businessName = businessName;
        }

        public String getBusinessName() {
            return businessName;
        }
        public long getBusinessId() {
            return businessId;
        }

    }

    public class TurbineResponse {
        private long turbineId;
        private String korName;
        private String engName;
        private long serialNumber;
    }

    public class BusinessScoreResponse {
        private long businessScoreId;

    }

    public class BusinessScoreRequest {
        private long businessId;
        private String observerName;
        private int score1 = 1;
        private int score2 = 1;
        private int score3 = 1;
        private int score4 = 1;
        private int score5 = 1;

        private LocalDateTime createdAt;

    }
}
