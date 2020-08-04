package main.dao.userinfo;

import org.springframework.stereotype.Service;
import main.model.entities.UserInfo;

@Service
public class UserInfoDaoImpl implements UserInfoDao{
	private final UserInfoRepo userInfoRepo;
	
	public UserInfoDaoImpl(UserInfoRepo userInfoRepo) {
		this.userInfoRepo = userInfoRepo;
	}

	@Override
	public UserInfo save(UserInfo userInfo) {
		return this.userInfoRepo.save(userInfo);
	}

	@Override
	public UserInfo findUserInfoByUserId(Long userId) {
		return this.userInfoRepo.findByUserId(userId);
	}
	
}
