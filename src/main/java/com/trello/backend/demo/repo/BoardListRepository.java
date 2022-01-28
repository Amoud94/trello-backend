package com.trello.backend.demo.repo;

import com.trello.backend.demo.entity.BoardList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface BoardListRepository extends JpaRepository<BoardList,String> {
    BoardList findByBoardListID(String boardListID);
    Collection<BoardList> findBoardListsByBoard_BoardID(String boardListId);
}
