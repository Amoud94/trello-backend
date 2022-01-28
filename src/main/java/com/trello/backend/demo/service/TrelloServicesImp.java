package com.trello.backend.demo.service;

import com.trello.backend.demo.dto.BoardDTO;
import com.trello.backend.demo.dto.BoardListDTO;
import com.trello.backend.demo.dto.TaskDTO;
import com.trello.backend.demo.entity.AppUser;
import com.trello.backend.demo.entity.Board;
import com.trello.backend.demo.entity.BoardList;
import com.trello.backend.demo.entity.Task;
import com.trello.backend.demo.repo.AppUserRepository;
import com.trello.backend.demo.repo.BoardListRepository;
import com.trello.backend.demo.repo.BoardRepository;
import com.trello.backend.demo.repo.TaskRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Service
public class TrelloServicesImp implements TrelloServices {

    BoardRepository boardRepository;
    AppUserRepository appUserRepository;
    BoardListRepository boardListRepository;
    TaskRepository taskRepository;

    @Autowired
    public TrelloServicesImp(AppUserRepository appUserRepository,
                             BoardRepository boardRepository,
                             BoardListRepository boardListRepository,
                             TaskRepository taskRepository) {
        this.appUserRepository = appUserRepository;
        this.boardListRepository = boardListRepository;
        this.boardRepository = boardRepository;
        this.taskRepository = taskRepository;

    }

    @Override
    public Board CreateNewBoard(BoardDTO boardDTO) throws Exception {
        try{
            AppUser appUser = appUserRepository.findByAppUserID(boardDTO.getAppUser_ID());
            boardDTO.setBoardID(UUID.randomUUID().toString());
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            Board board = modelMapper.map(boardDTO , Board.class);
            board.setAppUser(appUser);
            Board data = boardRepository.save(board);
            return data;
        }catch( Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public BoardList CreateNewBoardList(BoardListDTO listDTO) {
        listDTO.setBoardListID(UUID.randomUUID().toString());
        Board board = boardRepository.findByBoardID(listDTO.getBoard_ID());
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        BoardList boardList = modelMapper.map(listDTO , BoardList.class);
        boardList.setBoard(board);
        BoardList data = boardListRepository.save(boardList);
        return data;
    }

    @Override
    public Task CreateNewTask(TaskDTO taskDTO) {
        taskDTO.setTaskID(UUID.randomUUID().toString());
        BoardList list = boardListRepository.findByBoardListID(taskDTO.getList_ID());
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Task task = modelMapper.map(taskDTO, Task.class);
        task.setBoardList(list);
        Task data = taskRepository.save(task);
        return data;
    }

    @Override
    public Task UpdateTaskStatus(TaskDTO taskDTO, String status) {
        BoardList list = boardListRepository.findByBoardListID(status);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Task task = modelMapper.map(taskDTO, Task.class);
        Task updateTask = taskRepository.findByTaskID(task.getTaskID());
        updateTask.setBoardList(list);
        updateTask.setStatus(list.getName());
        return taskRepository.save(updateTask);
    }

    @Override
    public Board GetBoard(String boardId) {
        Board board = boardRepository.findByBoardID(boardId);
        return board;
    }

    @Override
    public Boolean DeleteBoardList(String boardListID) {
        BoardList list = boardListRepository.findByBoardListID(boardListID);
        if(list != null) {
            boardListRepository.delete(list);
            return true;
        }
        return false;
    }

    @Override
    public Collection<Board> FindBoardsByUser(String appUserId) {
        AppUser appUser = appUserRepository.findByAppUserID(appUserId);
        Collection<Board> boardCollection = new ArrayList<>();
        if(appUser != null ) boardCollection = boardRepository.findBoardsByAppUser_AppUserID(appUserId);
        return boardCollection;
    }

    @Override
    public Collection<BoardList> FindListsByBoard(String boardID) {
        Board board = boardRepository.findByBoardID(boardID);
        Collection<BoardList> lists = new ArrayList<>();
        if(board != null) lists = boardListRepository.findBoardListsByBoard_BoardID(boardID);
        return lists;
    }

    @Override
    public Boolean DeleteUserBoard(String boardID) {
        Board board = boardRepository.findByBoardID(boardID);
        if(board != null){
            boardRepository.delete(board);
            return true;
        }
        return false;
    }
}
