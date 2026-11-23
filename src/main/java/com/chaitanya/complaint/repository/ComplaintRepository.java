package com.chaitanya.complaint.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.chaitanya.complaint.entity.Complaint;
import com.chaitanya.complaint.entity.ComplaintStatus;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    @Query("""
            select c from Complaint c left join c.assignedTo e
            where (:status is null or c.status = :status)
              and (:employeeId is null or e.id = :employeeId)
            order by c.createdAt desc, c.id desc""")
    List<Complaint> search(@Param("status") ComplaintStatus status, @Param("employeeId") Long employeeId);
}
