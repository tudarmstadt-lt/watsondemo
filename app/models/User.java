/*
 * Copyright 2015 Technische Universitaet Darmstadt
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
 *
 * Contributors:
 *         - Uli Fahrer
 */

package models;


import play.Logger;
import play.db.ebean.Model;
import util.AppException;
import util.Hash;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User extends Model {
    @Id
    public Integer id;
    public String username;

    public String passwordHash;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="user_to_question")
    public List<Question> questionList = new ArrayList<>();

    public static Finder<Integer, User> find = new Finder(Integer.class, User.class);

    private User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public static User create(String username, String password) throws AppException {
        User user = new User(username, Hash.createPassword(password));
        user.save();
        return user;
    }

    public static void createGuestUser() {
        try {
            User guest = User.create("guest", "guest");
            guest.save();
        } catch (AppException e) {
            Logger.error("Can't create guest user");
        }
    }

    public Question addQuestion(String question) {
        Question questionEntry = Question.create(question);
        questionList.add(questionEntry);
        return questionEntry;
    }

    /**
     * Checks whether a user exists in the database.
     *
     * @param username username to check
     * @return <code>true</code> if the user exists. <code>False</code> otherwise.
     */
    public static boolean userExists(String username) {
        return find.where().eq("username", username).findRowCount() != 0;
    }

    /**
     * Retrieve a user from an username.
     *
     * @param username username to search
     * @return a user
     */
    public static User findByUsername(String username) {
        return find.where().eq("username", username).findUnique();
    }

    /**
     * Authenticate a User, from a username and clear password.
     *
     * @param username username
     * @param clearPassword clear password
     * @return User if authenticated, null otherwise
     * @throws AppException App Exception
     */
    public static User authenticate(String username, String clearPassword) throws AppException {

        User user = find.where().eq("username", username).findUnique();
        if (user != null) {
            if (Hash.checkPassword(clearPassword, user.passwordHash)) {
                return user;
            }
        }
        return null;
    }
}
