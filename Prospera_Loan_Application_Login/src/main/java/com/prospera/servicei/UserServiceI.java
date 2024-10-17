package com.prospera.servicei;

import com.prospera.model.User;

public interface UserServiceI
{

	void changePassword(String username, String password);

	User checkdetails(String username, String password);

}
