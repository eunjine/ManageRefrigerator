package com.android.managerefrigerator.data;

public class FoodInfo {
    public String foodName;
    public String foodDate;
    public String foodCount;
    public String foodImage;

    public FoodInfo(String foodName, String foodDate, String foodImage, String foodCount) {
        this.foodName = foodName;
        this.foodDate = foodDate;
        this.foodImage = foodImage;
        this.foodCount = foodCount;
    }

    @Override
    public String toString() {
        return "FoodInfo{" +
                "foodName='" + foodName + '\'' +
                ", foodDate='" + foodDate + '\'' +
                ", foodCount='" + foodCount + '\'' +
                ", foodImage='" + foodImage + '\'' +
                '}';
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodDate() {
        return foodDate;
    }

    public void setFoodDate(String foodDate) {
        this.foodDate = foodDate;
    }

    public String getFoodCount() {
        return foodCount;
    }

    public void setFoodCount(String foodCount) {
        this.foodCount = foodCount;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }
}
