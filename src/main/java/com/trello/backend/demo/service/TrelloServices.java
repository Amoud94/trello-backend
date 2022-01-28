package com.trello.backend.demo.service;

import com.trello.backend.demo.dto.BoardDTO;
import com.trello.backend.demo.dto.BoardListDTO;
import com.trello.backend.demo.dto.TaskDTO;
import com.trello.backend.demo.entity.Board;
import com.trello.backend.demo.entity.BoardList;
import com.trello.backend.demo.entity.Task;

import java.util.Collection;

public interface TrelloServices {

    Board CreateNewBoard(BoardDTO board) throws Exception;
    Board GetBoard(String boardId);
    BoardList CreateNewBoardList(BoardListDTO list);
    Boolean DeleteBoardList(String boardListID);
    Task CreateNewTask(TaskDTO task);
    Task UpdateTaskStatus(TaskDTO task, String status);

    Collection<Board> FindBoardsByUser(String appUserId);
    Collection<BoardList> FindListsByBoard(String boardID);
    Boolean DeleteUserBoard(String boardID);






}
