package com.bucketstore.api.auth.repository;

import com.bucketstore.api.auth.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
