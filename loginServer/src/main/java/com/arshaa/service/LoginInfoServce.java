package com.arshaa.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.arshaa.entity.LoginInfo;
import com.arshaa.entity.User;
import com.arshaa.repository.LoginInfoRepository;


@Service
public class LoginInfoServce {

    @Autowired
    private LoginInfoRepository loginInfoRepository;

    public LoginInfo saveLoginDetails(LoginInfo info) {
        try {
            return loginInfoRepository.save(info);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage() + "no user found");
        }
    }

    public List<LoginInfo> getAllPays(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable p = PageRequest.of(pageNumber, pageSize, sort);
        Page<LoginInfo> pagePost = this.loginInfoRepository.findAll(p);
        List<LoginInfo> allPosts = pagePost.getContent();
        List<LoginInfo> thist = new ArrayList<>();
        pagePost.forEach(h -> {
            LoginInfo lf = new LoginInfo();
            lf.setUserName(h.getUserName());
            lf.setUserPhoneNumber(h.getUserPhoneNumber());
        lf.setBuildingId(h.getBuildingId());
        lf.setId(h.getId());
            lf.setEmail(h.getEmail());
            lf.setUserType(h.getUserType());
            lf.setUserLoggedinDate(h.getUserLoggedinDate());

            thist.add(lf);
        });

        return thist;
    }
    
    
    public List<LoginInfo> getAllDataOfLogs(String key) {
        List<LoginInfo> comm = loginInfoRepository.findAll(Sort.by(Sort.Direction.DESC, key));

        List<LoginInfo> gdto = new ArrayList<>();
        comm.forEach(s -> {
            LoginInfo c = new LoginInfo();
            c.setBuildingId(s.getBuildingId());
c.setEmail(s.getEmail());
c.setId(s.getId());
c.setUserLoggedinDate(s.getUserLoggedinDate());
c.setUserName(s.getUserName());
c.setUserPhoneNumber(s.getUserPhoneNumber());
c.setUserType(s.getUserType());
            gdto.add(c);
        });
        return gdto;
    }


}
