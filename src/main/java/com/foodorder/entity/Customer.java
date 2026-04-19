package com.foodorder.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "customers")
@PrimaryKeyJoinColumn(name = "customer_id")
public class Customer extends User {
    @Column(name = "reward_points")
    private int rewardPoints;

    public Customer() {
    }

    public Customer(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }
}
