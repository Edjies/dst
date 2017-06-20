package com.haofeng.apps.dst.bean;

import com.haofeng.apps.dst.utils.http.UrlBuilder;

import java.util.ArrayList;

/**
 * Created by WIN10 on 2017/6/19.
 */

public class LongOrderParams {
    public String m_city_id = "";
    public String m_car_models = "";
    public String m_company_name = "";
    public String m_contact_name = "";
    public String m_contact_mobile = "";
    public String m_contact_email = "";
    public String m_es_take_time = "";

    public ArrayList<UrlBuilder.UrlParams> getParams() {
        ArrayList<UrlBuilder.UrlParams> params = new ArrayList<>();
        params.add(new UrlBuilder.UrlParams("city_id", m_city_id));
        params.add(new UrlBuilder.UrlParams("car_models", m_car_models));
        params.add(new UrlBuilder.UrlParams("company_name", m_company_name));
        params.add(new UrlBuilder.UrlParams("contact_name", m_contact_name));
        params.add(new UrlBuilder.UrlParams("contact_mobile", m_contact_mobile));
        params.add(new UrlBuilder.UrlParams("contact_email", m_contact_email));
        params.add(new UrlBuilder.UrlParams("es_take_time", m_es_take_time));
        return params;
    }

}
