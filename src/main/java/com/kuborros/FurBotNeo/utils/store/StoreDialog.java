/*
 * Copyright 2016-2018 John Grosh (jagrosh) & Kaidan Gustave (TheMonitorLizard)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kuborros.FurBotNeo.utils.store;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Menu;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.internal.utils.Checks;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A {@link Menu Menu} implementation that creates
 * a listed display of text choices horizontally that users can scroll through
 * using reactions and make selections. Results can span multiple pages
 *
 * @author John Grosh
 * @author Kuborros
 */
public class StoreDialog extends Menu {
    public static final String UP = "\uD83D\uDD3C";
    public static final String DOWN = "\uD83D\uDD3D";
    public static final String SELECT = "\u2705";
    public static final String CANCEL = "\u274E";
    public static final String BIG_LEFT = "\u23EA";
    public static final String LEFT = "\u25C0";
    public static final String RIGHT = "\u25B6";
    public static final String BIG_RIGHT = "\u23E9";
    private final List<ShopItem> choices;
    private final String leftEnd, rightEnd;
    private final int itemsPerPage;
    private final boolean showPageNumbers;
    private final boolean numberItems;
    private final int pages;
    private final boolean wrapPageEnds;
    private final String defaultLeft, defaultRight;
    private final BiFunction<Integer, Integer, Color> color;
    private final boolean loop;
    private final BiFunction<Integer, Integer, String> text;
    private final BiConsumer<Message, Integer> success;
    private final Consumer<Message> cancel;
    int bulkSkipNumber = 2;
    boolean noUpdate;

    StoreDialog(EventWaiter waiter, Set<User> users, Set<Role> roles, long timeout, TimeUnit unit,
                List<ShopItem> choices, String leftEnd, String rightEnd, int itemsPerPage, boolean showPageNumbers,
                boolean numberItems, String defaultLeft, String defaultRight,
                BiFunction<Integer, Integer, Color> color, boolean loop, BiConsumer<Message, Integer> success,
                Consumer<Message> cancel, BiFunction<Integer, Integer, String> text) {
        super(waiter, users, roles, timeout, unit);
        this.choices = choices;
        this.leftEnd = leftEnd;
        this.rightEnd = rightEnd;
        this.itemsPerPage = itemsPerPage;
        this.showPageNumbers = showPageNumbers;
        this.numberItems = numberItems;
        this.pages = (int) Math.ceil((double) choices.size() / itemsPerPage);
        this.wrapPageEnds = true;
        this.defaultLeft = defaultLeft;
        this.defaultRight = defaultRight;
        this.color = color;
        this.loop = loop;
        this.success = success;
        this.cancel = cancel;
        this.text = text;
    }

    /**
     * Shows the SelectionDialog as a new {@link Message Message}
     * in the provided {@link MessageChannel MessageChannel}, starting with
     * the first selection.
     *
     * @param channel The MessageChannel to send the new Message to
     */
    @Override
    public void display(MessageChannel channel) {
        showDialog(channel, 1, 1);
    }

    /**
     * Displays this SelectionDialog by editing the provided
     * {@link Message Message}, starting with the first selection.
     *
     * @param message The Message to display the Menu in
     */
    @Override
    public void display(Message message) {
        showDialog(message, 1, 1);
    }

    /**
     * Shows the SelectionDialog as a new {@link Message Message}
     * in the provided {@link MessageChannel MessageChannel}, starting with
     * the number selection provided.
     *
     * @param channel   The MessageChannel to send the new Message to
     * @param selection The number selection to start on
     */
    public void showDialog(MessageChannel channel, int selection, int pageNum) {
        if (selection < 1)
            selection = 1;
        else if (selection > choices.size())
            selection = choices.size();
        Message msg = renderPage(pageNum, selection);
        initialize(channel.sendMessage(msg), selection, pageNum);
    }

    /**
     * Displays this SelectionDialog by editing the provided
     * {@link Message Message}, starting with the number selection
     * provided.
     *
     * @param message   The Message to display the Menu in
     * @param selection The number selection to start on
     */
    public void showDialog(Message message, int selection, int pageNum) {
        if (selection < 1)
            selection = 1;
        else if (selection > choices.size())
            selection = choices.size();
        if (pageNum < 1)
            pageNum = 1;
        else if (pageNum > pages)
            pageNum = pages;
        Message msg = renderPage(pageNum, selection);
        initialize(message.editMessage(msg), selection, pageNum);
    }

    private void initialize(RestAction<Message> action, int selection, int pageNum) {
        action.queue(m -> {
            if (choices.size() > 1) {
                m.addReaction(UP).queue();
                m.addReaction(SELECT).queue();
                m.addReaction(CANCEL).queue();
                m.addReaction(DOWN).queue();
            } else {
                m.addReaction(SELECT).queue();
                m.addReaction(CANCEL).queue(v -> selectionDialog(m, selection, pageNum), v -> selectionDialog(m, selection, pageNum));
            }
            if (pages > 1) {
                if (bulkSkipNumber > 1)
                    m.addReaction(BIG_LEFT).queue();
                m.addReaction(LEFT).queue();
                if (bulkSkipNumber > 1)
                    m.addReaction(RIGHT).queue();
                m.addReaction(bulkSkipNumber > 1 ? BIG_RIGHT : RIGHT).queue(
                        v -> selectionDialog(m, selection, pageNum), v -> selectionDialog(m, selection, pageNum));
            }
        });
    }

    private void selectionDialog(Message message, int selection, int pageNum) {
        waiter.waitForEvent(MessageReactionAddEvent.class, event -> {
            if (!event.getMessageId().equals(message.getId()))
                return false;
            if (!(UP.equals(event.getReaction().getReactionEmote().getName())
                    || DOWN.equals(event.getReaction().getReactionEmote().getName())
                    || CANCEL.equals(event.getReaction().getReactionEmote().getName())
                    || LEFT.equals(event.getReaction().getReactionEmote().getName())
                    || RIGHT.equals(event.getReaction().getReactionEmote().getName())
                    || BIG_LEFT.equals(event.getReaction().getReactionEmote().getName())
                    || BIG_RIGHT.equals(event.getReaction().getReactionEmote().getName())
                    || SELECT.equals(event.getReaction().getReactionEmote().getName())))
                return false;
            return isValidUser(event.getUser(), event.isFromGuild() ? event.getGuild() : null);
        }, event -> {
            int newPageNum = pageNum;
            int newSelection = selection;
            switch (event.getReaction().getReactionEmote().getName()) {
                case UP:
                    if (newSelection > 1)
                        newSelection--;
                    else if (loop)
                        newSelection = choices.size();
                    break;
                case DOWN:
                    if (newSelection < choices.size())
                        newSelection++;
                    else if (loop)
                        newSelection = 1;
                    break;
                case LEFT:
                    if (newPageNum == 1 && wrapPageEnds)
                        newPageNum = pages + 1;
                    if (newPageNum > 1)
                        newPageNum--;
                    break;
                case RIGHT:
                    if (newPageNum == pages && wrapPageEnds)
                        newPageNum = 0;
                    if (newPageNum < pages)
                        newPageNum++;
                    break;
                case BIG_LEFT:
                    if (newPageNum > 1 || wrapPageEnds) {
                        for (int i = 1; (newPageNum > 1 || wrapPageEnds) && i < bulkSkipNumber; i++) {
                            if (newPageNum == 1)
                                newPageNum = pages + 1;
                            newPageNum--;
                        }
                    }
                    break;
                case BIG_RIGHT:
                    if (newPageNum < pages || wrapPageEnds) {
                        for (int i = 1; (newPageNum < pages || wrapPageEnds) && i < bulkSkipNumber; i++) {
                            if (newPageNum == pages)
                                newPageNum = 0;
                            newPageNum++;
                        }
                    }
                case SELECT:
                    success.accept(message, selection);
                    break;
                case CANCEL:
                    cancel.accept(message);
                    return;

            }
            try {
                event.getReaction().removeReaction(event.getUser()).queue();
            } catch (PermissionException ignored) {
            }
            int n = newSelection;
            int o = newPageNum;
            if (!noUpdate) {
                message.editMessage(renderPage(n, o)).queue(m -> selectionDialog(m, n, o));
            }
        }, timeout, unit, () -> cancel.accept(message));
    }

    public void setNoUpdate(boolean update) {
        noUpdate = update;
    }

    private Message renderPage(int pageNum, int selection) {
        MessageBuilder mbuilder = new MessageBuilder();
        EmbedBuilder ebuilder = new EmbedBuilder();
        int start = (pageNum - 1) * itemsPerPage;
        int end = Math.min(choices.size(), pageNum * itemsPerPage);


        for (int i = start; i < end; i++) {
            if (i + 1 == selection) {
                ebuilder.addField((numberItems ? "`" + (i + 1) + ".` " : "") + leftEnd + choices.get(i).itemName + rightEnd, "Price: " + choices.get(i).value, true);
            } else {
                ebuilder.addField((numberItems ? "`" + (i + 1) + ".` " : "") + defaultLeft + choices.get(i).itemName + defaultRight, "Price: " + choices.get(i).value, true);
            }
        }

        ebuilder.setColor(color.apply(pageNum, pages));
        if (showPageNumbers)
            ebuilder.setFooter("Page " + pageNum + "/" + pages, null);
        mbuilder.setEmbed(ebuilder.build());
        if (text != null)
            mbuilder.append(text.apply(pageNum, pages));
        return mbuilder.build();
    }

    /**
     * The {@link Menu.Builder Menu.Builder} for
     * a {@link StoreDialog SelectuibDialog}.
     *
     * @author John Grosh
     */
    @SuppressWarnings({"UnusedReturnValue", "unused"})
    public static class Builder extends Menu.Builder<Builder, StoreDialog> {
        private List<ShopItem> choices = new LinkedList<>();
        private String leftEnd = "";
        private String rightEnd = "";
        private int itemsPerPage = 12;
        private boolean showPageNumbers = true;
        private boolean numberItems = false;
        private String defaultLeft = "";
        private String defaultRight = "";
        private boolean loop = true;
        private BiFunction<Integer, Integer, Color> color = (page, pages) -> null;
        private BiFunction<Integer, Integer, String> text = (page, pages) -> null;
        private BiConsumer<Message, Integer> selection;
        private Consumer<Message> cancel = (m) -> {
        };

        /**
         * Builds the {@link StoreDialog SelectionDialog}
         * with this Builder.
         *
         * @return The OrderedMenu built from this Builder.
         * @throws IllegalArgumentException If one of the following is violated:
         *                                  <ul>
         *                                      <li>No {@link EventWaiter EventWaiter} was set.</li>
         *                                      <li>No choices were set.</li>
         *                                      <li>No action {@link Consumer Consumer} was set.</li>
         *                                  </ul>
         */
        @Override
        public StoreDialog build() {
            Checks.check(waiter != null, "Must set an EventWaiter");
            Checks.check(!choices.isEmpty(), "Must have at least one choice");
            Checks.check(selection != null, "Must provide a selection consumer");

            return new StoreDialog(waiter, users, roles, timeout, unit, choices, leftEnd, rightEnd, itemsPerPage, showPageNumbers, numberItems,
                    defaultLeft, defaultRight, color, loop, selection, cancel, text);
        }

        /**
         * Sets the {@link Color Color} of the {@link net.dv8tion.jda.api.entities.MessageEmbed MessageEmbed}.
         *
         * @param color The Color of the MessageEmbed
         * @return This builder
         */
        public Builder setColor(Color color) {
            this.color = (i0, i1) -> color;
            return this;
        }

        /**
         * Sets the {@link Color Color} of the {@link net.dv8tion.jda.api.entities.MessageEmbed MessageEmbed},
         * relative to the current selection number as determined by the provided
         * {@link Function Function}.
         * <br>As the selection changes, the Function will re-process the current selection number,
         * allowing for the color of the embed to change depending on the selection number.
         *
         * @param color A Function that uses current selection number to get a Color for the MessageEmbed
         * @return This builder
         */
        public Builder setColor(BiFunction<Integer, Integer, Color> color) {
            this.color = color;
            return this;
        }

        /**
         * Sets the text of the {@link Message Message} to be displayed
         * when the {@link StoreDialog SelectionDialog} is built.
         *
         * <p>This is displayed directly above the embed.
         *
         * @param text The Message content to be displayed above the embed when the SelectionDialog is built
         * @return This builder
         */
        public Builder setText(String text) {
            this.text = (i0, i1) -> text;
            return this;
        }

        /**
         * Sets the text of the {@link Message Message} to be displayed
         * relative to the current selection number as determined by the provided
         * {@link Function Function}.
         * <br>As the selection changes, the Function will re-process the current selection number,
         * allowing for the displayed text of the Message to change depending on the selection number.
         *
         * @param textBiFunction A Function that uses current selection number to get a text for the Message
         * @return This builder
         */
        public Builder setText(BiFunction<Integer, Integer, String> textBiFunction) {
            this.text = textBiFunction;
            return this;
        }

        public Builder setItemsPerPage(int num) {
            if (num < 1)
                throw new IllegalArgumentException("There must be at least one item per page");
            this.itemsPerPage = num;
            return this;
        }

        /**
         * Sets whether or not the page number will be shown.
         *
         * @param show {@code true} if the page number should be shown, {@code false} if it should not
         * @return This builder
         */
        public Builder showPageNumbers(boolean show) {
            this.showPageNumbers = show;
            return this;
        }

        /**
         * Sets whether or not the items will be automatically numbered.
         *
         * @param number {@code true} if the items should be numbered, {@code false} if it should not
         * @return This builder
         */
        public Builder useNumberedItems(boolean number) {
            this.numberItems = number;
            return this;
        }


        public Builder setSelectedEnds(String left, String right) {
            this.leftEnd = left;
            this.rightEnd = right;
            return this;
        }

        /**
         * Sets the text to use on either side of all unselected items. This will not
         * be applied to the selected item.
         * <br>Usage is primarily to mark which items are not currently selected.
         *
         * @param left  The left non-selection end
         * @param right The right non-selection end
         * @return This builder
         */
        public Builder setDefaultEnds(String left, String right) {
            this.defaultLeft = left;
            this.defaultRight = right;
            return this;
        }

        /**
         * Sets if moving up when at the top selection jumps to the bottom, and visa-versa.
         *
         * @param loop {@code true} if pressing up while at the top selection should loop
         *             to the bottom, {@code false} if it should not
         * @return This builder
         */
        public Builder useLooping(boolean loop) {
            this.loop = loop;
            return this;
        }

        /**
         * Sets a {@link BiConsumer BiConsumer} action to perform once a selection is made.
         * <br>The {@link Message Message} provided is the one used to display
         * the menu and the {@link Integer Integer} is that of the selection made by the user,
         * and selections are in order of addition, 1 being the first String choice.
         *
         * @param selection A Consumer for the selection. This is one-based indexing.
         * @return This builder
         */
        public Builder setSelectionConsumer(BiConsumer<Message, Integer> selection) {
            this.selection = selection;
            return this;
        }

        /**
         * Sets a {@link Consumer Consumer} action to take if the menu is cancelled, either
         * via the cancel button being used, or if the SelectionDialog times out.
         *
         * @param cancel The action to take when the SelectionDialog is cancelled
         * @return This builder
         */
        public Builder setCanceled(Consumer<Message> cancel) {
            this.cancel = cancel;
            return this;
        }

        /**
         * Clears the choices to be shown.
         *
         * @return This builder
         */
        public Builder clearChoices() {
            this.choices.clear();
            return this;
        }

        /**
         * Sets the String choices to be shown as selections.
         *
         * @param choices The String choices to show
         * @return the builder
         */
        public Builder setChoices(LinkedList<ShopItem> choices) {
            this.choices.clear();
            this.choices = choices;
            return this;
        }

        /**
         * Adds String choices to be shown as selections.
         *
         * @param choices The String choices to add
         * @return This builder
         */
        public Builder addChoices(List<ShopItem> choices) {
            this.choices.addAll(choices);
            return this;
        }
    }
}
