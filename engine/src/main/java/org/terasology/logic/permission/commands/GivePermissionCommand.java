/*
 * Copyright 2014 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.logic.permission.commands;

import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.common.DisplayNameComponent;
import org.terasology.logic.console.dynamic.Command;
import org.terasology.logic.console.dynamic.CommandParameter;
import org.terasology.logic.permission.PermissionManager;
import org.terasology.network.ClientComponent;
import org.terasology.registry.In;

/**
 * @author Limeth
 */
@RegisterSystem
public class GivePermissionCommand extends Command {
    @In
    private PermissionManager permissionManager;
    @In
    private EntityManager entityManager;

    public GivePermissionCommand() {
        super("givePermission", true, "Gives specified permission to player",
                "Gives specified permission to player");
    }

    @Override
    protected CommandParameter[] constructParameters() {
        return new CommandParameter[] {
                CommandParameter.single("player", String.class, true),
                CommandParameter.single("permission", String.class, true)
        };
    }

    public String execute(EntityRef sender, String player, String permission) {
        boolean permissionGiven = false;

        for (EntityRef client : entityManager.getEntitiesWith(ClientComponent.class)) {
            ClientComponent clientComponent = client.getComponent(ClientComponent.class);
            if (clientHasName(clientComponent, player)) {
                permissionManager.addPermission(clientComponent.character, permission);
                permissionGiven = true;
            }
        }

        if (permissionGiven) {
            return "Permission " + permission + " added to player " + player;
        } else {
            return "Unable to find player " + player;
        }
    }

    private boolean clientHasName(ClientComponent client, String playerName) {
        EntityRef clientInfo = client.clientInfo;
        if (clientInfo != null) {
            String name = clientInfo.getComponent(DisplayNameComponent.class).name;
            return playerName.equals(name);
        }
        return false;
    }

    //TODO Implement the suggest method
}
