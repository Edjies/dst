package com.haofeng.apps.dst;

import com.haofeng.apps.dst.bean.Car;
import com.haofeng.apps.dst.bean.CitySite;

import java.util.ArrayList;

/**
 * Created by WIN10 on 2017/6/20.
 */

public class DataCache {
    private static DataCache cache = new DataCache();

    private ArrayList<Car> mLongRentCars;

    private ArrayList<CitySite> mCitys;

    public static DataCache getInstance() {
        return cache;
    }

    public ArrayList<Car> getLongRentCars() {
        return mLongRentCars;
    }

    public ArrayList<CitySite> getCitySite() {
        return mCitys;
    }

    public void setLongRentCars(ArrayList<Car> car) {
        this.mLongRentCars = car;
    }

    public void setCity(ArrayList<CitySite> city) {
        this.mCitys = city;
    }

}
