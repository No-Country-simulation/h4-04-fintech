package com.practice.Notices.repository;

import com.practice.Notices.model.NoticieModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<NoticieModel, Long> {
}
