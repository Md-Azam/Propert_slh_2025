package com.arshaa.repository;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.arshaa.entity.Image;

@Repository
public interface ImageRepository  extends JpaRepository<Image, Integer>{  
    Image findByGuestId(String guestId);
    
    boolean existsByGuestId(String guestId);
    
    @Transactional
     public void deleteByGuestId(String guestId);
}
