package pl.com.foks.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private String id;
    private String login;
    private String password;
    private Role role;

    public enum Role {
        ADMIN, USER
    }
}
