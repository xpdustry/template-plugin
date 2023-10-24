/*
 * This file is part of TemplatePlugin. A template plugin for Mindustry to get you started quickly.
 *
 * MIT License
 *
 * Copyright (c) 2023 Xpdustry
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.xpdustry.template;

import arc.util.CommandHandler;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.meta.CommandMeta;
import fr.xpdustry.distributor.api.command.ArcCommandManager;
import fr.xpdustry.distributor.api.command.sender.CommandSender;
import fr.xpdustry.distributor.api.event.EventHandler;
import fr.xpdustry.distributor.api.plugin.AbstractMindustryPlugin;
import fr.xpdustry.distributor.api.plugin.PluginListener;
import fr.xpdustry.distributor.api.scheduler.MindustryTimeUnit;
import fr.xpdustry.distributor.api.scheduler.TaskHandler;
import mindustry.game.EventType;
import mindustry.gen.Groups;

/**
 * Template plugin.
 */
@SuppressWarnings("unused")
public final class TemplatePlugin extends AbstractMindustryPlugin {

    private final ArcCommandManager<CommandSender> clientCommandManager = ArcCommandManager.standard(this);

    @Override
    public void onInit() {
        this.getLogger().info("Bonjour");
        this.addListener(new PlayerListener());
    }

    @Override
    public void onClientCommandsRegistration(final CommandHandler handler) {
        this.clientCommandManager.initialize(handler);
        this.clientCommandManager.command(this.clientCommandManager
                .commandBuilder("echo")
                .meta(CommandMeta.DESCRIPTION, "Print something")
                .argument(StringArgument.of("message"))
                .handler(context -> {
                    final String message = context.get("message");
                    context.getSender().sendMessage(message);
                }));
    }

    @Override
    public void onExit() {
        this.getLogger().info("Au revoir");
    }

    public static final class PlayerListener implements PluginListener {

        @EventHandler
        public void onPlayerJoin(final EventType.PlayerJoin event) {
            event.player.sendMessage("Welcome to the server!");
        }

        @TaskHandler(interval = 5, unit = MindustryTimeUnit.MINUTES)
        public void onPlayerTask() {
            Groups.player.forEach(player -> {
                player.sendMessage("Don't forget to join our discord!");
            });
        }
    }
}
