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
package org.terasology.logic.debug.commands;

import org.terasology.logic.characters.CharacterMovementComponent;
import org.terasology.logic.console.dynamic.Command;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.console.dynamic.CommandParameter;
import org.terasology.network.ClientComponent;

/**
 * @author Immortius, Limeth
 */
@RegisterSystem
public class SetJumpSpeedCommand extends Command {
    public SetJumpSpeedCommand() {
        super("setJumpSpeed", true, "Set jump speed", null);
    }

    @Override
    protected CommandParameter[] constructParameters() {
        return new CommandParameter[] {
                CommandParameter.array("amount", Float.class, true)
        };
    }

    public String execute(EntityRef sender, Float amount) {
        ClientComponent clientComp = sender.getComponent(ClientComponent.class);
        CharacterMovementComponent move = clientComp.character.getComponent(CharacterMovementComponent.class);
        if (move != null) {
            float oldSpeed = move.jumpSpeed;
            move.jumpSpeed = amount;
            clientComp.character.saveComponent(move);

            return "Jump speed set to " + amount + " (was " + oldSpeed + ")";
        }

        return "";
    }

    //TODO Implement the suggest method
}
