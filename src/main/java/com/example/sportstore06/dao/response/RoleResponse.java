package com.example.sportstore06.dao.response;

import com.example.sportstore06.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {
    private int id;
    private String name;
    public RoleResponse (Role role)
    {
        this.id = role.getId();
        this.name = role.getName();
    }
}
