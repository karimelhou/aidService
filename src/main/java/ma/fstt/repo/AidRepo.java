package ma.fstt.repo;

import ma.fstt.entity.AidEntity;
import ma.fstt.entity.TypeAid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AidRepo extends JpaRepository<AidEntity, Long> {
    @Query("SELECT a FROM AidEntity a WHERE a.userId = :userId")
    List<AidEntity> findByUserId(@Param("userId") Long userId);

    @Query("SELECT a FROM AidEntity a WHERE a.type = :type")
    List<AidEntity> findByType(@Param("type") TypeAid userId);



}
