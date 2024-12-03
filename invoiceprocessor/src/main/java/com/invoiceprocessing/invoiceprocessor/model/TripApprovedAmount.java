//package com.invoiceprocessing.invoiceprocessor.repository;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public class TripTableApprovedAmountRepository extends JpaRepository<>{
//
//}
package com.invoiceprocessing.invoiceprocessor.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "trip")
public class TripApprovedAmount {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRIP_ID", nullable = false)
    private Integer tripId;

    @Column(name = "APPROVED_AMOUNT")
    private BigDecimal approvedAmount;

    // Constructors
    public TripApprovedAmount() {
    }

    public TripApprovedAmount(Integer tripId, BigDecimal approvedAmount) {
        this.tripId = tripId;
        this.approvedAmount = approvedAmount;
    }

    // Getters and Setters
    public Integer getTripId() {
        return tripId;
    }

    public void setTripId(Integer tripId) {
        this.tripId = tripId;
    }

    public BigDecimal getApprovedAmount() {
        return approvedAmount;
    }

    public void setApprovedAmount(BigDecimal approvedAmount) {
        this.approvedAmount = approvedAmount;
    }

    @Override
    public String toString() {
        return "TripApprovedAmount{" +
                "tripId=" + tripId +
                ", approvedAmount=" + approvedAmount +
                '}';
    }
}
