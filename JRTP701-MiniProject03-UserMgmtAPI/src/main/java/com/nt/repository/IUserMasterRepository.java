//Repository Interface
package com.nt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.entity.UserMaster;

public interface IUserMasterRepository extends JpaRepository<UserMaster, Integer> {
     public    UserMaster   findByEmailAndPassword(String mail,String pwd);
     public   UserMaster   findByNameAndEmail(String name, String  mail);
}
