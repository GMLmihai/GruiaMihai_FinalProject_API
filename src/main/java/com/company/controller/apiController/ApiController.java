package com.company.controller.apiController;

import com.auth0.jwt.interfaces.Claim;
import com.company.controller.database.MongoController;
import com.company.controller.hashers.HashAlgorithm;
import com.company.controller.hashers.Sha512;
import com.company.controller.token.TokenManager;
import com.company.exception.NotFoundException;
import com.company.exception.UnauthorizedException;
import com.company.model.admins.AddAdminRequestBody;
import com.company.model.admins.AddAdminResponse;
import com.company.model.getChatroomMessages.ChatroomMessageRequestBody;
import com.company.model.getChatroomMessages.ChatroomMessageResponse;
import com.company.model.deleteChatroom.DeleteChatroomRequest;
import com.company.model.deleteChatroom.DeleteChatroomResponse;
import com.company.model.login.LoginRequestBody;
import com.company.model.login.LoginResponse;
import com.company.model.postChatroom.GetChatroomsResponse;
import com.company.model.postChatroom.PostChatroomRequestBody;
import com.company.model.postChatroom.PostChatroomResponse;
import com.company.model.register.RegisterRequestBody;
import com.company.model.register.RegisterResponse;
import com.company.model.updateChatroomName.UpdateChatroomNameRequestBody;
import com.company.model.updateChatroomName.UpdateChatroomNameResponse;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
public class ApiController {


    private final HashAlgorithm hasher = new Sha512();

    @RequestMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequestBody body) {

        String hashedPassword = hasher.saltAndHash(body.getPassword());
        MongoController mc = new MongoController();
        mc.addUser(body.getUsername(), hashedPassword);
        Document result = mc.getUserWithUsername(body.getUsername());
        String insertedID = "";
        if (result != null) {
            insertedID = ((ObjectId) result.get("_id")).toString();
        }
        return new RegisterResponse(insertedID);

    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequestBody body) {

        MongoController mc = new MongoController();
        Document result = mc.getUserWithUsername(body.getUsername());
        if (result == null) {
            throw new UnauthorizedException();
        }
        String referencePassword = (String) result.get("password");
        boolean isPasswordValid = hasher.checkPassword(referencePassword, body.getPassword());
        if (!isPasswordValid) {
            throw new UnauthorizedException();
        }
        TokenManager tm = new TokenManager();
        String token = tm.generateToken(body.getUsername());
        return new LoginResponse(token);
    }

    @PostMapping("/chatrooms")
    public PostChatroomResponse addChatroom(@RequestHeader(name = "Authorization") String authHeader,
                                            @RequestBody PostChatroomRequestBody body) {
        TokenManager token = new TokenManager();
        Map<String, Claim> claims = token.verifyToken(authHeader);

        if (claims == null) {
            throw new UnauthorizedException();
        }
        MongoController mongo = new MongoController();
        String password = hasher.saltAndHash(body.getPassword());
        mongo.addChatroom(body.getName(), password, claims.get("username").asString());
        String insertedId = "";
        return new PostChatroomResponse("");
    }

    @PostMapping("/getChatroomMessages")
    public ChatroomMessageResponse getChatroomMessages(@RequestHeader(name = "Authorization") String authHeader,
                                                       @RequestBody ChatroomMessageRequestBody body) {

        TokenManager tm = new TokenManager();
        Map<String, Claim> claims = tm.verifyToken(authHeader);

        if (claims == null) {
            throw new UnauthorizedException();
        }

        MongoController mc = new MongoController();

        Document result = mc.getChatRoomWithName(body.getChatroomName());
        if (result == null) {
            throw new NotFoundException();
        }

        String referencePassword = (String) result.get("password");
        boolean isPasswordValid = hasher.checkPassword(referencePassword, body.getPassword());
        if (!isPasswordValid) {
            throw new UnauthorizedException();
        }

        ArrayList<Document> chatrooms = mc.getChatroomWithName(body.getChatroomName());
        ArrayList<Document> chatroomMessages = new ArrayList<>();
        for (Document chatroom : chatrooms) {
            List<Document> messages = chatroom.getList("messages", Document.class);
            chatroomMessages.addAll(messages);
        }
        return new ChatroomMessageResponse(chatroomMessages);
    }

    @GetMapping("/getAllChatrooms")
    public GetChatroomsResponse getChatrooms(@RequestHeader(name = "Authorization") String authHeader) {

        TokenManager tm = new TokenManager();
        Map<String, Claim> claims = tm.verifyToken(authHeader);
        if (claims == null) {
            throw new UnauthorizedException();
        }

        MongoController mc = new MongoController();
        ArrayList<Document> chatrooms = mc.getChatrooms();
        ArrayList<String> chatroomNames = new ArrayList<>();
        for (Document chatroom : chatrooms) {
            chatroomNames.add(chatroom.get("chatroomName").toString());
        }
        return new GetChatroomsResponse(chatroomNames);
    }


    @PostMapping("/addChatroomMessage")
    public void addMessages(@RequestHeader(name = "Authorization") String authHeader,
                            @RequestBody ChatroomMessageRequestBody body) {

        TokenManager tm = new TokenManager();
        Map<String, Claim> claims = tm.verifyToken(authHeader);

        if (claims == null) {
            throw new UnauthorizedException();
        }

        MongoController mc = new MongoController();
        Document result = mc.getChatRoomWithName(body.getChatroomName());
        if (result == null) {
            throw new NotFoundException();
        }
        String referencePassword = (String) result.get("password");
        boolean isPasswordValid = hasher.checkPassword(referencePassword, body.getPassword());
        if (!isPasswordValid) {
            throw new UnauthorizedException();
        }
        mc.addMessage(body.getChatroomName(), body.getMessage(), claims.get("username").asString());

    }

    @DeleteMapping("/deleteChatrooms")
    public DeleteChatroomResponse deleteChatrooms(@RequestHeader(name = "Authorization") String authHeader,
                                                  @RequestBody DeleteChatroomRequest body) {

        TokenManager tm = new TokenManager();
        Map<String, Claim> claims = tm.verifyToken(authHeader);
        if (claims == null) {
            throw new UnauthorizedException();
        }
        MongoController mc = new MongoController();
        Document result = mc.getChatRoomWithName(body.getChatroomName());
        if (result == null) {
            throw new NotFoundException();
        }
        String referencePassword = (String) result.get("password");
        boolean isPasswordValid = hasher.checkPassword(referencePassword, body.getPassword());
        if (!isPasswordValid) {
            throw new UnauthorizedException();
        }
        mc.deleteChatroom(body.getChatroomName());
        return new DeleteChatroomResponse(body.getChatroomName() + " deleted");
    }

    @PostMapping("/updateChatroomName")
    public UpdateChatroomNameResponse updateChatroomName(@RequestHeader(name = "Authorization") String authHeader,
                                                         @RequestBody UpdateChatroomNameRequestBody body) {
        TokenManager tm = new TokenManager();
        Map<String, Claim> claims = tm.verifyToken(authHeader);
        if (claims == null) {
            throw new UnauthorizedException();
        }
        MongoController mc = new MongoController();
        Document result = mc.getChatRoomWithName(body.getChatroomName());
        if (result == null) {
            throw new NotFoundException();
        }
        String referencePassword = (String) result.get("password");
        boolean isPasswordValid = hasher.checkPassword(referencePassword, body.getPassword());
        if (!isPasswordValid) {
            throw new UnauthorizedException();
        }
        mc.updateChatroomName(body.getChatroomName(), body.getNewChatroomName());
        return new UpdateChatroomNameResponse(body.getNewChatroomName());
    }

    @PostMapping("/addAdmin")
    public AddAdminResponse addAdmins(@RequestHeader(name = "Authorization") String authHeader,
                                      @RequestBody AddAdminRequestBody body) {

        TokenManager tm = new TokenManager();
        Map<String, Claim> claims = tm.verifyToken(authHeader);
        if (claims == null) {
            throw new UnauthorizedException();
        }
        MongoController mongoController = new MongoController();

        String username = claims.get("username").asString();

        if (!mongoController.isAdmin(username, body.getChatroomName())) {
            throw new UnauthorizedException();
        }
        mongoController.addAdmin(body.getChatroomName(), body.getAdminName());
        Document result = mongoController.getChatRoomWithName(body.getChatroomName());
        String insertedID = "";
        if (result != null) {
            insertedID = ((ObjectId) result.get("_id")).toString();
        }
        return new AddAdminResponse(insertedID);
    }

}