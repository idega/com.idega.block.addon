package com.idega.restful.business;

import java.io.Serializable;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.google.gson.Gson;
import com.idega.core.business.DefaultSpringBean;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.util.StringUtil;

public abstract class DefaultRestfulService extends DefaultSpringBean {

	protected Response getResponse(Response.Status status, Serializable message) {
		ResponseBuilder responseBuilder = Response.status(status.getStatusCode());
		Response response = responseBuilder.entity(getJSON(message)).build();
		return response;
	}

	protected Response getOKResponse(Serializable message) {
		return getResponse(Response.Status.OK, message);
	}

    protected String getJSON(Serializable object) {
		Gson gson = new Gson();
		return gson.toJson(object);
	}

    protected User getUser(String userId) {
    	if (StringUtil.isEmpty(userId))
    		return null;

    	User user = null;
    	UserBusiness userBusiness = getServiceInstance(UserBusiness.class);
    	try {
			user = userBusiness.getUser(userId);
		} catch (Exception e) {}

    	if (user == null) {
    		try {
    			user = userBusiness.getUser(Integer.valueOf(userId));
    		} catch (Exception e) {}
    	}

    	if (user == null)
    		getLogger().warning("Error getting user by ID: " + userId);

    	return user;
	}

}