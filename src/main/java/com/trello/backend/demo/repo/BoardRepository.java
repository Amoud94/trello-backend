package com.trello.backend.demo.repo;

import com.trello.backend.demo.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface BoardRepository extends JpaRepository<Board, String> {
    Collection<Board> findBoardsByAppUser_AppUserID(String appUserID);
    Board findByBoardID(String boardID);

}