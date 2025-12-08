package org.lms.Service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.lms.Dto.MeetingResponse;
import org.lms.Dto.UserResponseDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class MeetingService {
    private final String JITSI_APP_ID = "mydeploy1";
    private final String JITSI_APP_SECRET = "wEoG/Y5keRbH4yrjMe7UxWiBzqO8a8VRqY8cVR4oXro=";
    private final String JITSI_DOMAIN = "jit.shancloudservice.com";

    @Inject
    UserService userService;

    @Inject
    ModuleService moduleService;

    private String generateMeetingLink(String token, String roomId) {
        return String.format("https://%s/%s?jwt=%s", JITSI_DOMAIN, roomId, token);
    }
    private String generateJitsiAuthToken(UserResponseDto user, String roomId, boolean isModerator) throws Exception {
        try{
            long curr = System.currentTimeMillis();
            long exp = curr + 3600_000;
            Map<String,Object> userContext = new HashMap<>();
            userContext.put("name",user.firstName != null ? user.firstName : user.username);
            userContext.put("email",user.email);
            userContext.put("id",user.id.toString());
            userContext.put("affiliation", isModerator ? "owner":"member");
            Map<String, Object> context = new HashMap<>();
            context.put("user", userContext);

            // Convert app secret to bytes
            byte[] keyBytes = JITSI_APP_SECRET.getBytes(StandardCharsets.UTF_8);

            // Build JWT token for Jitsi
            String token = Jwts.builder()
                    .setHeaderParam("typ", "JWT")
                    .setAudience("jitsi")
                    .setIssuer(JITSI_APP_ID)
                    .setSubject(JITSI_DOMAIN)
                    .claim("room", roomId) // Use schedule ID as room name// true for TUTOR, false for STUDENT
                    .setExpiration(new Date(exp))
                    .claim("context", context)
                    .signWith(Keys.hmacShaKeyFor(keyBytes), SignatureAlgorithm.HS256)
                    .compact();

            return token;



        } catch (Exception e){
            throw new Exception("cannot create auth token");
        }
    }

    public MeetingResponse createMeeting(UUID moduleId) throws Exception {
        MeetingResponse res = new MeetingResponse();
        UserResponseDto ur = userService.getProfileFromToken();
        res.displayName = ur.username;
        res.roomName = moduleId.toString();
        res.email = ur.email;
        if (ur.role.equalsIgnoreCase("lecturer")) {
            if (moduleService.doesModulehasThisLecturer(moduleId, ur.id)) {
                res.token = generateJitsiAuthToken(ur, moduleId.toString(), true);
                res.role = "lecturer".toUpperCase();
                return res;

//                return generateMeetingLink(token, moduleId.toString());
            } else {
                throw new Exception("lecturer doesnt have this module");
            }
        } else if (ur.role.equalsIgnoreCase("student")) {
            if (moduleService.doesthismodulehaveStudent(moduleId, ur.id)) {
                res.token = generateJitsiAuthToken(ur, moduleId.toString(), false);
                res.role = "student".toUpperCase();
                return res;
            } else {
                throw new Exception("student doesnt have this module");

            }

        } else {
            throw new Exception("not suitable role");
        }

    }

}
