package main.dao.userinfo;

import main.model.entities.UserInfo;

public interface UserInfoDao {
	
	public UserInfo save(UserInfo userInfo);
	
	public UserInfo findUserInfoByUserId(Long userId);
}
