package com.csv.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.csv.file.model.Member;
import com.csv.file.model.MemberId;

@Repository
public interface MemberRepository  extends JpaRepository<Member, MemberId>{


}
