package proj21_funding.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import proj21_funding.dto.account.UserInfo;
import proj21_funding.dto.account.UserLogin;

@Component
public interface UserInfoMapper {

	List<UserInfo> selectUserAll();

	UserInfo selectUserbyNo(int userNo);

	UserInfo selectUserbyId(String userId);
	
	UserInfo selectUserbyNickname(String nickname);
	
	UserInfo selectUserbyEmail(String email);

	UserInfo selectUserbylogin(UserLogin userLogin);

	UserInfo selectUserbySearchId(UserInfo userInfo);

	//	계좌 확인
	UserInfo showBankAccount(int userNo);

	int insertUser(UserInfo userInfo);

	int updateUser(UserInfo userInfo);

	int updateUserPw(UserInfo userInfo);

	int deleteUser(UserInfo userInfo);

}
