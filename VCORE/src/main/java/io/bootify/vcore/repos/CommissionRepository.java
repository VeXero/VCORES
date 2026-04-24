package io.bootify.vcore.repos;

import io.bootify.vcore.domain.CommissionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommissionRepository extends JpaRepository<CommissionRequest, Long> {
}

