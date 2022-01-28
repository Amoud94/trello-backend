package com.trello.backend.demo.controller;


import com.trello.backend.demo.dto.AppUserDTO;
import com.trello.backend.demo.dto.BoardDTO;
import com.trello.backend.demo.dto.BoardListDTO;
import com.trello.backend.demo.dto.TaskDTO;
import com.trello.backend.demo.entity.AppUser;
import com.trello.backend.demo.entity.Board;
import com.trello.backend.demo.entity.BoardList;
import com.trello.backend.demo.model.request.*;
import com.trello.backend.demo.service.AuthenticationServices;
import com.trello.backend.demo.service.TrelloServices;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class Controller {

    @Autowired
    private TrelloServices trelloServices;
    @Autowired
    private AuthenticationServices authenticationServices;

    @GetMapping("/hello")
    public ResponseEntity<Map<String,String>> hello(){
        Map<String, Object> response = new HashMap<>();
        response.put("client", "data");
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/Users/{id}/Boards")
    public ResponseEntity<Map<String,Collection<Board>>> getUserBoards(@PathVariable(name = "id") String userId){
        Map<String, Collection<Board>> response = new HashMap();
        Collection<Board> boards = trelloServices.FindBoardsByUser(userId);
        response.put("ReturnedData", boards);
        return new ResponseEntity(response,HttpStatus.OK);
    }

    @PostMapping("/Users/{id}/Boards")
    public ResponseEntity<Map<String,Object>> createNewBoard(@PathVariable(name = "id") String userId, @RequestBody AddBoard boardRequestModel) throws Exception {
        Map<String, Object> response = new HashMap<>();
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        BoardDTO board = mapper.map(boardRequestModel , BoardDTO.class);
        board.setAppUser_ID(userId);
        response.put("ReturnedData",trelloServices.CreateNewBoard(board));
        return new ResponseEntity(response,HttpStatus.OK);
    }

    @DeleteMapping("/Users/Boards/{id}")
    public ResponseEntity removeUserBoard(@PathVariable(value = "id") String BoardID){
        if(trelloServices.DeleteUserBoard(BoardID)) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/Boards/{id}/Lists")
    public ResponseEntity<Map<String, Collection<BoardList>>> getBoardLists(@PathVariable(value = "id") String BoardID){
        Map<String, Collection<BoardList>> response = new HashMap();
        response.put("ReturnedData" ,trelloServices.FindListsByBoard(BoardID));
        return new ResponseEntity(response,HttpStatus.OK);
    }

    @PostMapping("/Boards/{id}/Lists")
    public ResponseEntity<Map<String, Object>> createBoardList(@PathVariable(value = "id") String BoardID, @RequestBody AddBoardList boardListModel){
        Map<String, Object> response = new HashMap<>();
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        BoardListDTO boardList = mapper.map(boardListModel , BoardListDTO.class);
        boardList.setBoard_ID(BoardID);
        response.put("ReturnedData", trelloServices.CreateNewBoardList(boardList));
        return new ResponseEntity(response,HttpStatus.OK);
    }

    @DeleteMapping("/Boards/Lists/{id}")
    public ResponseEntity removeBoardList(@PathVariable(value = "id") String ListID){
        if(trelloServices.DeleteBoardList(ListID)) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/Lists/{id}/Tasks")
    public ResponseEntity<Map<String,Object>> createListTask(@PathVariable(value = "id") String ListID, @RequestBody AddTask taskRequestModel){
        Map<String ,Object> response = new HashMap<>();
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        TaskDTO task = mapper.map(taskRequestModel , TaskDTO.class);
        task.setList_ID(ListID);
        response.put("ReturnedData" , trelloServices.CreateNewTask(task));
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping("/Task")
    public ResponseEntity<Map<String,Object>> createNewTask(@RequestBody AddTask taskRequestModel){
        Map<String ,Object> response = new HashMap<>();
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        TaskDTO task = mapper.map(taskRequestModel , TaskDTO.class);
        response.put("ReturnedData" , trelloServices.CreateNewTask(task));
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PutMapping("/Task/{id}")
    public ResponseEntity<Map<String,Object>> updateTaskStatus(@PathVariable(name = "id") String ListID, @RequestBody UpdateTaskStatus taskModel){
        Map<String, Object> response = new HashMap<>();
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        TaskDTO task = mapper.map(taskModel , TaskDTO.class);
        response.put("ReturnedData" , trelloServices.UpdateTaskStatus(task , ListID));
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String,Object>> register(@Valid @RequestBody UserRegistration registrationRequestModel) throws Exception {
        Map<String,Object> response = new HashMap<>();
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        AppUserDTO appUserDTO = mapper.map(registrationRequestModel, AppUserDTO.class);
        AppUser appUser =   authenticationServices.saveUser(appUserDTO);
        response.put("ReturnedData", appUser);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity logoutUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(null);
        return ResponseEntity.ok("logout successful");
    }
}
