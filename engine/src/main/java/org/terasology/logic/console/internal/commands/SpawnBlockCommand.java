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
package org.terasology.logic.console.internal.commands;

import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.console.dynamic.Command;
import org.terasology.logic.console.dynamic.CommandParameter;
import org.terasology.logic.inventory.PickupBuilder;
import org.terasology.registry.In;
import org.terasology.rendering.cameras.Camera;
import org.terasology.rendering.world.WorldRenderer;
import org.terasology.world.block.BlockManager;
import org.terasology.world.block.family.BlockFamily;
import org.terasology.world.block.items.BlockItemFactory;

import javax.vecmath.Vector3f;

/**
 * @author Immortius, Limeth
 */
// TODO: Fix this up for multiplayer (cannot at the moment due to the use of the camera), also applied required
// TODO: permission
@RegisterSystem
public class SpawnBlockCommand extends Command {
    @In
    private WorldRenderer worldRenderer;

    @In
    private BlockManager blockManager;

    @In
    private EntityManager entityManager;

    private PickupBuilder pickupBuilder;

    @Override
    public void initialiseMore() {
        pickupBuilder = new PickupBuilder(entityManager);
    }

    public SpawnBlockCommand() {
        super("spawnBlock", false, "Spawns a block in front of the player",
                "Spawns the specified block as an item in front of the player. You can simply pick it up.");
    }

    @Override
    protected CommandParameter[] constructParameters() {
        return new CommandParameter[] {
            CommandParameter.single("blockName", String.class, true)
        };
    }

    public String execute(EntityRef sender, String blockName) {
        Camera camera = worldRenderer.getActiveCamera();
        Vector3f spawnPos = camera.getPosition();
        Vector3f offset = camera.getViewingDirection();
        offset.scale(3);
        spawnPos.add(offset);

        BlockFamily block = blockManager.getBlockFamily(blockName);
        if (block == null) {
            return "";
        }

        BlockItemFactory blockItemFactory = new BlockItemFactory(entityManager);
        EntityRef blockItem = blockItemFactory.newInstance(block);

        pickupBuilder.createPickupFor(blockItem, spawnPos, 60);
        return "Spawned block.";
    }

    //TODO Add the suggestion method
}
