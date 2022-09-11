package com.bl.fundoouser.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bl.fundoouser.model.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long>{
	Optional<UserModel> findByEmailId(String emailId);

}
