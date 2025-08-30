package com.arshaa.cronService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arshaa.entity.CronNotification;
import com.arshaa.repository.CronRepository;

@Service
public class CronServices {
    
    @Autowired
    private CronRepository cronRepository ;
    
    public CronNotification saveCronNotification(CronNotification notify) throws Exception {
        try {
           
           return  cronRepository.save(notify);
        }
        catch(Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    
    public void deleteAll()
    {
        if(cronRepository!=null)
        cronRepository.deleteAll();
    }

}
